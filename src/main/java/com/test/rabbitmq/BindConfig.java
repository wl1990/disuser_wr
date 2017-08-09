package com.test.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BindConfig {
	@Autowired
	private Queue fanout1;
	@Autowired
	private Queue fanout2;
	@Autowired
	private Queue fanout3;
	@Autowired
	private Queue topicQueue1;
	@Autowired
	private Queue topicQueue2;
	@Autowired
	private FanoutExchange fanoutExchange;
	@Autowired
	private TopicExchange topicExchange;
	
	@Bean
	public Binding topicBindMsg(){
		return BindingBuilder.bind(topicQueue1).to(topicExchange).with("topic.msg");
	}
	@Bean
	public Binding topicBindMsgSub(){
		return BindingBuilder.bind(topicQueue2).to(topicExchange).with("topic.#");
	}
	@Bean
	public Binding fanoutBindMsg(){
		return BindingBuilder.bind(fanout1).to(fanoutExchange);
	}
	@Bean
	public Binding fanoutBindMsgA(Queue queue,FanoutExchange exchange){
		return BindingBuilder.bind(fanout2).to(fanoutExchange);
	}
	@Bean
	public Binding fanoutBindMsgB(Queue queue,FanoutExchange exchange){
		return BindingBuilder.bind(fanout3).to(fanoutExchange);
	}
}
