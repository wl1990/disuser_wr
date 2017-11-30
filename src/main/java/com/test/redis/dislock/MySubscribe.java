package com.test.redis.dislock;

import redis.clients.jedis.JedisPubSub;

public class MySubscribe extends JedisPubSub{
	
	public void onMessage(String channel,String message){
		System.out.println(channel+"----"+message);
	}
}
