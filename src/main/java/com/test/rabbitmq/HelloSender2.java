package com.test.rabbitmq;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloSender2 {
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	public void send(String msg){
		String sndMsg=msg+new Date();
		amqpTemplate.convertAndSend("hello", sndMsg);
	}
	
}
