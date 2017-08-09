package com.test.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSender {
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	public void send(){
		RabbitTest rt=new RabbitTest();
		rt.setName("123");
		rt.setPass("456");
		amqpTemplate.convertAndSend("user", rt);
	}
}
