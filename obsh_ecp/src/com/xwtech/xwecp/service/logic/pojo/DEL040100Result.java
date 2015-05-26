package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class DEL040100Result extends BaseServiceInvocationResult
{
	private static final long serialVersionUID = 1L;

	private String cltSrl;

	private String plain;
	
	private String signature;
	
	private String payUrl;
	
	public String getCltSrl()
	{
		return cltSrl;
	}

	
	public void setCltSrl(String cltSrl)
	{
		this.cltSrl = cltSrl;
	}

	public String getPlain()
	{
		return plain;
	}

	public void setPlain(String plain)
	{
		this.plain = plain;
	}


	public String getSignature()
	{
		return signature;
	}

	
	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	
	public String getPayUrl()
	{
		return payUrl;
	}

	public void setPayUrl(String payUrl)
	{
		this.payUrl = payUrl;
	}

}