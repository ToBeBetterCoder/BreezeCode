package org.seckill.dao;


import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	
	/**
	 * 秒杀成功，记录秒杀信息
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	int addSuccessKilled(long seckillId, String userPhone);
	
	/**
	 * 查询秒杀成功信息
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	SuccessKilled queryByIdWithSecKill(long seckillId, String userPhone);
	
}
