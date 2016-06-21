package org.seckill.dao;


import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit, spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

	// 注入Dao实现类依赖
	@Resource
	private RedisDao redisDao;
	
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testSeckill() {
		long seckillId = 1000;
		Seckill seckill = redisDao.getSeckill(seckillId);
		if (null == seckill) {
			seckill = seckillDao.queryById(seckillId);
			if (null != seckill) {
				System.out.println(redisDao.putSeckill(seckill));
			}
		}
		System.out.println(seckill);
	}
}
