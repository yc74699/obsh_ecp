package com.xwtech.xwecp.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IServiceCallerPrivilegeDAO;
import com.xwtech.xwecp.flow.connContrl.FlowContrlUtil;
import com.xwtech.xwecp.log.LInterfaceAccessLogger;
import com.xwtech.xwecp.log.PerformanceTracer;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.SequenceGenerator;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;

public class ServiceExecutor_Bak
{
	private static final Logger logger = Logger.getLogger(ServiceExecutor.class);
	
	private ServiceLocator serviceLocator = null;
	
	private MessageHelper messageHelper = null;
	
	private IServiceCallerPrivilegeDAO serviceCallerPrivilegeDAO = null;
	
	private SequenceGenerator accessIdGenerator;
	
	public static interface ServiceConstants
	{
		//地市路由参数
		String USER_CITY = "fixed_ddr_city";
		
		//来源渠道参数
		String INVOKE_CHANNEL = "fixed_invoke_channel";
	}
	
	public SequenceGenerator getAccessIdGenerator()
	{
		return accessIdGenerator;
	}

	public void setAccessIdGenerator(SequenceGenerator accessIdGenerator)
	{
		this.accessIdGenerator = accessIdGenerator;
	}

	public MessageHelper getMessageHelper()
	{
		return messageHelper;
	}

	public void setMessageHelper(MessageHelper messageHelper)
	{
		this.messageHelper = messageHelper;
	}

	public ServiceLocator getServiceLocator()
	{
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator)
	{
		this.serviceLocator = serviceLocator;
	}
	
	public IServiceCallerPrivilegeDAO getServiceCallerPrivilegeDAO()
	{
		return serviceCallerPrivilegeDAO;
	}

	public void setServiceCallerPrivilegeDAO(IServiceCallerPrivilegeDAO serviceCallerPrivilegeDAO)
	{
		this.serviceCallerPrivilegeDAO = serviceCallerPrivilegeDAO;
	}
	
	/**
	 * 接口处理
	 * @param xmlRequest
	 * @param clientIp
	 * @return
	 */
	public String executeService(String xmlRequest, String clientIp)
	{
		PerformanceTracer pt = PerformanceTracer.getInstance();
		long n = 0;
		long accessTime = System.currentTimeMillis();
		n = pt.addBreakPoint();
		String accessId = this.accessIdGenerator.next();
		pt.trace("取到sequence["+accessId+"]...", n);
		ServiceMessage requestMsg = null;
		ServiceMessage responseMsg = null;
		String clientAccessId = "";
		long responseTime = accessTime;
		Exception stackTrace = null;
		boolean isFlowContrl = false;
		
		try
		{
			n = pt.addBreakPoint();
			requestMsg = this.xmlMessage2Object(xmlRequest);
			pt.trace("反序列化XML请求报文成JAVA对象...", n);
			clientAccessId = requestMsg.getHead().getSequence();
			requestMsg.getHead().setSequence(accessId);
			
			//接入权限验证
			if(this.checkAccessPrivilege(requestMsg, clientIp))
			{
				//流量控制
				isFlowContrl = this.checkChannelFlow(requestMsg);
				if(isFlowContrl)
				{
					//接口调用权限验证
					if(this.checkLIInvokePrivilege(requestMsg))
					{
						n = pt.addBreakPoint();
						BaseServiceInvocationResult ret = this.callService(requestMsg);
						pt.trace("处理业务逻辑结束...", n);
						if(ret != null)
						{
							ret.setAccessId(accessId);
						}
						responseMsg = messageHelper.createResponseMessage(requestMsg);
						ResponseData responseData = new ResponseData();
						responseData.setServiceResult(ret);
						responseMsg.setData(responseData);
					}
					else
					{
						responseMsg = messageHelper.createNoInvokePrivilegeResponseMessage(requestMsg);
					}
				}
				else
				{
					responseMsg = messageHelper.createOverFlowResponseMessage(requestMsg);
				}
			}
			else
			{
				responseMsg = messageHelper.createNoAccessPrivilegeResponseMessage(requestMsg);
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
			stackTrace = e;
		}
		
		if(stackTrace != null)
		{
			responseMsg = messageHelper.createErrorResponseMessage(requestMsg);
		}
		
		//处理结束，扣除连接数
		if(isFlowContrl)
		{
			this.removeChannelFlow(requestMsg);
		}
		
		//write access log
		String xmlResponse = null;
		if(responseMsg != null)
		{
			n = pt.addBreakPoint();
			xmlResponse = responseMsg.toString();
			pt.trace("序列化成XML报文...", n);
		}
		responseTime = System.currentTimeMillis();
		n = pt.addBreakPoint();
		LInterfaceAccessLogger.log(xmlRequest, xmlResponse, accessId, requestMsg, accessTime, responseTime, responseMsg, clientIp, stackTrace, clientAccessId);
		pt.trace("记录日志...", n);
		
		return xmlResponse;
	}
	
	private ServiceMessage xmlMessage2Object(String xmlRequest) throws Exception
	{
		ServiceMessage msg = ServiceMessage.fromXML(xmlRequest);
		return msg;
	}
	
	private ServiceInfo findService(String cmd, List<RequestParameter> params) throws Exception
	{
		return this.serviceLocator.locate(cmd, params);
	}
	
	/**
	 * 验证客户端渠道的接入权限
	 * @param msg
	 * @param clientIP
	 * @return
	 */
	private boolean checkAccessPrivilege(ServiceMessage msg, String clientIP)
	{
		//接入渠道的用户名
		String user = msg.getHead().getUser();
		//接入渠道的密码
		String password = msg.getHead().getPwd();
		//接入渠道编码
		String channel = msg.getHead().getChannel();

		//是否需要鉴权IP
		boolean isNeedAuthIp = false;
		//是否需要鉴权密码
		boolean isNeedAuthPwd = false;
		try
		{
			ChannelInfo channelInfo = this.getServiceCallerPrivilegeDAO().getChannelInfo(channel);
			
			if("1".equals(channelInfo.getNeedAuthIp()))
				isNeedAuthIp = true;
			if("1".equals(channelInfo.getNeedAuthPwd()))
				isNeedAuthPwd = true;
			
			//IP验证
			if(isNeedAuthIp && !this.serviceCallerPrivilegeDAO.getCallerAcceptAddress(channel).contains(clientIP))
			{
				return false;
			}
			//用户名密码验证
			if(isNeedAuthPwd)
			{
				if(!user.equals(channelInfo.getUserNum()) || !password.equals(channelInfo.getPassword()))
				{
					return false;
				}
			}
		}
		catch(DAOException e)
		{
			logger.error("获取渠道信息出错！");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 验证客户端渠道的接口调用权限
	 * @param msg
	 * @param clientIP
	 * @return
	 */
	private boolean checkLIInvokePrivilege(ServiceMessage msg)
	{
		//接入渠道编码
		String channel = msg.getHead().getChannel();
		//接口编码
		String cmd = msg.getHead().getCmd();
		try
		{
			ChannelInfo channelInfo = this.getServiceCallerPrivilegeDAO().getChannelInfo(channel);
			if("1".equals(channelInfo.getNeedAuthLi()))
			{
				if(!this.getServiceCallerPrivilegeDAO().getCallerAcceptLiLst(channel).contains(cmd))
				{
					return false;
				}
			}
		}
		catch(DAOException e)
		{
			logger.error("获取渠道信息出错！");
			return false;
		}
		return true;
	}
	
	/**
	 * 流量控制
	 * @param msg
	 * @return
	 */
	private boolean checkChannelFlow(ServiceMessage msg)
	{
		//接入渠道编码
		String channel = msg.getHead().getChannel();
		String clientAccessId = msg.getHead().getSequence();
		String cmd = msg.getHead().getCmd();
		try
		{
			ChannelInfo channelInfo = this.getServiceCallerPrivilegeDAO().getChannelInfo(channel);
			if("1".equals(channelInfo.getNeedFlowContrl()))
			{
				if(!FlowContrlUtil.checkCurrConnNums(cmd, clientAccessId, channel, Long.parseLong(channelInfo.getMaxConnNum())))
				{
					return false;
				}
			}
		}
		catch(DAOException e)
		{
			logger.error("获取渠道信息出错！");
			return false;
		}
		return true;
	}
	
	/**
	 * 处理结束，扣除流量
	 * @param msg
	 */
	private void removeChannelFlow(ServiceMessage msg)
	{
		//接入渠道编码
		String channel = msg.getHead().getChannel();
		String cmd = msg.getHead().getCmd();
		String clientAccessId = msg.getHead().getSequence();
		FlowContrlUtil.removeCurrConn(cmd, clientAccessId, channel);
	}
	
	
	/**
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private BaseServiceInvocationResult callService(ServiceMessage msg) throws Exception
	{
		List<RequestParameter> params = ((RequestData)(msg.getData())).getParams();
		InvocationContext context = ((RequestData)(msg.getData())).getContext();
		if(context != null)
		{
			Map<String, Object> map = context.toContextParameter();
			for(Iterator iterator = map.keySet().iterator(); iterator.hasNext();)
			{
				Object key = iterator.next();
				RequestParameter contextParam = new RequestParameter();
				contextParam.setParameterName("context_" + key.toString());
				contextParam.setParameterValue(map.get(key));
				params.add(contextParam);
			}
		}
		//增加一个固有参数fixed_ddr_city
		String userCity = msg.getHead().getUserCity();
		RequestParameter userCityParam = new RequestParameter(ServiceConstants.USER_CITY, userCity == null ? "" : userCity);
		params.add(userCityParam);
		//修改结束(BOSS分地市割接)
		ServiceInfo sInfo = this.findService(msg.getHead().getCmd(), params);
		//2010年7月28日修改, 掌厅接入, 掌厅向BOSS传递的有几个参数和网厅不一样
		String invokeChannel = msg.getHead() != null ? msg.getHead().getChannel() : "";
		invokeChannel = invokeChannel == null ? "" : invokeChannel;
		RequestParameter invokeChannelParam = new RequestParameter(ServiceConstants.INVOKE_CHANNEL, invokeChannel);
		params.add(invokeChannelParam);
		//修改结束,掌厅接入的来源渠道传递
		return sInfo.getServiceInstance().execute(msg.getHead().getSequence());
	}
	
	public static void main(String[] args)
	{

	}
}