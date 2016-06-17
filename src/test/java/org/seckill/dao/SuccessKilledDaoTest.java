package org.seckill.dao;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 * @author zhoukl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit, spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

	// 注入Dao实现类依赖
	@Resource
	private SuccessKilledDao successKilledDao;
		
	@Test
	public void testAddSuccessKilled() {
		long seckillId = 1003;
		String userPhone = "18252151060";
		int result = successKilledDao.addSuccessKilled(seckillId, userPhone);
		System.out.println(result);
	}

	@Test
	public void testQueryByIdWithSecKill() {
		long seckillId = 1001;
		String userPhone = "18252151060";
		SuccessKilled successKilled = successKilledDao.queryByIdWithSecKill(seckillId, userPhone);
		System.out.println(successKilled);
	}

}
