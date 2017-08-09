package com.test.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicSender {
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	public void send(){
		
		String msg1 = "I am topic.mesaage msg======";
        this.amqpTemplate.convertAndSend("topicExchange", "topic.msg", msg1);
        
        String msg2 = "I am topic.mesaages msg########";
        this.amqpTemplate.convertAndSend("topicExchange", "topic.message2", msg2); 
	}
}
