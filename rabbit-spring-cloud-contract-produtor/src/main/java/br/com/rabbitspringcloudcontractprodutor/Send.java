package br.com.rabbitspringcloudcontractprodutor;

import lombok.NoArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class Send {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Scheduled(fixedDelay = 3000)
    public void sendNotification() {
        amqpTemplate.convertAndSend("userExchange", "*.*", new User("Alexandre"));
        System.out.println("message ...: Enviada");
    }
}
