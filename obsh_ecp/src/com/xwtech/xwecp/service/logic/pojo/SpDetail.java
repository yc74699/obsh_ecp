package com.xwtech.xwecp.service.logic.pojo;


public class SpDetail
{
	private String spName;

	private String busCode;

	private String busName;

	private Long fee;

	private String feeType;

	private String svcCode;

	private String useType;

	private Long spType;

	public void setSpName(String spName)
	{
		this.spName = spName;
	}

	public String getSpName()
	{
		return this.spName;
	}

	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}

	public String getBusCode()
	{
		return this.busCode;
	}

	public void setBusName(String busName)
	{
		this.busName = busName;
	}

	public String getBusName()
	{
		return this.busName;
	}

	public void setFee(Long fee)
	{
		this.fee = fee;
	}

	public Long getFee()
	{
		return this.fee;
	}

	public void setFeeType(String feeType)
	{
		this.feeType = feeType;
	}

	public String getFeeType()
	{
		return this.feeType;
	}

	public void setSvcCode(String svcCode)
	{
		this.svcCode = svcCode;
	}

	public String getSvcCode()
	{
		return this.svcCode;
	}

	public void setUseType(String useType)
	{
		this.useType = useType;
	}

	public String getUseType()
	{
		return this.useType;
	}

	public void setSpType(Long spType)
	{
		this.spType = spType;
	}

	public Long getSpType()
	{
		return this.spType;
	}

}