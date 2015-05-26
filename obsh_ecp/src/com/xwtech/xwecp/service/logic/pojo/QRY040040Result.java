package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040040Result extends BaseServiceInvocationResult
{
	private String leftMoney = "";

	private String invalidDate = "";

	public void setLeftMoney(String leftMoney)
	{
		this.leftMoney = leftMoney;
	}

	public String getLeftMoney()
	{
		return this.leftMoney;
	}

	public void setInvalidDate(String invalidDate)
	{
		this.invalidDate = invalidDate;
	}

	public String getInvalidDate()
	{
		return this.invalidDate;
	}

}