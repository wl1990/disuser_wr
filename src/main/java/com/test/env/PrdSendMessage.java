package com.test.env;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prd")
public class PrdSendMessage implements SendMessage{
	
	@Override
	public void send() {
		System.out.println("prd send");
	}

}
