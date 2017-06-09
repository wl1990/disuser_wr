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
		Object eval=jedis.eval("return 1",keys,vals);
		System.out.println("--------"+eval);
		keys.add("kk");
		vals.add("yangyang");
		Object eval2=jedis.eval("local tab={} for i=1,#KEYS do tab[i]=redis.call('get',KEYS[i]) end return tab",keys,vals);
		System.out.println("-eval2--"+eval2);
		String lua="local tab={} for i=1,#KEYS do tab[1]=redis.call('get',KEYS[i]) end return tab";
		String scriptLoad=jedis.scriptLoad(lua);
		System.out.println(scriptLoad);
		Object evalsha=jedis.evalsha(scriptLoad,keys,vals);
		System.out.println(evalsha);
	/*	Object pobj=jedis.eval("if (redis.call('exists',KEYS[1])==0) then redis.call('hset',KEYS[1],ARGV[2],1);redis.call('pexpire',KEYS[1],ARGV[1]);return nil;end;"
				+ "if(redis.call('hexists',KEYS[1],ARGV[2])==1) then redis.call('hincrby',KEYS[1],ARGV[2],1);redis.call('pexpire',KEYS[1],ARGV[1]);return nil;end;return redis.call('pttl',KEYS[1]);"
				, Arrays.asList(new String[]{"lock"}),
				Arrays.asList(new String[]{"10000","lock1"}));
		System.out.println("--+++-"+pobj);*/
		
		Object ob=jedis.eval("return redis.call('pttl',KEYS[1])",Arrays.asList(new String[]{"lock"}),Arrays.asList(new String[]{"10000","lock1"}));
		System.out.println("===="+ob);
	}
}
