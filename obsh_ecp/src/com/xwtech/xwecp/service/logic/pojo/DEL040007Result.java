package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL040007Result extends BaseServiceInvocationResult
{
	private String bossDate = "";

	private String unionPaySrl = "";

	public void setBossDate(String bossDate)
	{
		this.bossDate = bossDate;
	}

	public String getBossDate()
	{
		return this.bossDate;
	}

	public void setUnionPaySrl(String unionPaySrl)
	{
		this.unionPaySrl = unionPaySrl;
	}

	public String getUnionPaySrl()
	{
		return this.unionPaySrl;
	}

}