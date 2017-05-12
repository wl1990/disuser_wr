package com.test.redis;

import redis.clients.jedis.Jedis;

public class RedisConection {
	public static Jedis getJedis(){
		Jedis jedis=new Jedis("127.0.0.1",6379);
		return jedis;
	}
	
	public static void main(String[] args) {
		Jedis j=getJedis();
		j.set("223", "234");
		System.out.println(j.get("223"));
		
	}
}
