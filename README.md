# Spring-cloud-contract-amqp

Poc demonstrando a utilização do Spring Cloud Contract junto com o RabbitMQ.

Criaremos um exemplo simples onde um produtor enviara uma mensagem contendo os atributos de um usuario, esta mensagem estara no formato JSON.

Os principais objetivos do Spring Cloud Contract são:

    * Garantir que os stubs HTTP e Messaging (usados no lado do cliente) façam exatamente o que a imple- 
      mentação real do lado do servidor faz.

    * Fornecer uma maneira de publicar alterações nos contratos que são imediatamente visíveis em ambos
      os lados.

    * Gerar o código de teste padrão para ser usado no lado do servidor.

O reteiro sera o seguinte.
   1. Criaremos um contrato do lado do produtor.
   2. Criaremos uma implementação do lado do consumidor que ira ouvir uma fila do RabbitMQ que cumpra o contrato.
   3. Criaremos testes do lado do consumidor para garantir que o mesmo cumpra o contrato.
   4. Criaremos uma implementação do lado do produtor que envie mensagens seguindo as especificações que estão no contrato.
   5. Gerar de forma automática os testes do lado do produtor para que o mesmo tambem cumpra com o contrato.
   

### Produtor.

Nosso produtor ira enviar uma mensagem com os dados de um usuario. 
Para isso, utilizaremos as seguintes dependencias em nosso build.gradle
    
    * spring-boot-starter-web
    * org.projectlombok:lombok
    * spring-boot-starter-test
    * spring-cloud-starter-contract-verifier
    * spring-cloud-contract-wiremock
    * spring-boot-starter-amqp
    * spring-cloud-contract-gradle-plugin
    
Também teremos o plugin abaixo responsável pelo gerenciamento dos contratos.
   
    * apply plugin: 'maven-publish'
    
Teremos a task contracts, responsável por informar o local onde estará nossa classe base para testes ( veremos mais adiante ).

    contracts {
          baseClassForTests = 'br.com.rabbitspringcloudcontractprodutor.TestBase'
          contractsMode = "LOCAL"
    }
    

A task publishStubstoScm será utilizada para informar o local de nossos contratos, neste exemplo estamos utilizando um repositório do GitHub.

    publishStubsToScm{
      customize {
        contractDependency {
            stringNotation = "br.com:rabbit-spring-cloud-contract-produtor:+"
        }

        contractRepository{
            repositoryUrl = "git://https://github.com/aluizt/stubs.git"
            username = "${gitUser}"
            password = "${gitPassword}"
        }       
      }
    }
 
      
Para salvar nossos contratos no repositório git ou maven local será utilizada as tasks abaixo respectivamente.
  
    publish.dependsOn("publishStubsToScm")
  
    publishToMavenLocal.dependsOn("publishStubsToScm")
   
No Spring Cloud Contract, um contrato pode ser definido em um arquivo Groovy, YAML ou Pact. Para este exemplo estaremos utilizando um arquivo YAML.

No exemplo abaixo temos :
   
    * label, informação que sera utilizada pelo consumidor para localizar o contrato.
    * triggeredBy, nome do metodo que será utilizado pela classe base de teste.
    * sentTo, nome da exchange para onde a mensagem será enviada.
    
    Neste exemplo estaremos utilizando expressões regulares para validar os atributos de nosso usuário.
      Na nossa classe de testes sera verificado se o atributo name é do tipo String.
      
      - path: $.name
        type: by_type        
     
      Na nossa classe de testes sera verificado se o conteudo do atributo name contem caracteres de A até Z, os acentos  
      ortográficos e no minimu um caractere e no máximo 50, alem de espaços em branco.
      Se contiver algum digito numérico o testes ira falhar.
      
      - path: $.name
        type: by_regex
        value: "[a-zA-Z . \u00C0-\u00ff]{1,50}"
        

Arquivo user.yaml

```
description: Message sending test
label: userTest
input:
  triggeredBy: sendUserMessage()
outputMessage:
  sentTo: userExchange
  body:
    name: João da Silva
    address: Rua Figueira
    number: 318
  matchers:
    body:
      - path: $.name
        type: by_type
      - path: $.name
        type: by_regex
        value: "[a-zA-Z . \u00C0-\u00ff]{1,50}"
      - path: $.address
        type: by_type
      - path: $.address
        type: by_regex
        value: "[a-zA-Z .]{1,50}"
      - path: $.number
        type: by_type
      - path: $.number
        type: by_regex
        value: "[0-9]{1,3}"
  headers:
    contentType: application/json
```

Podemos publicar este contrato em nosso repositório git executando a task publishStubsToScm.

    ./gradlew publishStubsToScm
    
Exemplo:
![poc-rabbit-contract-01](https://user-images.githubusercontent.com/41808527/65959526-9ae47900-e428-11e9-92c6-b5d360d5940f.png)

##### Obs: 

Em nosso repositório git temos de ter a seguinte estrutura.

META-INF/br.com/rabbit-spring-cloud-contract-produtor/0.0.1-SNAPSHOT/contracts/

Onde:
     br.com,   é o group-id.                       
     rabbit-spring-cloud-contract-produtor,   é o artifact-id                     
     0.0.1-SNAPSHOT,   é a versão                          
     contracts,   o local onde sera armazenado os contratos                
     
     
### Consumidor.
 
Agora vamos trabalhar no lado que ira consumir as mensagens, para isto teremos as seguintes dependencias em nosso gradle.build.

    * spring-boot-starter-amqp
    * spring-boot-starter-json
    * org.projectlombok:lombok
    * spring-boot-starter-parent
    * spring-boot-starter-test
    * spring-cloud-starter-contract-stub-runner
    
Vamos criar uma classe responsavé por ouvir a fila de mensagens que seram enviadas pelo produtor, para isto sera utilizado o Spring AMQP.

A anotação @RabbitListener neste exemplo estará fazendo o binding entre a fila userQueue e a exchange userExchange.
No método listenerUser a mensagem sera convertida em um objeto User que sera utilizado durante a execução dos testes.
O mesmo ira ocorrer com o método listenerInvoice.

```
@Component
@Slf4j
public class UserListener {

    User user = new User();
    Invoice invoice = new Invoice();

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "userQueue"),exchange = @Exchange(value = "userExchange")))
    public void listenerUser(User userReceived) {
        printUserLog(setUser(userReceived));
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "invoiceQueue"),exchange = @Exchange(value = "invoiceExchange")))
    public void listenerInvoice(Invoice invoiceReceived) {
        printInvoiceLog(setInvoice(invoiceReceived));
    }

    private Invoice setInvoice(Invoice invoiceReceived){
        invoice = invoiceReceived;
        return invoice;
    }

    private User setUser(User userReceived){
        user = userReceived;
        return user;
    }

    public User getUser(){
        return user;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    private void printUserLog(User userReceived){
        log.info("User : " + userReceived.getName());
    }
    private void printInvoiceLog(Invoice invoiceReceived){
        log.info("Invoice number : " + invoiceReceived.getNumber());
    }
}
```

Criamos uma classe config, onde temos um bean "jackson2JsonMessageConverter" utilizado para manipular a conversão de mensagens de JSON para objeto de domínio.

```
@Configuration
public class Config {

    @Bean
    MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
```

### Testes do lado do consumidor.

Agora podemos criar nossos testes, para isto o Stub Runner simulará o fluxo de mensagens do broker RabbitMQ na memória, sendo guiado pelo Contrato.

Para a realização dos testes não é necessário o RabbitMQ.

#### Configurando o Stub Runner

Vamos iniciar criando um arquivo application.properties em src/test/resources com o seguinte conteudo.

      stubrunner.amqp.enabled=true
      stubrunner.stream.enabled=false
      stubrunner.integration.enabled=false
      
Criando o teste.

```
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "br.com:rabbit-spring-cloud-contract-produtor:+:6565",
        repositoryRoot = "git://https://github.com/aluizt/stubs.git",
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
public class ListenerTest {

    @Autowired
    StubTrigger stubTrigger;

    @Autowired
    UserListener userListener;

    @Test
    public void shouldReceiveMessageWithUserAlexandre() {
        stubTrigger.trigger("userTest");
        User response = this.userListener.getUser();

        assertEquals(response.getName(), "João da Silva");
    }

    @Test
    public void shouldReceiveMessageWithInvoice() {
        Integer number = 199887;
        stubTrigger.trigger("invoiceTest");
        Invoice response = this.userListener.getInvoice();

        assertEquals(response.getNumber(), number);
    }
}
```
Utilizamos a anotação @AutoConfigureStubRunner para informar o local onde esta armazenado nossos arquivos de contrato.

      ids = "br.com:rabbit-spring-cloud-contract-produtor:+:6565"
      
      br.com,   é o group-id.                       
      rabbit-spring-cloud-contract-produtor,   é o artifact-id 
      + faz referencia a ultima versão.
      6565 a porta que sera utilizada para simular o RabbitMQ
      
      repositoryRoot = "git://https://github.com/aluizt/stubs.git", informa o local dos contratos.
      
      stubsMode = StubRunnerProperties.StubsMode.REMOTE), REMOTE ou LOCAL
      
Vamos simular o seguinte cenário: uma mensagem será enviada para uma exchange no RabbitMQ, depois roteada para uma fila e finalmente processada pelo Consumidor. Neste caso vamos utilizar o StubTrigger que é capaz de simular interações de mensagens.

Através do subTrigger.trigger , criamos o cenário de contrato necessário por meio da label que foi especificado no arquivo  YAML.

Nesse teste , o StubRunner configurará a simulação do RabbitMQ na memória e criará a infraestrutura necessária do cliente RabbitMQ. 

O subTrigger fica ouvindo o metodo this.userListener.getUser() e envia a mensagem conforme o especificado no contrato, após efetua o teste.

#### Produtor.

Voltando ao produtor esta na hora de criarmos a classe resposável por enviar as mensagens e a clase de teste que verifica o contrato do lado do produtor.

Neste exemplo criamos uma classe que envia a cada 3 segundos uma mensagem contendo os dados de um usuário e outra contendo os dados de uma fatura.

```
public class Send {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Scheduled(fixedDelay = 3000)
    public void sendUserMessage() {
        amqpTemplate.convertAndSend("userExchange", "*.*",Data.getUser());
    }

    @Scheduled(fixedDelay = 3000)
    public void sendInvoiceMessage() {
        amqpTemplate.convertAndSend("invoiceExchange", "*.*",Data.getInvoice());
    }
}
```
Criamos uma classe config, onde temos um bean "jackson2JsonMessageConverter" utilizado para manipular a conversão de mensagens de JSON para objeto de domínio.

```
@Configuration
public class Config {
    
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
```




      






    




   


