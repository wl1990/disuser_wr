package com.test.redis.dislock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ShardedJedisPool;


/**
 * 发布订阅模式的分布式锁
 * @author wanglei-ds20
 *
 */
@Service
public class MyDisLock {
	private static final Logger logger=LoggerFactory.getLogger(MyDisLock.class);
	private static final Integer EXPIRE_TIME=60*1000;// 缓存过期时间
	private static final ExecutorService exec=Executors.newFixedThreadPool(10);
	@Autowired
	private ShardedJedisPool shardedJedisPool;
	/**
	 * 获取连接
	 * @return
	 */
	public Jedis getRedisConnection(String key){
		return shardedJedisPool.getResource().getShard(key);// 待改进
	}
	/**
	 * 加锁
	 * @param key 锁的粒度
	 * @return
	 */
	public boolean tryLock(String key){
		MySubscribe mysub=new MySubscribe();
		try {
			Jedis jedis=getRedisConnection(key);
			jedis.expire(key,EXPIRE_TIME); // 设置过期时间
			Semaphore semaphore=new Semaphore(0);
			for(int i=0;i<10;i++){
				// 争抢锁
				Long result=jedis.incr(key);
				if(result>1){ // 加锁失败，等待唤醒
					// 处理订阅信息
					operateSubscribe(jedis,mysub,semaphore,key);
					semaphore.acquire(); // 阻塞中，等待释放锁的消息
					// 再次争抢锁
					continue;
				}else if(result==1){ // 加锁成功   
					return true;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			if(mysub!=null){ // 释放通道
				mysub.unsubscribe();
			}
		}
		return false;
	}
	
	/**
	 * 释放锁 
	 * @param key
	 */
	public void releaseLock(String key){
		try{
			Jedis jedis=getRedisConnection(key);
			jedis.expire(key,EXPIRE_TIME); // 设置过期时间
			jedis.del(key);
			jedis.publish(key, "释放锁 通道:"+key);
		}catch(Exception e){
			logger.error("释放分布式锁异常 key="+key,e);
		}finally{
			logger.info("释放锁结束");
		}
	}
	/**
	 * 处理订阅消息 
	 * @param semaphore 信号量
	 */
	private void operateSubscribe(Jedis jedis,JedisPubSub mysub,Semaphore semaphore,String key) {
		exec.execute(new Runnable(){
			public void run(){
				jedis.subscribe(mysub,key);
				// 释放信号量
				semaphore.release();
			}
		});
	}
	
}
