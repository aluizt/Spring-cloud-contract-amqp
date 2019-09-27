package br.com.rabbitspringcloudcontractconsumidor;

import lombok.AllArgsConstructor;
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

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "userQueue"),exchange = @Exchange(value = "userExchange")))
    public void handle(User userReceived) {
        printLog(setUser(userReceived));
    }

    private User setUser(User userReceived){
        user = userReceived;
        return user;
    }
    private void printLog(User userReceived){
        log.info("User : " + userReceived.getName());
    }
    public User getUser(){
        return user;
    }

}