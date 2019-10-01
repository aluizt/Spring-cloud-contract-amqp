# Spring-cloud-contract-amqp

Poc demonstrando a utilização do Spring Cloud Contract junto com o RabbitMQ.

Criaremos um exemplo simples onde um produtor ira gera uma mensagem contendo os atributos de um usuario, esta mensagem estara no formato JSON.

Os principais objetivos do Spring Cloud Contract são:

    * Garantir que os stubs HTTP e Messaging (usados no lado do cliente) façam exatamente o que a imple- 
      mentação real do lado do servidor faz.

    * Fornecer uma maneira de publicar alterações nos contratos que são imediatamente visíveis em ambos
      os lados.

    * Gerar o código de teste padrão para ser usado no lado do servidor.

