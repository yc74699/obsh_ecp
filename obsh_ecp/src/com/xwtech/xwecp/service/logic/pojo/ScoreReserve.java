package com.xwtech.xwecp.service.logic.pojo;


public class ScoreReserve
{
	private String exchangeTime;

	private String product;

	private long exchangeScore;

	private String exchangeState;

	private String sendTime;

	private String num;

	public void setExchangeTime(String exchangeTime)
	{
		this.exchangeTime = exchangeTime;
	}

	public String getExchangeTime()
	{
		return this.exchangeTime;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}

	public String getProduct()
	{
		return this.product;
	}

	public void setExchangeScore(long exchangeScore)
	{
		this.exchangeScore = exchangeScore;
	}

	public long getExchangeScore()
	{
		return this.exchangeScore;
	}

	public void setExchangeState(String exchangeState)
	{
		this.exchangeState = exchangeState;
	}

	public String getExchangeState()
	{
		return this.exchangeState;
	}

	public void setSendTime(String sendTime)
	{
		this.sendTime = sendTime;
	}

	public String getSendTime()
	{
		return this.sendTime;
	}

	public void setNum(String num)
	{
		this.num = num;
	}

	public String getNum()
	{
		return this.num;
	}

}