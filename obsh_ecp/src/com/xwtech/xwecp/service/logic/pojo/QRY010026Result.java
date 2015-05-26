package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY010026Result extends BaseServiceInvocationResult
{
	private String count;

	private String xtable_cdr;

	public void setCount(String count)
	{
		this.count = count;
	}

	public String getCount()
	{
		return this.count;
	}

	public void setXtable_cdr(String xtable_cdr)
	{
		this.xtable_cdr = xtable_cdr;
	}

	public String getXtable_cdr()
	{
		return this.xtable_cdr;
	}

}