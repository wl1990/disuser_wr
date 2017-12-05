package com.test.redis.dislock;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.redis.RedisConection;

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
	private static final Integer RETRY_TIME=10;// 重试次数
	private static final ExecutorService exec=Executors.newFixedThreadPool(10);
	private static final String LOCK_SUCCESS="OK";
	private static final String SEI_IF_NOT_EXIST="NX";
	private static final String SET_WITH_EXPIRE_TIME="PX";
	private static final Long RELEASE_SUCCESS=1L;
	private static final Integer PER_WAIT_TIME=5;
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
	 * @param clientKey 客户端标志，释放锁时客户端只能释放自己的锁
	 * @return
	 */
	public boolean tryLock(String key,String clientKey){
		MySubscribe mysub=new MySubscribe();
		try {
			Jedis jedis=getRedisConnection(key);
			Semaphore semaphore=new Semaphore(0);
			for(int i=0;i<RETRY_TIME;i++){
				// 争抢锁
				String result=jedis.set(key,clientKey,SEI_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,EXPIRE_TIME);
				if(!LOCK_SUCCESS.equals(result)){ // 加锁失败，等待唤醒
					// 处理订阅信息
					operateSubscribe(jedis,mysub,semaphore,key);
					semaphore.tryAcquire(PER_WAIT_TIME,TimeUnit.SECONDS); // 阻塞中，等待信号量
					// 再次争抢锁
					continue;
				}else{ // 加锁成功   
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
	 * @param key 锁粒度
	 * @param clientKey 客户端id,解锁权限
	 */
	public boolean releaseLock(String key,String clientKey){
		try{
			Jedis jedis=getRedisConnection(key);
			String script="if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del','KEYS[1]') else return 0 end";
			Object result=jedis.eval(script,Collections.singletonList(key),Collections.singletonList(clientKey));
			if(RELEASE_SUCCESS.equals(result)){
				jedis.publish(key, "释放锁通知等待中的客户端  通道:"+key);
				return true;
			}
		}catch(Exception e){
			logger.error("释放分布式锁异常 key="+key,e);
		}finally{
			logger.info("释放锁结束");
		}
		return false;
	}
	/**
	 * 处理订阅消息 
	 * @param semaphore 信号量
	 */
	private void operateSubscribe(Jedis jedis,JedisPubSub mysub,Semaphore semaphore,String key) {
		exec.execute(new Runnable(){
			public void run(){
				// 阻塞 等待释放锁的消息
				jedis.subscribe(mysub,key);
				// 释放信号量
				semaphore.release();
			}
		});
	}
	
}
