package com.test.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues="fanout3")
public class FanoutReceiverC {
	@RabbitHandler
	public void process(String msg){
		System.out.println("FanoutReceiver 3  : " + msg);
	}
}