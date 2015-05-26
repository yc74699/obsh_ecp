package com.xwtech.xwecp.service.logic.pojo;


public class IncrementInfo
{
	private String incrementId;

	private String incrementName;

	private String startDate;

	private String endDate;

	private int state;

	public void setIncrementId(String incrementId)
	{
		this.incrementId = incrementId;
	}

	public String getIncrementId()
	{
		return this.incrementId;
	}

	public void setIncrementName(String incrementName)
	{
		this.incrementName = incrementName;
	}

	public String getIncrementName()
	{
		return this.incrementName;
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

	public void setState(int state)
	{
		this.state = state;
	}

	public int getState()
	{
		return this.state;
	}

}