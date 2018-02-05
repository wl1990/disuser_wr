package com.test.rate.limiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 令牌桶
 * @author wanglei-ds20
 *
 */

public class TokenBucket {
	private static  Long time=System.currentTimeMillis();
	private static final int rate=3;
	private static final int state=10;
	private static int token=0;
	
	public static boolean tokenbucket(){
		synchronized(time){
			Long now=System.currentTimeMillis();
			int incr=(int) ((now-time)/rate);
			token+=incr;
			token=Math.min(state, token);
			time=now;
			if(token>0){
				--token;
				return true;
			}else{
				return false;
			}
		}
	}
	
	public static void tokenbucketTest(){
		for(int i=0;i<20;i++){
			new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(tokenbucket()){
						System.out.println("----token test----");
					}else{
						System.out.println("-----限流-----");
					}
				}
				
			}).start();
		}
	}
	
	public static void ratelimitTest(){
		RateLimiter rateLimiter=RateLimiter.create(10);
		ExecutorService threadPool=Executors.newCachedThreadPool();
		List<Runnable> tasks=new ArrayList<Runnable>();
		for(int i=0;i<100;i++){
			tasks.add(new UserRequest(i));
		}
		for(Runnable runnable:tasks){
			if(!rateLimiter.tryAcquire(100,TimeUnit.MILLISECONDS)){
				System.out.println("短期内无法获得令牌");
			}else{
				threadPool.execute(runnable);
			}
		}
	}
	
	private static class UserRequest implements Runnable{
		private int id;
		public UserRequest(int id){
			this.id=id;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				System.out.println(id);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ratelimitTest();
	}
}
