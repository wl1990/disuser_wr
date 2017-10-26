package com.test.redis.pubsub;

import redis.clients.jedis.JedisPubSub;

public class MySub extends JedisPubSub{
	private MyLockEntry lock;
	
	// 取的订阅的消息后的处理
	public void onMessage(String channel,String message){
//		lock.getLatch().release();
		System.out.println(channel+"----"+message);
	}

	public MyLockEntry getLock() {
		return lock;
	}

	public void setLock(MyLockEntry lock) {
		this.lock = lock;
	}
	
	
}
