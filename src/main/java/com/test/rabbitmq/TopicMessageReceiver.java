package com.test.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues="topic1")
public class TopicMessageReceiver {
	@RabbitHandler
	public void process(String msg){
		System.out.println("topicMessageReceiver1  : " +msg);
	}
	
}
