package com.test.redis;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;


public class RedisLock {
	private static Logger logger=LoggerFactory.getLogger(RedisLock.class);
	private RedisTemplate redisTemplate;
	private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS =100;
	private String lockKey;
	private int expireMsecs=60*1000;
	private static final Jedis jedis=RedisConection.getJedis();
	/** 加锁最大等待重试次数   **/
	private static final int RETRY_COUNT = 100;

	/** (默认)加锁重试等待间隔时间(单位:毫秒ms) **/
	private static final int REST_TIME = 500;

	/**
	 * 获取缓存的key值(防止用户重复提交)
	 * @param prefix 前缀
	 * @param userId 用户Id
	 * @return prefix + userId
	 */
	public String getRepeatingRequestKey(String prefix, String userId) {
		return prefix + userId;
	}

	/**
	 * 检查用户是否是重复请求
	 * @param prefix 前缀 
	 * @param userId 用户Id
	 * @return ture:是;false:不是
	 */
	public boolean checkRepeatingRequest(String prefix, String userId) {
		String key = getRepeatingRequestKey(prefix, userId);
		long status = jedis.incr(key);
		jedis.expire(key, 120); // 缓存2分钟
		if (status > 1) {
			return true;
		}
		return false;
	}

	/**
	 * 删除重复请求状态
	 * @param prefix 前缀 
	 * @param userId 用户Id
	 */
	public void removeRepeatingRequest(String prefix, String userId) {
		remove(prefix, userId);
	}

	/**
	 * 加锁(执行防止出现重复提交)
	 * @param prefix 前缀 
	 * @param userId 用户Id
	 * @param resttime 加锁重试等待间隔时间(单位:毫秒ms)
	 * @return true：不是重复提交，操作结束后需要解锁
	 * @throws Exception 是重复提交，且加锁，等待时间或等待循环次数超过限制
	 */
	public boolean lock(String prefix, String userId, int restTime) throws Exception {

		long start = System.currentTimeMillis();
		int count = 0;
		if (restTime == 0) {
			restTime = REST_TIME;
		}
		try {
			String key = getRepeatingRequestKey(prefix, userId);
			long status = 0;
			while (count < RETRY_COUNT) { // 最大重试100次，超过则抛出异常
				status = jedis.incr(key);
				jedis.expire(key, 120); // 缓存2分钟
				if (status > 1) { // 出现重复提交，等待1s后再次重试
					count++;
					// 休息一秒
					Thread.sleep(restTime);

				} else { // 正常(非重复)
					return true;
				}
			}
			logger.error("[userId=" + userId + "]==>已超过最大重试次数[" + RETRY_COUNT + "]抛出异常==>[status=" + status + "][count=" + count + "]，使用消息重试机制");
			throw new Exception("[userId=" + userId + "]==>已超过最大重试次数[" + RETRY_COUNT + "]抛出异常==>[status=" + status + "][count=" + count + "]，使用消息重试机制");

		} catch (Exception ex) {
			logger.error("[userId=" + userId + "]==>防止重复提交加锁时出现异常.", ex);
			throw ex;
		} finally {
			long executeTime = System.currentTimeMillis() - start;
			logger.info("会员成长值防重复提交(lock)==>[userId=" + userId + "]加锁时间:[" + executeTime + "ms].循环次数:[" + count + "]");
		}
	}

	/**
	 * 解锁
	 * @param prefix 前缀 
	 * @param userId 用户Id
	 * 
	 */
	public void unLock(String prefix, String userId) {
		remove(prefix, userId);
	}

	/**
	 * 删除重复请求状态
	 * @param prefix 前缀 
	 * @param userId 用户Id
	 */
	private void remove(String prefix, String userId) {
		if (StringUtils.isNotEmpty(userId)) {
			String key = getRepeatingRequestKey(prefix, userId);
			jedis.del(key);
		}
	}
	
	
	
}
