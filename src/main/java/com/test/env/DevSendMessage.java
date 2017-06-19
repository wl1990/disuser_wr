package com.test.env;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevSendMessage  implements SendMessage{

	@Override
	public void send() {
		System.out.println("send dev");
	}
	
}
