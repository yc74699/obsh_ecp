package com.xwtech.xwecp.log;


import java.util.Vector;

import org.apache.log4j.Logger;


public class LInterfaceAccessLogQueue extends LogQueue
{
	private static final Logger logger = Logger.getLogger(LInterfaceAccessLogQueue.class);
	
	public void pushLInterfaceAccessLog(LInterfaceAccessLogBean log)
	{
		super.push(log);
	}
	
	public LInterfaceAccessLogBean popLInterfaceAccessLog()
	{
		return (LInterfaceAccessLogBean)super.pop();
	}
}
