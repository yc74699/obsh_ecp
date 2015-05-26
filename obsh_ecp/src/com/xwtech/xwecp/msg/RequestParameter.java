package com.xwtech.xwecp.msg;


import org.apache.log4j.Logger;


public class RequestParameter
{
	private static final Logger logger = Logger.getLogger(RequestParameter.class);
	
	private String parameterName;
	
	private Object parameterValue;
	
	public RequestParameter()
	{
		
	}
	
	public RequestParameter(String name, Object value)
	{
		this.parameterName = name;
		this.parameterValue = value;
	}

	public String getParameterName()
	{
		return parameterName;
	}

	public void setParameterName(String parameterName)
	{
		this.parameterName = parameterName;
	}

	public Object getParameterValue()
	{
		return parameterValue;
	}

	public void setParameterValue(Object parameterValue)
	{
		this.parameterValue = parameterValue;
	}
}
