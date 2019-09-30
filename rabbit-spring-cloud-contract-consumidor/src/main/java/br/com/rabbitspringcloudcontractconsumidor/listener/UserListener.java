package br.com.rabbitspringcloudcontractconsumidor.listener;

import br.com.rabbitspringcloudcontractconsumidor.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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