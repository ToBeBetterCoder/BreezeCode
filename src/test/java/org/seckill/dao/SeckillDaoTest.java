package org.seckill.dao;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
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
public class SeckillDaoTest {

	// 注入Dao实现类依赖
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById() {
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}

	@Test
	public void testQueryAll() {
		List<Seckill> seckills = seckillDao.queryAll(0, 10);
		System.out.println(seckills);
	}

	@Test
	public void testReduceNumById() {
		long id = 1000;
		Calendar c1 = Calendar.getInstance();  
//		Calendar c3 = new GregorianCalendar(2008, 8, 8, 18, 10, 5);   
		int result = seckillDao.reduceNumById(id, c1.getTime());
		System.out.println(result);
	}

}
