package com.test.springevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventService{
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	public void register(String name){
		System.out.println("user:"+name+" register");
		applicationEventPublisher.publishEvent(new UserRegisterEvent(name));
	}
	
}
