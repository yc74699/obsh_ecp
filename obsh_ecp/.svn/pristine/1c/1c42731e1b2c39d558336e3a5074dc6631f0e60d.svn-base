package com.xwtech.xwecp.flow.works;

import com.xwtech.xwecp.flow.works.chains.IFlowControl;
import com.xwtech.xwecp.flow.works.chains.nodes.CallServiceNode;
import com.xwtech.xwecp.flow.works.chains.nodes.CheckAccessPrivilegeNode;
import com.xwtech.xwecp.flow.works.chains.nodes.CheckChannelFlowNode;
import com.xwtech.xwecp.flow.works.chains.nodes.CheckLCRMRegionNode;
import com.xwtech.xwecp.flow.works.chains.nodes.CheckLIInvokePrivilegeNode;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.service.ServiceExecutor;

/**
 * 控制外观（門面）
 * 
 * @author maofw
 * 
 */
public class WorkFlowFacade
{
	private static WorkFlowFacade workFlowFacade = new WorkFlowFacade();
	// 起始节点
	private IFlowControl startFlowControl;

	private WorkFlowFacade()
	{
		// 建立节点流程
		// 1. 接入权限验证节点
		startFlowControl = new CheckAccessPrivilegeNode();
		// 2. 设置 流量控制验证节点
		IFlowControl flowControl = startFlowControl.setNext(new CheckChannelFlowNode());
		// 3. 设置接口调用权限验证节点
		flowControl = flowControl.setNext(new CheckLIInvokePrivilegeNode());
		// 4.根据地市和渠道判断是否能够请求对应的CRM地址
		flowControl = flowControl.setNext(new CheckLCRMRegionNode());
		// 5. 设置最终请求节点
		flowControl.setNext(new CallServiceNode());
	}

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static WorkFlowFacade getInstance()
	{
		return workFlowFacade;
	}

	/**
	 * 执行入口
	 * 
	 * @return
	 */
	public ServiceMessage startExec(ServiceExecutor serviceExecutor, MessageHelper messageHelper, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo)
	{
		return this.startFlowControl.run(serviceExecutor, messageHelper, inputMessage, clientIp, channelInfo);
	}
}
