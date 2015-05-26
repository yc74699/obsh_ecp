package com.xwtech.xwecp.service.logic.pojo;


public class MExchange
{
	private String exchangeTime;

	private String Channel;

	private String exchangeDesc;

	private String type;

	public void setExchangeTime(String exchangeTime)
	{
		this.exchangeTime = exchangeTime;
	}

	public String getExchangeTime()
	{
		return this.exchangeTime;
	}

	public void setChannel(String Channel)
	{
		this.Channel = Channel;
	}

	public String getChannel()
	{
		return this.Channel;
	}

	public void setExchangeDesc(String exchangeDesc)
	{
		this.exchangeDesc = exchangeDesc;
	}

	public String getExchangeDesc()
	{
		return this.exchangeDesc;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return this.type;
	}

}