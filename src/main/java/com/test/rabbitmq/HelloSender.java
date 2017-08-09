package com.test.rabbitmq;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class HelloSender {
	@Autowired
    private AmqpTemplate rabbitTemplate;
 
    public void send() {
        String context = "hello " + new Date();
        this.rabbitTemplate.convertAndSend("hello", context);
    }
    public void send(String msg) {
        this.rabbitTemplate.convertAndSend("hello", msg);
    }
}
