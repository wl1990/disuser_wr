package com.test.springevent;

import org.springframework.context.ApplicationEvent;

public class UserRegisterEvent extends ApplicationEvent{
	/**
	 * uid
	 */
	private static final long serialVersionUID = -7550617433311200690L;

	public UserRegisterEvent(String name) {
		super(name);
		
	}
	
	
}
