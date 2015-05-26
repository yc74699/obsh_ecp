package com.xwtech.xwecp.msg;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class RequestData extends MessageData
{
	private static final Logger logger = Logger.getLogger(RequestData.class);
	
	private List<RequestParameter> params = new ArrayList<RequestParameter>();
	
	private InvocationContext context;
	
	public InvocationContext getContext()
	{
		return context;
	}

	public void setContext(InvocationContext context)
	{
		this.context = context;
	}

	public List<RequestParameter> getParams()
	{
		return params;
	}

	public void setParams(List<RequestParameter> params)
	{
		this.params = params;
	}
}
