package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author zhoukl
 *
 */
@Controller
@RequestMapping("/seckill") // url:/模块/资源/{id}/细分  /seckill/list
public class SeckillController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public String list(Model model) {
		// 获取列表页
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		return "list";
	}
	
	@RequestMapping(value="/{seckillId}/detail", method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (null == seckillId) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getSeckillById(seckillId);
		if (null == seckill) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}
	
	@RequestMapping(value="/{seckillId}/exposer", method = RequestMethod.GET, produces = {"application/json; character=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId")Long seckillId) {
		try {
			Exposer exposer = seckillService.exportSeckillUrl(seckillId);
			return new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillResult<Exposer>(true, e.getMessage());
		}
	}
	
	@RequestMapping(value="/{seckillId}/{md5}/execute", method = RequestMethod.POST, produces = {"application/json; character=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId, @PathVariable("md5")String md5, @CookieValue(value="killPhone", required= false)String killPhone) {
		if (null == killPhone || killPhone.trim().isEmpty()) {
			return new SeckillResult<SeckillExecution>(false, "用户号码为空");
		}
		try {
			SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, killPhone, md5);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(SeckillCloseException e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(RepeatKillException e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		} catch(Exception e) {
			SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, seckillExecution);
		}
	}
	
	@RequestMapping(value="/time/now", method = RequestMethod.GET, produces = {"application/json; character=UTF-8"})
	@ResponseBody
	public SeckillResult<Long> time() {
		return new SeckillResult<Long>(true, new Date().getTime());
	}
	
}
