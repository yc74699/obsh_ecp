package com.xwtech.xwecp.log;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class LogPersistExecutor implements Runnable
{
	private static final Logger logger = Logger.getLogger(LogPersistExecutor.class);
	
	private boolean directToDatabase;
	
	private LogQueue logQueue;
	
	private LogDAOImpl logDAO;	
	
	private int batchSize = 10;
	
	public LogDAOImpl getLogDAO()
	{
		return logDAO;
	}
	
	public void setLogDAO(LogDAOImpl logDAO)
	{
		this.logDAO = logDAO;
	}

	public boolean isDirectToDatabase()
	{
		return directToDatabase;
	}

	public void setDirectToDatabase(boolean directToDatabase)
	{
		this.directToDatabase = directToDatabase;
	}

	public LogQueue getLogQueue()
	{
		return logQueue;
	}

	public void setLogQueue(LogQueue logQueue)
	{
		this.logQueue = logQueue;
	}

	public int getBatchSize()
	{
		return batchSize;
	}

	public void setBatchSize(int batchSize)
	{
		this.batchSize = batchSize;
	}

	public void run()
	{
		boolean flag = true;
		while(flag)
		{
			try
			{
				int nRemaining = 0;
				List<LogBean> list = new ArrayList<LogBean>();
				synchronized(this.logQueue)
				{
					nRemaining = this.logQueue.remaining();
					for(int i = 0;i<this.batchSize;i++)
					{
						LogBean obj = this.logQueue.pop();
						if(obj == null)
						{
							break;
						}
						else
						{
							list.add(obj);
						}
					}
				}
				if(nRemaining > 0)
				{
					logger.info("日志队列大小: " + nRemaining);
					this.doPersist(list);
				}
				else
				{
					synchronized(this)
					{
						try
						{
							this.wait(100);
						}
						catch (InterruptedException e)
						{
							logger.error("日志持久化线程挂掉了!");
							logger.error(e, e);
							break;
						}
					}
				}
			}
			catch(Throwable e)
			{
				logger.error(e, e);
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e1)
				{
					logger.error(e1, e1);
				}
			}
		}
		logger.error("日志线程已死,有事烧纸!");
	}
	
	protected void doPersist(List<LogBean> li)
	{
		//循环处理了， 不用batch处理了
		for(int i = 0;i<li.size();i++)
		{
			String type = li.get(i).getType();
			if(LogBean.LI_ACCESS_LOG.equals(type))
			{
				LInterfaceAccessLogBean bean = (LInterfaceAccessLogBean)(li.get(i));
				if(!this.logDAO.insertLInterfaceAccessLog(bean))
				{
					LInterfaceAccessLogger.writeLInterfaceAccessLog(bean);
				}
			}
			else
			{
				BossRequestLogBean bean = (BossRequestLogBean)(li.get(i));
				if(!this.logDAO.insertBossRequestAccessLog(bean))
				{
					BossRequestLogger.writeBossRequestLog(bean);
				}
			}
		}
	}
}
