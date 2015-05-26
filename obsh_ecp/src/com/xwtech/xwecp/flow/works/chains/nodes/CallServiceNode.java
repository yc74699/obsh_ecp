package com.xwtech.xwecp.flow.works.chains.nodes;

import com.xwtech.xwecp.flow.works.chains.AbstractFlowControl;
import com.xwtech.xwecp.log.PerformanceTracer;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ServiceExecutor;

/**
 * 最后请求节点
 * 
 * @author maofw
 * 
 */
public class CallServiceNode extends AbstractFlowControl
{

	@Override
	public boolean execute(ServiceExecutor serviceExecutor, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo, Object o)
	{
		// 直接返回true 执行
		return true;
	}

	@Override
	public ServiceMessage failed(MessageHelper messageHelper, ServiceMessage inputMessage, Object o)
	{
		// execute 返回true 所以该方法不会被执行
		return null;
	}

	// 成功执行之后 执行的方法
	public ServiceMessage executeSuccess(ServiceExecutor serviceExecutor, MessageHelper messageHelper, ServiceMessage inputMessage, Object o) throws Exception
	{
		// System.out.println(this.getClass()+" executeSuccess!");
		// 如果 判断成功 可以继续
		PerformanceTracer pt = PerformanceTracer.getInstance();
		long n = pt.addBreakPoint();
		BaseServiceInvocationResult ret;

		ret = serviceExecutor.callService(inputMessage);

		pt.trace("处理业务逻辑结束...", n);
		String accessId = inputMessage.getHead().getSequence();
		if ( ret != null )
		{
			ret.setAccessId(accessId);
		}
		ServiceMessage responseMsg = messageHelper.createResponseMessage(inputMessage);
		ResponseData responseData = new ResponseData();
		responseData.setServiceResult(ret);
		responseMsg.setData(responseData);

		/***********************************************************************
		 * 测试返回结果 生产环境需要删除！！！-start
		 **********************************************************************/
		// -------------------------------------
		/*
		 * ServiceMessage responseMsg =
		 * messageHelper.createResponseMessage(inputMessage); ResponseData
		 * responseData = new ResponseData(); responseData.setServiceResult(new
		 * BaseServiceInvocationResult()); responseMsg.setData(responseData);
		 */
		/***********************************************************************
		 * 测试返回结果 生产环境需要删除！！！-end
		 **********************************************************************/

		return responseMsg;
	}

	// 成功执行之后是否执行
	public boolean isExecuteSuccess()
	{
		return true;
	}
}
