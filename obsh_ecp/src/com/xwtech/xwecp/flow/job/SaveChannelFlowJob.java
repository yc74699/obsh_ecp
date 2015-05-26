package com.xwtech.xwecp.flow.job;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.xwtech.xwecp.flow.ChannelFlowContainer;
import com.xwtech.xwecp.flow.flowSizeContrl.ChannelFlowInfo;
import com.xwtech.xwecp.flow.flowSizeContrl.ChannelFlowQueue;

public class SaveChannelFlowJob implements Job  
{

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext arg0) throws JobExecutionException 
	{
		List flowInfoLst = ChannelFlowQueue.getInstance().pop();
		
		Map tempContainer = null;
		if(flowInfoLst != null && flowInfoLst.size() > 0)
		{
			tempContainer = new HashMap();
			for(int i = 0; i < flowInfoLst.size(); i ++)
			{
				ChannelFlowInfo flowInfo = (ChannelFlowInfo)flowInfoLst.get(i);
				String channel = flowInfo.getChannel();
				String cmd = flowInfo.getCmd();
				//将具体的接口转化为连接数
				int connNum = getConnNumByCmd(cmd);
				
				if(tempContainer.containsKey(channel))
				{
					tempContainer.put(channel, ((Long)tempContainer.get(channel)).longValue() + connNum);
				}
				else
				{
					tempContainer.put(channel, Long.valueOf(connNum));
				}
			}
		}
		
		if(tempContainer != null && tempContainer.size() > 0)
		{
			//获取存放各渠道流量的容器
			Map flowContainer = ChannelFlowContainer.getInstance().getFlowContainer();
			synchronized(flowContainer)
			{
				for(Iterator iter = tempContainer.keySet().iterator(); iter.hasNext();)
				{
					String channel = (String)iter.next();
					long connNum =  ((Long)tempContainer.get(channel)).longValue();
					if(flowContainer.containsKey(channel))
					{
						flowContainer.put(channel, ((Long)flowContainer.get(channel)).longValue() + connNum);
					}
					else
					{
						flowContainer.put(channel, Long.valueOf(connNum));
					}
				}
			}
		}
	}
	
	/**
	 * 将具体的接口转化为连接数
	 * @param cmd
	 * @return
	 */
	private int getConnNumByCmd(String cmd)
	{
		//读取配置文件或者查询数据库，获取每个接口对应的连接数
		return 1;
	}

}
