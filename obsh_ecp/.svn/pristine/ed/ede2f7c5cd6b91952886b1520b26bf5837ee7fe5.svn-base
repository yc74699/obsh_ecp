package com.xwtech.xwecp.flow.works.chains;

import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.service.ServiceExecutor;

/**
 * 控制接口
 * 
 * @author maofw
 * 
 */
public interface IFlowControl
{
	// 设置下一个节点内容
	public IFlowControl setNext(IFlowControl flowControl);

	// 模板方法
	public ServiceMessage run(ServiceExecutor serviceExecutor, MessageHelper messageHelper, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo);
}
