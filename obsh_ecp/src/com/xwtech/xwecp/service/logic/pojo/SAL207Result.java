package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class SAL207Result extends BaseServiceInvocationResult
{
	private String crmorderid;

	public void setCrmorderid(String crmorderid)
	{
		this.crmorderid = crmorderid;
	}

	public String getCrmorderid()
	{
		return this.crmorderid;
	}

}