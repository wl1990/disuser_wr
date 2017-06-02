package com.test.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisLock {
	private static Logger logger=LoggerFactory.getLogger(RedisLock.class);
	private  RedisTemplate redisTemplate;
	private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS =100;
	private String lockKey;
	private int expireMsecs=60*1000;
	
}
