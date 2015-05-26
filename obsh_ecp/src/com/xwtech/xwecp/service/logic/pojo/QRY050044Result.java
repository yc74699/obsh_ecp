package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY050044Result extends BaseServiceInvocationResult
{
	private String tempNo = "";

	private String tempName = "";

	public void setTempNo(String tempNo)
	{
		this.tempNo = tempNo;
	}

	public String getTempNo()
	{
		return this.tempNo;
	}

	public void setTempName(String tempName)
	{
		this.tempName = tempName;
	}

	public String getTempName()
	{
		return this.tempName;
	}

}