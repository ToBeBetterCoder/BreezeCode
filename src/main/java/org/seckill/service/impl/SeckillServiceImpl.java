package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
@Service
public class SeckillServiceImpl implements SeckillService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	@Autowired
	private RedisDao redisDao;
	
	private final String salt = "@#$%^&ERTYHUJxcvbn3456";
	
	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 10);
	}

	@Override
	public Seckill getSeckillById(long seckillId) {
		Seckill seckill = redisDao.getSeckill(seckillId);
		if (null == seckill) {
			seckill = seckillDao.queryById(seckillId);
			if (null != seckill) {
				redisDao.putSeckill(seckill);
			}
		}
		return seckill;
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill = getSeckillById(seckillId);
		if (null == seckill) {
			return new Exposer(false, seckillId);
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date now = new Date();
		if (now.getTime() < startTime.getTime() || 
				now.getTime() > endTime.getTime()) {
			return new Exposer(false, now.getTime(), startTime.getTime(), endTime.getTime(), seckillId);
		}
		return new Exposer(true, getMD5(seckillId), seckillId);
	}

	/**
	 * 使用注解控制事务方法的优点：
	 * 1：开发团队达成一致约定，明确标注事务方法的编程风格。
	 * 2：保证事务方法的执行时间尽可能短，不要穿插其它网络操作RPC/HTTP请求或者剥离到事务方法外部。
	 * 3:不是所有的方法都需要事务，如果只有一条修改操作，只读操作不需要事务控制。
	 */
	@Override
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, String userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {
		SuccessKilled successKilled = null;
		if (null == md5 || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("md5不一致");
		}
		try {
			// 记录秒杀记录
			int insertCount = successKilledDao.addSuccessKilled(seckillId, userPhone);
			if (0 >= insertCount) {
				throw new RepeatKillException("重复秒杀");
			}
			// 减库存
			int updateCount = seckillDao.reduceNumById(seckillId, new Date());
			if (0 >= updateCount) {
				// 库存没了或者秒杀时间不符
				throw new SeckillCloseException("秒杀已结束");
			}
			successKilled = successKilledDao.queryByIdWithSecKill(seckillId, userPhone);
		} catch (SeckillCloseException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (RepeatKillException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (SeckillException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 所有编译期异常转化为运行期异常
			throw new SeckillException(e.getMessage(), e);
		}
		return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
	}
	
	/**
	 * 获取MD5
	 * @param seckillId
	 * @return
	 */
	private String getMD5(long seckillId) {
		String base = seckillId + "/" + salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Override
	public SeckillExecution executeSeckillProcedure(long seckillId, String userPhone, String md5) {
		if (null == md5 || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
		}
		SuccessKilled successKilled = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("seckillId", seckillId);
		paramMap.put("userPhone", userPhone);
		paramMap.put("killTime", new Date());
		paramMap.put("result", null); //执行存储过程，result被赋值
		try {
			successKilledDao.killByProcedure(paramMap);
			int result = MapUtils.getInteger(paramMap, "result", -2);
			if (result == 0) {
				successKilled = successKilledDao.queryByIdWithSecKill(seckillId, userPhone);
			} else {
				return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
		}
		return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
	};
}
