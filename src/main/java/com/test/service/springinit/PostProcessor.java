package com.test.service.springinit;

import org.springframework.beans.factory.config.BeanPostProcessor;

public class PostProcessor implements BeanPostProcessor{
	@Override
	public Object postProcessBeforeInitialization(Object bean,String beanName){
		System.out.println("--------post process before init---------");
		return bean;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean,String beanName){
		System.out.println("--------post process after init----------");
		return bean;
	}

	public void destroy(){
		System.out.println("-----------destroy---");
	}
	
}
