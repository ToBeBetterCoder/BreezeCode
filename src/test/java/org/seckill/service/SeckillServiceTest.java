package org.seckill.service;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit, spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@Test
	public void testGetSeckillList() {
		logger.info("list={}", seckillService.getSeckillList());
	}

	@Test
	public void testGetSeckillById() {
		System.out.println(seckillService.getSeckillById(1000));
	}

	@Test
	public void testExportSeckillUrl() {
		System.out.println(seckillService.exportSeckillUrl(1001));
	}

	@Test
	public void testExecuteSeckill() {
		Exposer exposer = seckillService.exportSeckillUrl(1001);
		if (exposer.isExposed()) {
			SeckillExecution seckillExecution = seckillService.executeSeckill(exposer.getSeckillId(), "18106029521", exposer.getMd5());
			System.out.println(seckillExecution);
		} else {
			System.out.println(exposer);
		}
		
	}

}
