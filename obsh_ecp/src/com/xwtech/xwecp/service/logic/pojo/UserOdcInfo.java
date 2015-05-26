package com.xwtech.xwecp.service.logic.pojo;


public class UserOdcInfo
{
	private String mobile;

	private String area;

	private String actNum;

	private String actName;

	private String awardNum;

	private String awardName;

	private String awardLevel;

	private String awardEndtime;
	
	private String actPublicKey;

	private int isExchange;

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getMobile()
	{
		return this.mobile;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public String getArea()
	{
		return this.area;
	}

	public void setActNum(String actNum)
	{
		this.actNum = actNum;
	}

	public String getActNum()
	{
		return this.actNum;
	}

	public void setActName(String actName)
	{
		this.actName = actName;
	}

	public String getActName()
	{
		return this.actName;
	}

	public void setAwardNum(String awardNum)
	{
		this.awardNum = awardNum;
	}

	public String getAwardNum()
	{
		return this.awardNum;
	}

	public void setAwardName(String awardName)
	{
		this.awardName = awardName;
	}

	public String getAwardName()
	{
		return this.awardName;
	}

	public void setAwardLevel(String awardLevel)
	{
		this.awardLevel = awardLevel;
	}

	public String getAwardLevel()
	{
		return this.awardLevel;
	}

	public void setAwardEndtime(String awardEndtime)
	{
		this.awardEndtime = awardEndtime;
	}

	public String getAwardEndtime()
	{
		return this.awardEndtime;
	}

	public void setIsExchange(int isExchange)
	{
		this.isExchange = isExchange;
	}

	public int getIsExchange()
	{
		return this.isExchange;
	}

	public String getActPublicKey() {
		return actPublicKey;
	}

	public void setActPublicKey(String actPublicKey) {
		this.actPublicKey = actPublicKey;
	}

}