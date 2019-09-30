package br.com.rabbitspringcloudcontractprodutor.send;

import br.com.rabbitspringcloudcontractprodutor.util.*;
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
    public void sendUserMessage() {
        amqpTemplate.convertAndSend("userExchange", "*.*",Data.getUser());
    }

    @Scheduled(fixedDelay = 3000)
    public void sendInvoiceMessage() {
        amqpTemplate.convertAndSend("invoiceExchange", "*.*",Data.getInvoice());
    }

}
