package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL100005Result extends BaseServiceInvocationResult
{
	private String recoid;
	//新加营销案返回值
	private String newPrivId;//二级营销案编码
	private String newPrivName;//二级营销案名称
	private String newPrivStartDate;//开始时间
	private String newPrivEndDate;//开始时间
	public void setRecoid(String recoid)
	{
		this.recoid = recoid;
	}

	public String getRecoid()
	{
		return this.recoid;
	}

	
	public String getNewPrivId()
	{
		return newPrivId;
	}

	
	public void setNewPrivId(String newPrivId)
	{
		this.newPrivId = newPrivId;
	}

	
	public String getNewPrivName()
	{
		return newPrivName;
	}

	
	public void setNewPrivName(String newPrivName)
	{
		this.newPrivName = newPrivName;
	}

	
	public String getNewPrivStartDate()
	{
		return newPrivStartDate;
	}

	
	public void setNewPrivStartDate(String newPrivStartDate)
	{
		this.newPrivStartDate = newPrivStartDate;
	}

	
	public String getNewPrivEndDate()
	{
		return newPrivEndDate;
	}

	
	public void setNewPrivEndDate(String newPrivEndDate)
	{
		this.newPrivEndDate = newPrivEndDate;
	}

}