package com.xwtech.xwecp.flow.flowSizeContrl;

import java.util.Map;

import com.xwtech.xwecp.flow.ChannelFlowContainer;

public class FlowContrlUtil 
{
	/**
	 * 流量校验
	 * @param channel
	 * @param maxSize
	 * @return
	 */
	public static boolean checkCurrFlowSize(String channel, long maxSize)
	{
		boolean result = false;
		
		//获取存放各渠道流量的容器
		Map flowContainer = ChannelFlowContainer.getInstance().getFlowContainer();
		
		Long flowSize = (Long)flowContainer.get(channel);
		
		if(flowSize == null || flowSize.longValue() < maxSize)
		{
			result = true;
		}
		
		return result;
	}

}
