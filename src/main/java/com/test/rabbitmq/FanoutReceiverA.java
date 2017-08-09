package com.test.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues="fanout1")
public class FanoutReceiverA {
	@RabbitHandler
	public void process(String msg){
		System.out.println("FanoutReceiver 1  : " + msg);
	}
}
