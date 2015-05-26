package com.xwtech.xwecp.service.logic.pojo;


public class SelfDonate
{
	private String prdCode;
	
	private String bossImp;
	
	private String pinyin;
	
	private String donateUserId;

	private String beginDate;

	private String endDate;

	public void setPrdCode(String prdCode)
	{
		this.prdCode = prdCode;
	}

	public String getPrdCode()
	{
		return this.prdCode;
	}

	public void setDonateUserId(String donateUserId)
	{
		this.donateUserId = donateUserId;
	}

	public String getDonateUserId()
	{
		return this.donateUserId;
	}

	public void setBeginDate(String beginDate)
	{
		this.beginDate = beginDate;
	}

	public String getBeginDate()
	{
		return this.beginDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public String getEndDate()
	{
		return this.endDate;
	}

	public String getBossImp() {
		return bossImp;
	}

	public void setBossImp(String bossImp) {
		this.bossImp = bossImp;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
}