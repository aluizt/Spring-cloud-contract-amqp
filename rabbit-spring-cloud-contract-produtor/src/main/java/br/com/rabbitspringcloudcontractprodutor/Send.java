package br.com.rabbitspringcloudcontractprodutor;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Slf4j
public class Send {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Scheduled(fixedDelay = 3000)
    public void sendNotification() {
        amqpTemplate.convertAndSend("userExchange", "*.*",getUser());
    }

    private User getUser(){
        return User.builder()
                .name("Alexandre")
                .address("Rua Figueira")
                .number(479)
                .build();
    }
}
