package com.xwtech.xwecp.service.logic.pojo;


public class ScoreDT
{
	private String userId;

	private String giftScore;

	private String exchangedScore;

	private String leavingsScore;

	private String changeFlag;

	private String retCode;

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUserId()
	{
		return this.userId;
	}

	public void setGiftScore(String giftScore)
	{
		this.giftScore = giftScore;
	}

	public String getGiftScore()
	{
		return this.giftScore;
	}

	public void setExchangedScore(String exchangedScore)
	{
		this.exchangedScore = exchangedScore;
	}

	public String getExchangedScore()
	{
		return this.exchangedScore;
	}

	public void setLeavingsScore(String leavingsScore)
	{
		this.leavingsScore = leavingsScore;
	}

	public String getLeavingsScore()
	{
		return this.leavingsScore;
	}

	public void setChangeFlag(String changeFlag)
	{
		this.changeFlag = changeFlag;
	}

	public String getChangeFlag()
	{
		return this.changeFlag;
	}

	public void setRetCode(String retCode)
	{
		this.retCode = retCode;
	}

	public String getRetCode()
	{
		return this.retCode;
	}

}