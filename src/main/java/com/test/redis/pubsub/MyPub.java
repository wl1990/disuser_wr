package com.test.redis.pubsub;

import redis.clients.jedis.Jedis;

import com.test.redis.RedisConection;

public class MyPub {
	public void myPublish(Jedis jedis){
		jedis.publish("hello_foo", "test_pub");
	}
}
