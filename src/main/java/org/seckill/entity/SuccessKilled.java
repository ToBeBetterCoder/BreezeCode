package org.seckill.entity;

import java.util.Date;

/**
 * 
 * @author zhoukl
 * 秒杀成功
 */
public class SuccessKilled {
	
	private long seckillId;
	
	private String phoneNum;
	
	private short state;
	
	private Date createTime;
	
	// 多对一
	private Seckill seckill;

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Seckill getSeckill() {
		return seckill;
	}

	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	@Override
	public String toString() {
		return "SuccessKilled [seckillId=" + seckillId + ", phoneNum="
				+ phoneNum + ", state=" + state + ", createTime=" + createTime
				+ ", seckill=" + seckill + "]";
	}
}
