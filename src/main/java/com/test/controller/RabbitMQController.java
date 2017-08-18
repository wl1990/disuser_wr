package com.test.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.test.rabbitmq.CallBackSender;
import com.test.rabbitmq.FanoutSender;
import com.test.rabbitmq.HelloSender;
import com.test.rabbitmq.HelloSender2;
import com.test.rabbitmq.TopicSender;
import com.test.rabbitmq.UserSender;

@RestController
public class RabbitMQController {
	private static final Logger log=LoggerFactory.getLogger(RabbitMQController.class);
	@Autowired
	private HelloSender helloSender;
	@Autowired
	private HelloSender2 helloSender2;
	@Autowired
	private UserSender userSender;
	@Autowired
	private TopicSender topicSender;
	@Autowired
	private FanoutSender fanoutSender;
	@Autowired
	private CallBackSender callBackSender;
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public String addUser(){
		try{
		 helloSender.send();
		}catch(Exception e){
			return "send msg failed";
		}
		return "send msg success";
	}
	
	@RequestMapping(value="/onToMany",method=RequestMethod.POST)
	public String oneToMany(){
		for(int i=0;i<10;i++){
			helloSender.send("msg:"+i);
			log.info("msg:"+i);
		}
		return "send success";
	}
	
	@RequestMapping(value="/mangTomang",method=RequestMethod.POST)
	public String mangTomany(){
		for(int i=0;i<10;i++){
			helloSender.send("sendmsg："+i);
			helloSender2.send("sendmsg2："+i);
		}
		return "send msg success";
	}
	@RequestMapping(value="/clazztest",method=RequestMethod.POST)
	public String clazzTest(){
		userSender.send();
		return "send msg success";
	}
	
	@RequestMapping(value="/topictest",method=RequestMethod.POST)
	public String topicTest(){
		topicSender.send();
		return "send msg success";
	}
	
	@RequestMapping(value="/fanoutTest",method=RequestMethod.POST)
	public String fanoutTest(){
		fanoutSender.send();
		return "send msg success";
	}
	
	@RequestMapping(value="/callbackTest",method=RequestMethod.POST)
	public String callbackTest(){
		callBackSender.send();
		return "send msg success";
	}
}
