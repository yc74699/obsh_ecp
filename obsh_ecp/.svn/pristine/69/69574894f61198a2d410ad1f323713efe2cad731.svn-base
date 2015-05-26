package com.xwtech.xwecp.flow.job;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.xwtech.xwecp.flow.ChannelFlowContainer;

public class RemoveChannelFlowJob implements Job  
{
	
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{
		//获取存放各渠道流量的容器
		Map flowContainer = ChannelFlowContainer.getInstance().getFlowContainer();
		synchronized(flowContainer)
		{
			flowContainer.clear();
		}
	}

}
