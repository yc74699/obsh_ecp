package com.xwtech.xwecp.service.logic.pojo;


public class ChargeInfo
{
	private String spShortName;

	private String serviceCode;

	private String bizName;

	private long realFee;

	public void setSpShortName(String spShortName)
	{
		this.spShortName = spShortName;
	}

	public String getSpShortName()
	{
		return this.spShortName;
	}

	public void setServiceCode(String serviceCode)
	{
		this.serviceCode = serviceCode;
	}

	public String getServiceCode()
	{
		return this.serviceCode;
	}

	public void setBizName(String bizName)
	{
		this.bizName = bizName;
	}

	public String getBizName()
	{
		return this.bizName;
	}

	public void setRealFee(long realFee)
	{
		this.realFee = realFee;
	}

	public long getRealFee()
	{
		return this.realFee;
	}

}