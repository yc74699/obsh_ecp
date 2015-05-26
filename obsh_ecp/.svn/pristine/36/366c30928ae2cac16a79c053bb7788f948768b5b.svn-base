package com.xwtech.xwecp.log;


import org.apache.log4j.Logger;


public class BossRequestLogQueue extends LogQueue
{
	private static final Logger logger = Logger.getLogger(BossRequestLogQueue.class);
	
	public void pushBossRequestLog(BossRequestLogBean log)
	{
		super.push(log);
	}
	
	public BossRequestLogBean popBossRequestLog()
	{
		return (BossRequestLogBean)super.pop();
	}
}
