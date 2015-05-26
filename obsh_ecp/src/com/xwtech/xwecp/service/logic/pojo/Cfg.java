package com.xwtech.xwecp.service.logic.pojo;

/**
 * 用户信用星级评定明细查询  - 得分规则
 * @author YangXQ
 * 2014-12-12
 */
public class Cfg {
	private long left_value;
	private long right_value;
	private long cc_level;
	
	public long getLeft_value() {
		return left_value;
	}
	public void setLeft_value(long leftValue) {
		left_value = leftValue;
	}
	public long getRight_value() {
		return right_value;
	}
	public void setRight_value(long rightValue) {
		right_value = rightValue;
	}
	public long getCc_level() {
		return cc_level;
	}
	public void setCc_level(long ccLevel) {
		cc_level = ccLevel;
	}
	
}
