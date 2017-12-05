package com.test.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import redis.clients.jedis.Jedis;

public class RedisLuaTest {
	public static void main(String[] args) {
		List<String> keys=new ArrayList<String>();
		List<String> vals=new ArrayList<String>();
		Jedis jedis=RedisConection.getJedis();
		/*keys.add("kk");
		vals.add("yangyang");
		Object pobj=jedis.eval("if (redis.call('exists',KEYS[1])==0) then return 0;end;return 1",
				Arrays.asList(new String[]{"lock"}),
				Arrays.asList(new String[]{"10000","lock1"}));
		System.out.println(pobj);
		pobj=jedis.eval("if (redis.call('exists',KEYS[1])==0) then redis.call('hset',KEYS[1],ARGV[2],1);redis.call('pexpire',KEYS[1],ARGV[1]);return nil;end;"
				+ "if(redis.call('hexists',KEYS[1],ARGV[2])==1) then redis.call('hincrby',KEYS[1],ARGV[2],1);redis.call('pexpire',KEYS[1],ARGV[1]);return nil;end;"
				+ "return redis.call('pttl',KEYS[1]);"
				, Arrays.asList(new String[]{"lock"}),
				Arrays.asList(new String[]{"20000","lock1"}));
		System.out.println("--+++-"+(Long)pobj);
		
		pobj=jedis.eval("if (redis.call('exists',KEYS[1])==1) then return redis.call('hget',KEYS[1],ARGV[2]);end;return nil",
				Arrays.asList(new String[]{"lock"}),
				Arrays.asList(new String[]{"10000","lock1"}));
		System.out.println(pobj);
		Object ob=jedis.eval("return redis.call('pttl',KEYS[1])",Arrays.asList(new String[]{"lock"}),Arrays.asList(new String[]{"10000","lock1"}));
		System.out.println("===="+ob);*/
		
		

	}
}
