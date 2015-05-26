package com.xwtech.xwecp.log;


import java.util.Vector;

import org.apache.log4j.Logger;


public class LogQueue
{
	private static final Logger logger = Logger.getLogger(LogQueue.class);
	
	protected Vector<LogBean> logQueue = new Vector<LogBean>();
	
	public void push(LogBean lb)
	{
		logQueue.add(lb);
	}
	
	public int remaining()
	{
		return logQueue.size();
	}
	
	public synchronized LogBean pop()
	{
		if(logQueue.size() > 0)
		{
			return logQueue.remove(0);
		}
		return null;
	}
}
