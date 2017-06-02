package com.test.redis;

import java.util.HashMap;
import java.util.Map;

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
		Map<String,String> map=new HashMap<String,String>();
		map.put("123", "234");
		j.hmset("hkey", map);
		Map<String,String> map1=new HashMap<String,String>();
		map1.put("q", "qq");
		j.hmset("hkey", map1);
		System.out.println(j.hexists("hkey", "123")+"--"+j.hexists("hkey", "q"));
		j.hdel("hkey", "123");
		System.out.println(j.hexists("hkey", "123")+"--"+j.hexists("hkey", "q"));
	}
}
