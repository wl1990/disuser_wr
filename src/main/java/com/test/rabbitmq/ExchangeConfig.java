package com.test.rabbitmq;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {
	@Bean
	TopicExchange topicExchange(){
		return new TopicExchange("topicExchange");
	}
	
	@Bean
	FanoutExchange fanoutExchange(){
		return new FanoutExchange("fanoutExchange");
	}
	
	
}
