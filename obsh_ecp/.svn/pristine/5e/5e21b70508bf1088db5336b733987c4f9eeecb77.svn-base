package com.xwtech.xwecp.flow.works.chains.nodes;

import com.xwtech.xwecp.flow.works.chains.AbstractFlowControl;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.service.ServiceExecutor;

/**
 * 接口权限节点
 * 
 * @author maofw
 * 
 */
public class CheckLIInvokePrivilegeNode extends AbstractFlowControl
{

	// 执行判断
	@Override
	public boolean execute(ServiceExecutor serviceExecutor, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo, Object o)
	{
		//System.out.println("---------" + this.getClass() + " execute!!!");
		
		if ( channelInfo == null ) { return false; }
		
		// 接入渠道编码
		// String channel = msg.getHead().getChannel();
		// 接口编码
		String cmd = inputMessage.getHead().getCmd();

		if ( "1".equals(channelInfo.getNeedAuthLi()) )
		{
			if ( !serviceExecutor.getServiceCallerPrivilegeDAO().getCallerAcceptLiLst(channelInfo.getChannelId()).contains(cmd) ) { return false; }
		}

		return true;
	}

	// 校验失败执行方法
	@Override
	public ServiceMessage failed(MessageHelper messageHelper, ServiceMessage inputMessage, Object o)
	{
		return messageHelper.createNoInvokePrivilegeResponseMessage(inputMessage);
	}
}
