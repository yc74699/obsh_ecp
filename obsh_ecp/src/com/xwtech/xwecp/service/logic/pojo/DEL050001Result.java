package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL050001Result extends BaseServiceInvocationResult
{
	private String retcode;

	private String retmsg;

	public void setRetcode(String retcode)
	{
		this.retcode = retcode;
	}

	public String getRetcode()
	{
		return this.retcode;
	}

	public void setRetmsg(String retmsg)
	{
		this.retmsg = retmsg;
	}

	public String getRetmsg()
	{
		return this.retmsg;
	}

}