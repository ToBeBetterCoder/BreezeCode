package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis缓存操作
 * @author zhoukl
 *
 */
public class RedisDao {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final JedisPool jedisPool;

	public RedisDao(String ip, int port) {
		this.jedisPool = new JedisPool(ip, port);
	}
	
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	public Seckill getSeckill(long seckillId) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:" + seckillId;
			// 采用自定义序列化
			byte[] bytes = jedis.get(key.getBytes());
			// 缓存获取
			if (null != bytes) {
				// 空对象
				Seckill seckill = schema.newMessage();
				ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
				// seckill 反列化
				return seckill;
			}
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error(e.getMessage(), e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResource(jedis);
			}
		}
		return null;
	}
	
	public String putSeckill(Seckill seckill) {
		// set Object(Seckill) ->序列化->byte[]
		Jedis jedis = null;
		String result = "";
		try {
			jedis = jedisPool.getResource();
			String key = "seckill:" + seckill.getSeckillId();
			byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			// 超时缓存1h
			int timeout = 60 * 60;
			result = jedis.setex(key.getBytes(), timeout, bytes);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			logger.error(e.getMessage(), e);
		} finally {
			if (null != jedis) {
				jedisPool.returnResource(jedis);
			}
		}
		return result;
	}
	
}
