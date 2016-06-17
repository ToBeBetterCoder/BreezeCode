package org.seckill.enums;

/**
 * 使用枚举表述常量数据字段
 * @author zhoukl
 *
 */
public enum SeckillStateEnum {
	
	SUCCESS(0, "秒杀成功"),
	END(1, "秒杀已结束"),
	REPEAT_KILL(-1, "重复秒杀"),
	INNER_ERROR(-2, "系统异常"),
	DATA_REWRITE(-3, "数据篡改");
	 
	SeckillStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	private int state;
	 
	private String stateInfo;

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}
	
	public static SeckillStateEnum stateOf(int index) {
		for (SeckillStateEnum state : SeckillStateEnum.values()) {
			if (index == state.getState()) {
				return state;
			}
		}
		return null;
	}
}
