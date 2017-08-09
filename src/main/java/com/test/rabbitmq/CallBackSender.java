package com.test.rabbitmq;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
@Component
public class CallBackSender implements RabbitTemplate.ConfirmCallback{
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void send(){
		rabbitTemplate.setConfirmCallback(this);
		String msg="callbackSender:i am callback sender";
		CorrelationData correlationData=new CorrelationData(UUID.randomUUID().toString());
		rabbitTemplate.convertAndSend("topicExchange", "topic.msg",msg,correlationData);
	}
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		System.out.println("callbakck confirm: " + correlationData.getId());
	}
	
}
