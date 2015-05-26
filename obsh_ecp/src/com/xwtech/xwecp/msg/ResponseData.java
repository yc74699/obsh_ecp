package com.xwtech.xwecp.msg;


import org.apache.log4j.Logger;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;


public class ResponseData extends MessageData
{
	private static final Logger logger = Logger.getLogger(ResponseData.class);
	
	private BaseServiceInvocationResult serviceResult;

	public BaseServiceInvocationResult getServiceResult()
	{
		return serviceResult;
	}

	public void setServiceResult(BaseServiceInvocationResult serviceResult)
	{
		this.serviceResult = serviceResult;
	}
}
