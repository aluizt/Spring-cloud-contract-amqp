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
    
Tambem teremos o plugin abaixo responsável pelo gerenciamento dos contratos.
   
    * apply plugin: 'maven-publish'
    
Teremos a task contracts, responsável por informar o local de onde estara nossa classe base para testes ( veremos vais adiante ).

    contracts {
          baseClassForTests = 'br.com.rabbitspringcloudcontractprodutor.TestBase'
          contractsMode = "LOCAL"
    }
    

A task publishStubstoScm sera utilizada para informar o local de nossos contratos, neste exemplo estamos utilizando um repositório do GitHub.

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
 
      
Para salvar nossos contratos no repositório git ou maven local sera utilizada as tasks abaixo respectivamente.
  
    publish.dependsOn("publishStubsToScm")
  
    publishToMavenLocal.dependsOn("publishStubsToScm")
   
   


