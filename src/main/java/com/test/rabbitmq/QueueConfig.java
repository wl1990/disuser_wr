package com.test.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class QueueConfig {
	 @Bean
    public Queue queue() {
        return new Queue("hello");
    }
	 @Bean
	 public Queue userQueue(){
		 return new Queue("user");
	 }
	 @Bean
	 public Queue topicQueue1(){
		 return new Queue("topic1");
	 }
	 @Bean
	 public Queue topicQueue2(){
		 return new Queue("topic2");
	 }
	 @Bean
	 public Queue fanout1(){
		 return new Queue("fanout1");
	 }
	 @Bean
	 public Queue fanout2(){
		 return new Queue("fanout2");
	 }
	 @Bean
	 public Queue fanout3(){
		 return new Queue("fanout3");
	 }
}
