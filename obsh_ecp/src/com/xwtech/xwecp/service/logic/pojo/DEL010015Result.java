package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL010015Result extends BaseServiceInvocationResult
{
	private String operatingSrl;

	private String wlanPassword;

	public void setOperatingSrl(String operatingSrl)
	{
		this.operatingSrl = operatingSrl;
	}

	public String getOperatingSrl()
	{
		return this.operatingSrl;
	}

	public void setWlanPassword(String wlanPassword)
	{
		this.wlanPassword = wlanPassword;
	}

	public String getWlanPassword()
	{
		return this.wlanPassword;
	}

}