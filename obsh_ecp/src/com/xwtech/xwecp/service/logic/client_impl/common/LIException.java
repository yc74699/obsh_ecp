package com.xwtech.xwecp.service.logic.client_impl.common;

import org.apache.log4j.Logger;


public class LIException extends Exception
{
	private static final Logger logger = Logger.getLogger(LIException.class);
	
	public LIException()
	{		
	}
	
	public LIException(Exception e)
	{
		super(e);
	}
	
	public LIException(String s)
	{
		super(s);
	}
}
