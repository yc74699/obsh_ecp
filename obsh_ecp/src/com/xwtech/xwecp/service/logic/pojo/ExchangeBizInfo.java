package com.xwtech.xwecp.service.logic.pojo;


public class ExchangeBizInfo
{
	private String exchangeTypeId;

	private String packageTypeId;

	private String exchangeCode;

	private String exchangeName;

	private int scores;

	public void setExchangeTypeId(String exchangeTypeId)
	{
		this.exchangeTypeId = exchangeTypeId;
	}

	public String getExchangeTypeId()
	{
		return this.exchangeTypeId;
	}

	public void setPackageTypeId(String packageTypeId)
	{
		this.packageTypeId = packageTypeId;
	}

	public String getPackageTypeId()
	{
		return this.packageTypeId;
	}

	public void setExchangeCode(String exchangeCode)
	{
		this.exchangeCode = exchangeCode;
	}

	public String getExchangeCode()
	{
		return this.exchangeCode;
	}

	public void setExchangeName(String exchangeName)
	{
		this.exchangeName = exchangeName;
	}

	public String getExchangeName()
	{
		return this.exchangeName;
	}

	public void setScores(int scores)
	{
		this.scores = scores;
	}

	public int getScores()
	{
		return this.scores;
	}

}