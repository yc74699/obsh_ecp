package com.xwtech.xwecp.log;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.xwtech.xwecp.XWECPApp;


public class LogLauncher
{
	private static final Logger logger = Logger.getLogger(LogLauncher.class);
	
	private ThreadPoolTaskExecutor taskExecutor;
	
	private int liAccessLogThreadNum = 1;
	
	private int bossRequestLogThreadNum = 1;
	
	private String liAccessLogExecutorName = "logicInterfaceAccessLogPersitenter";
	
	private String bossRequestLogExecutorName = "bossRequestLogPersitenter";
	
	private List<Thread> liAccessLogThreads = new ArrayList<Thread>();
	
	private List<Thread> bossAccessLogThreads = new ArrayList<Thread>();
	
	public String getBossRequestLogExecutorName()
	{
		return bossRequestLogExecutorName;
	}

	public void setBossRequestLogExecutorName(String bossRequestLogExecutorName)
	{
		this.bossRequestLogExecutorName = bossRequestLogExecutorName;
	}

	public String getLiAccessLogExecutorName()
	{
		return liAccessLogExecutorName;
	}

	public void setLiAccessLogExecutorName(String liAccessLogExecutorName)
	{
		this.liAccessLogExecutorName = liAccessLogExecutorName;
	}

	public int getBossRequestLogThreadNum()
	{
		return bossRequestLogThreadNum;
	}

	public void setBossRequestLogThreadNum(int bossRequestLogThreadNum)
	{
		this.bossRequestLogThreadNum = bossRequestLogThreadNum;
	}

	public int getLiAccessLogThreadNum()
	{
		return liAccessLogThreadNum;
	}

	public void setLiAccessLogThreadNum(int liAccessLogThreadNum)
	{
		this.liAccessLogThreadNum = liAccessLogThreadNum;
	}

	public ThreadPoolTaskExecutor getTaskExecutor()
	{
		return taskExecutor;
	}
	
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}
	
	public LogQueue launchNewLiAccessLogThread()
	{
		LogPersistExecutor executor = (LogPersistExecutor)(XWECPApp.SPRING_CONTEXT.getBean(this.liAccessLogExecutorName));
		logger.info("启动新逻辑接口访问日志线程: " + executor);
		Thread t = new Thread(executor);
		this.liAccessLogThreads.add(t);
		t.start();
		return executor.getLogQueue();
	}
	
	public void killLiAccessLogThread(Thread t)
	{
		if(t.isAlive())
		{
			logger.error("线程活着,不能杀(会造成死锁)!");
		}
		else
		{
			this.liAccessLogThreads.remove(t);
		}
	}
	
	public LogQueue launchNewBossAccessLogThread()
	{
		LogPersistExecutor executor = (LogPersistExecutor)(XWECPApp.SPRING_CONTEXT.getBean(this.bossRequestLogExecutorName));
		logger.info("启动新BOSS接口访问日志线程: " + executor);
		Thread t = new Thread(executor);
		this.bossAccessLogThreads.add(t);
		t.start();
		return executor.getLogQueue();
	}
	
	public void killBossAccessLogThread(Thread t)
	{
		if(t.isAlive())
		{
			logger.error("线程活着,不能杀(会造成死锁)!");
		}
		else
		{
			this.bossAccessLogThreads.remove(t);
		}
	}

	public void launch()
	{
		logger.info("启动日志服务...");
		
		LogQueue liAccessQueue = null;
		LogQueue bossAccessQueue = null;
		
		if(this.liAccessLogThreadNum <= 0)
		{
			logger.warn("逻辑接口访问日志持久化服务被禁用[liAccessLogThreadNum="+liAccessLogThreadNum+"]!");
		}
		if(this.bossRequestLogThreadNum <= 0)
		{
			logger.warn("BOSS接口访问日志持久化服务被禁用[bossRequestLogThreadNum="+bossRequestLogThreadNum+"]!");
		}
		for(int i = 0;i<liAccessLogThreadNum;i++)
		{
			liAccessQueue = this.launchNewLiAccessLogThread();
		}
		
		for(int i = 0;i<bossRequestLogThreadNum;i++)
		{
			bossAccessQueue = this.launchNewBossAccessLogThread();
		}
		
		if(liAccessQueue != null)
		{
			logger.info("启动逻辑接口日志队列监控线程...");
			LogQueueMonitor liLogQueueMonitor = new LogQueueMonitor();
			liLogQueueMonitor.setInterval(200000);	//20秒检查一次
			liLogQueueMonitor.setLimitSize(5000);	//超过5000个不处理将严重告警
			liLogQueueMonitor.setWarningSize(200);	//超过200个不处理将启动日志线程健康性检查
			liLogQueueMonitor.setQueue(liAccessQueue);
			liLogQueueMonitor.setLogLauncher(this);
			new Thread(liLogQueueMonitor).start();
			logger.info("启动逻辑接口日志队列监控线程成功!");
		}
		
		if(bossAccessQueue != null)
		{
			logger.info("启动BOSS接口日志队列监控线程...");
			LogQueueMonitor bossLogQueueMonitor = new LogQueueMonitor();
			bossLogQueueMonitor.setInterval(200000);	//20秒检查一次
			bossLogQueueMonitor.setLimitSize(5000);	//超过5000个不处理将严重告警
			bossLogQueueMonitor.setWarningSize(200);	//超过200个不处理将启动日志线程健康性检查
			bossLogQueueMonitor.setQueue(bossAccessQueue);
			bossLogQueueMonitor.setLogLauncher(this);
			new Thread(bossLogQueueMonitor).start();
			logger.info("启动BOSS接口日志队列监控线程成功!");
		}
		logger.info("启动日志服务成功!");
	}

	protected List<Thread> getLiAccessLogThreads()
	{
		return liAccessLogThreads;
	}

	protected void setLiAccessLogThreads(List<Thread> liAccessLogThreads)
	{
		this.liAccessLogThreads = liAccessLogThreads;
	}

	protected List<Thread> getBossAccessLogThreads()
	{
		return bossAccessLogThreads;
	}

	protected void setBossAccessLogThreads(List<Thread> bossAccessLogThreads)
	{
		this.bossAccessLogThreads = bossAccessLogThreads;
	}
}
