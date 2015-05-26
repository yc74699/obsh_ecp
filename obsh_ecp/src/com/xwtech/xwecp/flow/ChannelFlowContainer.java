package com.xwtech.xwecp.flow;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 渠道流量存储容器-单例
 *
 */
@SuppressWarnings("unchecked")
public class ChannelFlowContainer 
{
	public static ChannelFlowContainer self = new ChannelFlowContainer();
	
	private Map flowContainer;
	
	private ChannelFlowContainer()
	{
		flowContainer = new HashMap();
	}
	
	public static ChannelFlowContainer getInstance()
	{
		return self;
	}

	public Map getFlowContainer() {
		return flowContainer;
	}

	public void setFlowContainer(Map flowContainer) {
		this.flowContainer = flowContainer;
	}

}
