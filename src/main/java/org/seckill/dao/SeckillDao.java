package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.seckill.entity.Seckill;

public interface SeckillDao {
	
	/**
	 * 查询单个秒杀
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * 查询全部秒杀
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(int offset, int limit);
	
	/**
	 * 执行秒杀
	 * @param seckillId
	 * @param killTime
	 * @return
	 */
	int reduceNumById(long seckillId, Date killTime);
}
