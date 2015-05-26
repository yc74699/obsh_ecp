package com.xwtech.xwecp.service.logic.pojo;


public class ZoneMValueInfo
{
	private String totalMValue;

	private long redeemAmount;

	private long usedMvalue;

	private long randowPwd;

	public void setTotalMValue(String totalMValue)
	{
		this.totalMValue = totalMValue;
	}

	public String getTotalMValue()
	{
		return this.totalMValue;
	}

	public void setRedeemAmount(long redeemAmount)
	{
		this.redeemAmount = redeemAmount;
	}

	public long getRedeemAmount()
	{
		return this.redeemAmount;
	}

	public void setUsedMvalue(long usedMvalue)
	{
		this.usedMvalue = usedMvalue;
	}

	public long getUsedMvalue()
	{
		return this.usedMvalue;
	}

	public void setRandowPwd(long randowPwd)
	{
		this.randowPwd = randowPwd;
	}

	public long getRandowPwd()
	{
		return this.randowPwd;
	}

}