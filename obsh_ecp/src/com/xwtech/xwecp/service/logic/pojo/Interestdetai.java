package com.xwtech.xwecp.service.logic.pojo;

/**
 * 新增用户余额利息明细查询 
 * @author YangXQ
 * 2014-7-15
 */
public class Interestdetai
{
	private String USAGE_BALANCE = null;	//积分 
	private String monthday = null;	//有效期 
	private String dayinterestvalue = null;	//转增积分
	public String getUSAGE_BALANCE() {
		return USAGE_BALANCE;
	}
	public void setUSAGE_BALANCE(String uSAGEBALANCE) {
		USAGE_BALANCE = uSAGEBALANCE;
	}
	public String getMonthday() {
		return monthday;
	}
	public void setMonthday(String monthday) {
		this.monthday = monthday;
	}
	public String getDayinterestvalue() {
		return dayinterestvalue;
	}
	public void setDayinterestvalue(String dayinterestvalue) {
		this.dayinterestvalue = dayinterestvalue;
	}
	
}