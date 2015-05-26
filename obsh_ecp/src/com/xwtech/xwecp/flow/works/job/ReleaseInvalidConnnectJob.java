package com.xwtech.xwecp.flow.works.job;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.xwtech.xwecp.flow.ChannelFlowContainer;

/**
 * 移除已经超时的无效链接
 * 
 * @author maofw
 * 
 */
public class ReleaseInvalidConnnectJob implements Job
{
	// 超时时间 1 分钟
	private static final long timeout = 60 * 1000L;

	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		// 获取存放各渠道流量的容器
		Map flowContainer = ChannelFlowContainer.getInstance().getFlowContainer();

		// System.out.println("start:" + flowContainer.toString());

		if ( flowContainer != null )
		{
			// 获得当前时间
			long currentTime = System.currentTimeMillis();

			String tmpStr = null;
			// 校验每一个内容是否超时
			Iterator<String> keys = flowContainer.keySet().iterator();
			while (keys.hasNext())
			{
				// 获得渠道链接List
				List<String> list = (List<String>) flowContainer.get(keys.next());
				if ( list != null && list.size() > 0 )
				{
					Iterator<String> iterator = list.iterator();
					while (iterator.hasNext())
					{
						tmpStr = iterator.next();
						// 获得时间间隔
						int dex = tmpStr.indexOf('_');
						if ( dex >= 0 )
						{
							tmpStr = tmpStr.substring(dex + 1);
							try
							{
								long dis = currentTime - Long.parseLong(tmpStr);
								if ( dis >= timeout )
								{
									// 超时
									iterator.remove();
								}
							}
							catch (Exception e)
							{

							}
						}
						else
						{
							break;
						}
					}
				}
			}
		}
		// System.out.println("----end:" + flowContainer.toString());
	}
}
