package com.test.redis.pubsub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.Jedis;

import com.test.redis.RedisConection;

public class PubSubTest {
	public static ExecutorService exec=Executors.newFixedThreadPool(3);
	public static void main(String[] args) throws InterruptedException {
		Jedis je1=RedisConection.getJedis();
		Jedis je2=RedisConection.getJedis();
		Jedis je3=RedisConection.getJedis();
		MySub sub1=new MySub();
		MySub sub2=new MySub();
		MyLockEntry lock1=new MyLockEntry();
		MyLockEntry lock2=new MyLockEntry();
		sub1.setLock(lock1);
		sub2.setLock(lock2);
		try{
		exec.execute(new Runnable(){
			@Override
			public void run() {
				je1.getClient().setSoTimeout(3000);
				try{
				je1.subscribe(sub1, "hello_foo");
				}finally{
					je1.getClient().rollbackTimeout();
				}
				System.out.println("------sub1---");
			}
				
		});
		exec.execute(new Runnable(){
			@Override
			public void run() {
				je2.getClient().setSoTimeout(3000);
				try{
					je2.subscribe(sub2, "hello_foo");
				}finally{
					je2.getClient().rollbackTimeout();
				}
				System.out.println("------sub2---");
			}
		});
		
		exec.execute(()->{
			MyPub pub=new MyPub();
			pub.myPublish(je3);
		});
		Thread.sleep(1000);
		System.out.println("----------");
		}finally{
			if(sub1!=null)
				sub1.unsubscribe();
			if(sub2!=null)
				sub2.unsubscribe();
		}
	}
}
