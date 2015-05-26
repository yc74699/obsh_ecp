package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040008Result extends BaseServiceInvocationResult
{
	private String phoneNum = "";

	private String shortNum = "";

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}

	public String getPhoneNum()
	{
		return this.phoneNum;
	}

	public void setShortNum(String shortNum)
	{
		this.shortNum = shortNum;
	}

	public String getShortNum()
	{
		return this.shortNum;
	}

}