package com.test.service.springinit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.InitializingBean;

public class InitBeanTest extends PostProcessor implements InitializingBean{
	static{
		System.out.println("--------static-----");
	}
	public void afterPropertiesSet(){
		System.out.println("------after properties-----");
	}
	@PostConstruct
	public void init(){
		System.out.println("------init method----");
	}
	
	@PreDestroy
	public void destroy(){
		System.out.println("-----------destroy---");
	}
}
