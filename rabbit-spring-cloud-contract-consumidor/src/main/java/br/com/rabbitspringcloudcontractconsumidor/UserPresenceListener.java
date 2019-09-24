package br.com.rabbitspringcloudcontractconsumidor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserPresenceListener {


    UserGoesOnlineMessage user = new UserGoesOnlineMessage();

    @RabbitListener(
            bindings = @QueueBinding(
            value = @Queue(value = "userQueue"),
            exchange = @Exchange(value = "userExchange")))
    public void handle(UserGoesOnlineMessage userGoesOnlineMessage) {
        log.info("User : " + userGoesOnlineMessage.getUser());
        user.setUser(userGoesOnlineMessage.getUser());
    }

    public UserGoesOnlineMessage getUser(){
        return user;
    }

}