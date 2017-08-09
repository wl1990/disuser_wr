package com.test.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues="fanout2")
public class FanoutReceiverB {
	@RabbitHandler
	public void process(String msg){
		System.out.println("FanoutReceiver 2  : " + msg);
	}
}
