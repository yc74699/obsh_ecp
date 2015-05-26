package com.xwtech.xwecp.service.logic.pojo;


public class ScoreExchange
{
	private String exchangeTime;

	private String exchangeType;

	private long exchangeScore;

	private long exchangeFee;

	private String exchangeChannel;

	public void setExchangeTime(String exchangeTime)
	{
		this.exchangeTime = exchangeTime;
	}

	public String getExchangeTime()
	{
		return this.exchangeTime;
	}

	public void setExchangeType(String exchangeType)
	{
		this.exchangeType = exchangeType;
	}

	public String getExchangeType()
	{
		return this.exchangeType;
	}

	public void setExchangeScore(long exchangeScore)
	{
		this.exchangeScore = exchangeScore;
	}

	public long getExchangeScore()
	{
		return this.exchangeScore;
	}

	public void setExchangeFee(long exchangeFee)
	{
		this.exchangeFee = exchangeFee;
	}

	public long getExchangeFee()
	{
		return this.exchangeFee;
	}

	public void setExchangeChannel(String exchangeChannel)
	{
		this.exchangeChannel = exchangeChannel;
	}

	public String getExchangeChannel()
	{
		return this.exchangeChannel;
	}

}