package com.test.redis.pubsub;

import java.util.concurrent.Semaphore;

import redis.clients.jedis.JedisPubSub;

public class MyLockEntry {
	private int counter;
	private final Semaphore latch;
	private JedisPubSub jedisPubSub;

	public synchronized void aquire() {
		this.counter += 1;
	}

	public synchronized int release() {
		return (--this.counter);
	}

	public MyLockEntry() {
		this.latch = new Semaphore(0);
	}

	public Semaphore getLatch() {
		return this.latch;
	}

	public void setJedisPubSub(JedisPubSub jedisPubSub) {
		this.jedisPubSub = jedisPubSub;
	}

	public JedisPubSub getJedisPubSub() {
		return this.jedisPubSub;
	}
}
