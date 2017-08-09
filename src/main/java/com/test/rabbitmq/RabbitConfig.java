package com.test.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
@Configuration
public class RabbitConfig {
	@Value("${spring.rabbitmq.host}")
	private String address;
	
	@Value("${spring.rabbitmq.port}")
	private String port;
	
	@Value("${spring.rabbitmq.username}")
	private String username;
	
	@Value("${spring.rabbitmq.password}")
	private String password;
	
	@Value("${spring.rabbitmq.vhost}")
	private String virtualhost;
	
	@Value("${spring.rabbitmq.publisher-confirms}")
	private boolean publisherConfirms;
	
	@Bean
	public ConnectionFactory connectionFactory(){
		CachingConnectionFactory connectionFactory=new CachingConnectionFactory();
		connectionFactory.setAddresses(address+":"+port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost(virtualhost);
		connectionFactory.setPublisherConfirms(publisherConfirms);
		return connectionFactory;
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RabbitTemplate rabbitTemplate(){
		RabbitTemplate template=new RabbitTemplate(connectionFactory());
		return template;
	}
	
}
