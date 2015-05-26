package com.xwtech.xwecp.service;


import org.apache.log4j.Logger;


public class IllegalServiceInputException extends ServiceException
{
	private static final Logger logger = Logger.getLogger(IllegalServiceInputException.class);
	
	public IllegalServiceInputException()
	{
	}
	
	public IllegalServiceInputException(Exception e)
	{
		super(e);
	}
	
	public IllegalServiceInputException(String s)
	{
		super(s);
	}
}
