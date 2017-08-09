package com.test.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues="user")
public class UserReceiver {
	@RabbitHandler
	public void process(RabbitTest rt){
		System.out.println("user receive:"+rt.getName()+"/"+rt.getPass());
	}

}
