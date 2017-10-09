package com.test.springevent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements ApplicationListener<UserRegisterEvent>{

	@Override
	public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
		System.out.println("邮件服务接到通知，给 " + userRegisterEvent.getSource() + " 发送邮件...");
	}

}
