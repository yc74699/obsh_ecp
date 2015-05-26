package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040013Result extends BaseServiceInvocationResult
{
	private String shortNum = "";

	private String longNum = "";

	public void setShortNum(String shortNum)
	{
		this.shortNum = shortNum;
	}

	public String getShortNum()
	{
		return this.shortNum;
	}

	public void setLongNum(String longNum)
	{
		this.longNum = longNum;
	}

	public String getLongNum()
	{
		return this.longNum;
	}

}