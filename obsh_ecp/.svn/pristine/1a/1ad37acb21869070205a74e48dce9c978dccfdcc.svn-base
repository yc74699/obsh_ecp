package com.xwtech.xwecp.log;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class LogQueueMonitor implements Runnable
{
	private static final Logger	logger	= Logger.getLogger(LogQueueMonitor.class);
	
	private LogQueue queue;
	
	private long interval = 20000;
	
	private int warningSize = 1000;
	
	private int limitSize = 5000;
	
	private LogLauncher logLauncher;
	
	private String type = "LI";		//LI or BOSS
	
	
	
	protected LogQueue getQueue()
	{
		return queue;
	}

	protected void setQueue(LogQueue queue)
	{
		this.queue = queue;
	}

	protected long getInterval()
	{
		return interval;
	}

	protected void setInterval(long interval)
	{
		this.interval = interval;
	}

	protected int getWarningSize()
	{
		return warningSize;
	}

	protected void setWarningSize(int warningSize)
	{
		this.warningSize = warningSize;
	}

	protected int getLimitSize()
	{
		return limitSize;
	}

	protected void setLimitSize(int limitSize)
	{
		this.limitSize = limitSize;
	}

	protected LogLauncher getLogLauncher()
	{
		return logLauncher;
	}

	protected void setLogLauncher(LogLauncher logLauncher)
	{
		this.logLauncher = logLauncher;
	}

	protected String getType()
	{
		return type;
	}

	protected void setType(String type)
	{
		this.type = type;
	}

	public void run()
	{
		while(true)
		{
			if(queue != null)
			{
				int size = queue.remaining();
				if(size >= limitSize)
				{
					logger.warn("日志队列["+queue!=null?queue.getClass().getName():"null"+"]超过极限值["+limitSize+"]!!!("+size+")!");
					//TODO 
				}
				else if(size >= warningSize)
				{
					logger.warn("日志队列["+queue!=null?queue.getClass().getName():"null"+"]超过警戒值["+warningSize+"]("+size+")!");
					if("LI".equals(this.type))
					{
						this.checkLiAccessLogThreads();
					}
					else if("BOSS".equals(this.type))
					{
						this.checkBossAccessLogThreads();
					}
				}
			}
			else
			{
				logger.warn("invalid log queue!");
			}
			synchronized(this)
			{
				try
				{
					this.wait(interval);
				}
				catch (InterruptedException e)
				{
					logger.error("日志队列["+queue!=null?queue.getClass().getName():"null"+"]监控线程挂掉了!");
					logger.error(e, e);
					break;
				}
			}
		}
	}
	
	private void checkLiAccessLogThreads()
	{
		List<Thread> threads = this.logLauncher.getLiAccessLogThreads();
		for(int i = 0;i<threads.size();i++)
		{
			if(!threads.get(i).isAlive())
			{
				this.logLauncher.killLiAccessLogThread(threads.get(i));
				this.logLauncher.launchNewLiAccessLogThread();
				i--;
			}
		}
	}
	
	private void checkBossAccessLogThreads()
	{
		List<Thread> threads = this.logLauncher.getBossAccessLogThreads();
		for(int i = 0;i<threads.size();i++)
		{
			if(!threads.get(i).isAlive())
			{
				this.logLauncher.killBossAccessLogThread(threads.get(i));
				this.logLauncher.launchNewBossAccessLogThread();
				i--;
			}
		}
	}
}
