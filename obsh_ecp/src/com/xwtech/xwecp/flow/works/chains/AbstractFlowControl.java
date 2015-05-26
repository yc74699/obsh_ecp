package com.xwtech.xwecp.flow.works.chains;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.service.ServiceExecutor;

/**
 * 抽象控制类
 * 
 * @author maofw
 * 
 */
public abstract class AbstractFlowControl implements IFlowControl
{
	private static final Logger logger = Logger.getLogger(AbstractFlowControl.class);
	private IFlowControl flowControl;

	// 设置下一个节点内容 返回下一个节点对象
	public IFlowControl setNext(IFlowControl flowControl)
	{
		this.flowControl = flowControl;
		return flowControl;
	}

	// 子类实现 执行方法
	public abstract boolean execute(ServiceExecutor serviceExecutor, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo, Object o);

	// 子类实现 执行失败 调用方法
	public abstract ServiceMessage failed(MessageHelper messageHelper, ServiceMessage inputMessage, Object o);

	// 异常处理请求
	public ServiceMessage exception(ServiceExecutor serviceExecutor, MessageHelper messageHelper, ServiceMessage inputMessage)
	{
		this.execptionExec(serviceExecutor, inputMessage);
		return messageHelper.createErrorResponseMessage(inputMessage);
	}

	// 是否執行executeSuccess方法（默认不执行 只有最后一个真正请求的时候才执行）
	public boolean isExecuteSuccess()
	{
		return false;
	}

	// 成功执行之后是否执行
	public boolean isExecuteAfterSuccess()
	{
		return false;
	}

	// 成功执行之后 执行的方法 默认不执行任何内容 由子类重载具体执行内容
	public ServiceMessage executeSuccess(ServiceExecutor serviceExecutor, MessageHelper messageHelper,ServiceMessage inputMessage, Object o) throws Exception
	{
		return null;
	}

	// 成功执行之后 执行的方法 默认不执行任何内容 由子类重载具体执行内容
	public void executeAfterSuccess(ServiceExecutor serviceExecutor, ServiceMessage inputMessage, ChannelInfo channelInfo, Object o)
	{
	}

	// 出现异常情况时 执行的方法 默认不执行任何内容 由子类重载具体执行内容
	public void execptionExec(ServiceExecutor serviceExecutor, ServiceMessage inputMessage)
	{
	}

	// 创建自定义对象(子类可以自定义实例化)
	public Object createCustomObject()
	{
		return null;
	}

	/**
	 * 模板方法
	 * 
	 * @param inputObject
	 * @return
	 */
	public final ServiceMessage run(ServiceExecutor serviceExecutor, MessageHelper messageHelper, ServiceMessage inputMessage, String clientIp, ChannelInfo channelInfo)
	{
		// serviceExecutor
		ServiceMessage serviceMessage = null;
		try
		{
			// 获得自定义对象
			Object o = createCustomObject();
			boolean b = execute(serviceExecutor, inputMessage, clientIp, channelInfo, o);
			if ( b )
			{
				//是否执行executeSuccess方法
				if ( isExecuteSuccess() )
					serviceMessage = executeSuccess(serviceExecutor, messageHelper,inputMessage, o);
				
				if ( serviceMessage == null && flowControl != null )
				{
					// 执行下一个判断内容
					serviceMessage = flowControl.run(serviceExecutor, messageHelper, inputMessage, clientIp, channelInfo);
				}

				// 成功执行之后 执行的方法
				if ( isExecuteAfterSuccess() )
					executeAfterSuccess(serviceExecutor, inputMessage, channelInfo, o);
			}
			else
			{
				// 执行失败了 调用failed方法 生成失败信息結果ServiceMessage
				serviceMessage = failed(messageHelper, inputMessage, o);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			serviceMessage = exception(serviceExecutor, messageHelper, inputMessage);
		}
		return serviceMessage;
	}
}
