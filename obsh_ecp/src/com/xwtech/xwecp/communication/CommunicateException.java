package com.xwtech.xwecp.communication;

import org.apache.log4j.Logger;

public class CommunicateException extends Exception
{
	private static final Logger logger = Logger.getLogger(CommunicateException.class);

	public CommunicateException()
	{
	}

	public CommunicateException(Exception e)
	{
		super(e);
	}

	public CommunicateException(String s)
	{
		super(s);
	}
}
