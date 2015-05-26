package com.xwtech.xwecp.service.logic.pojo;


public class AlreadyExperienceBiz
{
	private String bizId;

	private String bizName;

	private String startDate;

	private String endDate;

	public void setBizId(String bizId)
	{
		this.bizId = bizId;
	}

	public String getBizId()
	{
		return this.bizId;
	}

	public void setBizName(String bizName)
	{
		this.bizName = bizName;
	}

	public String getBizName()
	{
		return this.bizName;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getStartDate()
	{
		return this.startDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public String getEndDate()
	{
		return this.endDate;
	}

}