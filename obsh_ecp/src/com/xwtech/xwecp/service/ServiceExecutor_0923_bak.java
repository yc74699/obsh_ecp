package com.xwtech.xwecp.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IServiceCallerPrivilegeDAO;
import com.xwtech.xwecp.flow.flowSizeContrl.ChannelFlowInfo;
import com.xwtech.xwecp.flow.flowSizeContrl.ChannelFlowQueue;
import com.xwtech.xwecp.flow.flowSizeContrl.FlowContrlUtil;
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
import com.xwtech.xwecp.pojo.ChannelLimitInfo;
import com.xwtech.xwecp.test.ThreadTest;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;

public class ServiceExecutor_0923_bak
{
	private static final Logger logger = Logger.getLogger(ServiceExecutor_0923_bak.class);

	private ServiceLocator serviceLocator = null;

	private MessageHelper messageHelper = null;

	private IServiceCallerPrivilegeDAO serviceCallerPrivilegeDAO = null;

	private SequenceGenerator accessIdGenerator;

	public static interface ServiceConstants
	{
		// 地市路由参数
		String USER_CITY = "fixed_ddr_city";

		// 来源渠道参数
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
	 * 
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
		pt.trace("取到sequence[" + accessId + "]...", n);
		ServiceMessage requestMsg = null;
		ServiceMessage responseMsg = null;
		String clientAccessId = "";
		long responseTime = accessTime;
		Exception stackTrace = null;

		try
		{
			n = pt.addBreakPoint();
			requestMsg = this.xmlMessage2Object(xmlRequest);
			pt.trace("反序列化XML请求报文成JAVA对象...", n);
			clientAccessId = requestMsg.getHead().getSequence();
			requestMsg.getHead().setSequence(accessId);

			// 获得渠道信息
			// 接入渠道编码
			String channel = requestMsg.getHead().getChannel();
			ChannelInfo channelInfo = this.queryChannelInfo(channel);

			// 接入权限验证
			if ( this.checkAccessPrivilege(channelInfo, requestMsg.getHead().getUser(), requestMsg.getHead().getPwd(), clientIp) )
			{
				// 流量控制
				if ( this.checkChannelFlow(channelInfo, requestMsg) )
				{
					// 接口调用权限验证
					if ( this.checkLIInvokePrivilege(channelInfo, requestMsg.getHead().getCmd()) )
					{
						// this.addChannelFlowInfo(requestMsg);

						n = pt.addBreakPoint();
						/*
						 * BaseServiceInvocationResult ret =
						 * this.callService(requestMsg); pt.trace("处理业务逻辑结束...",
						 * n); if ( ret != null ) { ret.setAccessId(accessId); }
						 * 
						 * responseMsg =
						 * messageHelper.createResponseMessage(requestMsg);
						 * ResponseData responseData = new ResponseData();
						 * responseData.setServiceResult(ret);
						 * responseMsg.setData(responseData);
						 */

						responseMsg = messageHelper.createResponseMessage(requestMsg);
						ResponseData responseData = new ResponseData();
						responseData.setServiceResult(new BaseServiceInvocationResult());
						responseMsg.setData(responseData);

					}
					else
					{
						responseMsg = messageHelper.createNoInvokePrivilegeResponseMessage(requestMsg);
					}
					// 移除當前請求
					this.removeChannelFlow(requestMsg);
				}
				else
				{
					// System.out.println("over flow !!!");
					responseMsg = messageHelper.createOverFlowResponseMessage(requestMsg);
				}
			}
			else
			{
				responseMsg = messageHelper.createNoAccessPrivilegeResponseMessage(requestMsg);
			}

		}
		catch (Exception e)
		{
			logger.error(e, e);
			stackTrace = e;			
		}

		if ( stackTrace != null )
		{
			responseMsg = messageHelper.createErrorResponseMessage(requestMsg);
		}

		// write access log
		String xmlResponse = null;
		if ( responseMsg != null )
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

	/**
	 * 处理结束，扣除流量
	 * 
	 * @param msg
	 */
	private void removeChannelFlow(ServiceMessage msg)
	{
		// 接入渠道编码
		String channel = msg.getHead().getChannel();
		String cmd = msg.getHead().getCmd();
		String clientAccessId = msg.getHead().getSequence();
		com.xwtech.xwecp.flow.connContrl.FlowContrlUtil.removeCurrConn(cmd, clientAccessId, channel);
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
	 * 
	 * @param msg
	 * @param user
	 *            接入渠道的用户名
	 * @param password
	 *            接入渠道的密码
	 * @param clientIP
	 * @return
	 */
	private boolean checkAccessPrivilege(ChannelInfo channelInfo, String user, String password, String clientIP)
	{
		if ( channelInfo == null ) { return false; }
		// 接入渠道的用户名
		// String user = msg.getHead().getUser();
		// 接入渠道的密码
		// String password = msg.getHead().getPwd();
		// 接入渠道编码
		// String channel = msg.getHead().getChannel();

		// 是否需要鉴权IP
		boolean isNeedAuthIp = false;
		// 是否需要鉴权密码
		boolean isNeedAuthPwd = false;

		if ( "1".equals(channelInfo.getNeedAuthIp()) )
			isNeedAuthIp = true;
		if ( "1".equals(channelInfo.getNeedAuthPwd()) )
			isNeedAuthPwd = true;

		// IP验证
		if ( isNeedAuthIp && !this.serviceCallerPrivilegeDAO.getCallerAcceptAddress(channelInfo.getChannelId()).contains(clientIP) ) { return false; }
		// 用户名密码验证
		if ( isNeedAuthPwd )
		{
			if ( !user.equals(channelInfo.getUserNum()) || !password.equals(channelInfo.getPassword()) ) { return false; }
		}
		return true;
	}

	/**
	 * 验证客户端渠道的接口调用权限
	 * 
	 * @param msg
	 * @param clientIP
	 * @return
	 */
	private boolean checkLIInvokePrivilege(ChannelInfo channelInfo, String cmd)
	{
		if ( channelInfo == null ) { return false; }
		// 接入渠道编码
		// String channel = msg.getHead().getChannel();
		// 接口编码
		// String cmd = msg.getHead().getCmd();

		if ( "1".equals(channelInfo.getNeedAuthLi()) )
		{
			if ( !this.getServiceCallerPrivilegeDAO().getCallerAcceptLiLst(channelInfo.getChannelId()).contains(cmd) ) { return false; }
		}

		return true;
	}

	/**
	 * 流量控制
	 * 
	 * @param msg
	 * @return
	 */
	private boolean checkChannelFlow(ChannelInfo channelInfo, ServiceMessage msg)
	{
		if ( channelInfo == null ) { return false; }
		// 接入渠道编码
		// String channel = msg.getHead().getChannel();
		String clientAccessId = msg.getHead().getSequence();
		String cmd = msg.getHead().getCmd();
		if ( "1".equals(channelInfo.getNeedFlowContrl()) )
		{
			long maxSize = StringUtil.isNull(channelInfo.getMaxConnNum()) ? 0 : Long.parseLong(channelInfo.getMaxConnNum());
			// 获得当日 渠道流量详情配置情况
			List<ChannelLimitInfo> list = this.getServiceCallerPrivilegeDAO().getChannelLimitInfoList(channelInfo.getChannelId(), DateTimeUtil.getTodayDay());
			if ( list != null && list.size() > 0 )
			{
				// 获得当前时间阶段的配置详情信息
				ChannelLimitInfo channelLimitInfo = this.searChannelLimitInfo(list, DateTimeUtil.getTodayChar14().substring(8), 0, list.size() - 1);
				if ( channelLimitInfo != null )
				{
					// 设置為时间段配置的最大连接数
					maxSize = channelLimitInfo.getMaxConnums();
				}
			}
			if ( !com.xwtech.xwecp.flow.connContrl.FlowContrlUtil.checkCurrConnNums(cmd, clientAccessId, channelInfo.getChannelId(), maxSize) ) { return false; }
			// if (
			// !FlowContrlUtil.checkCurrFlowSize(channelInfo.getChannelId(),
			// maxSize) ) { return false; }
		}

		return true;
	}

	/**
	 * 获得渠道流量限制详情对象
	 * 
	 * @param msg
	 * @return
	 */
	private ChannelLimitInfo searChannelLimitInfo(List<ChannelLimitInfo> list, String currentTime, int start, int end)
	{
		// System.out.println("--start:" + start + "-end:" + end);
		ChannelLimitInfo channelLimitInfo = null;

		if ( start <= end )
		{
			int mid = (start + end) / 2;
			// System.out.println("--mid:" + mid);
			// 获得当前时间段的流量配置
			channelLimitInfo = list.get(mid);
			// 比较时间段范围
			// 当前时间比开始时间小 则查找下一个对象
			if ( !StringUtil.isNull(channelLimitInfo.getStartTime()) && (channelLimitInfo.getStartTime().compareTo(currentTime) > 0) )
			{
				channelLimitInfo = searChannelLimitInfo(list, currentTime, start, mid - 1);
			}
			else if ( !StringUtil.isNull(channelLimitInfo.getEndTime()) && (channelLimitInfo.getEndTime().compareTo(currentTime) < 0) )
			{
				channelLimitInfo = searChannelLimitInfo(list, currentTime, mid + 1, end);
			}

		}

		return channelLimitInfo;

	}

	/**
	 * 添加渠道流量信息到队列中
	 * 
	 * @param channel
	 * @param cmd
	 */
	private void addChannelFlowInfo(ServiceMessage msg)
	{
		String channel = msg.getHead().getChannel();
		String cmd = msg.getHead().getCmd();
		ChannelFlowInfo flowInfo = new ChannelFlowInfo();
		flowInfo.setChannel(channel);
		flowInfo.setCmd(cmd);
		flowInfo.setRequestTime(DateTimeUtil.getTodayChar17());

		ChannelFlowQueue.getInstance().push(flowInfo);
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
		List<RequestParameter> params = ((RequestData) (msg.getData())).getParams();
		InvocationContext context = ((RequestData) (msg.getData())).getContext();
		if ( context != null )
		{
			Map<String, Object> map = context.toContextParameter();
			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();)
			{
				Object key = iterator.next();
				RequestParameter contextParam = new RequestParameter();
				contextParam.setParameterName("context_" + key.toString());
				contextParam.setParameterValue(map.get(key));
				params.add(contextParam);
			}
		}
		// 增加一个固有参数fixed_ddr_city
		String userCity = msg.getHead().getUserCity();
		RequestParameter userCityParam = new RequestParameter(ServiceConstants.USER_CITY, userCity == null ? "" : userCity);
		params.add(userCityParam);
		// 修改结束(BOSS分地市割接)
		ServiceInfo sInfo = this.findService(msg.getHead().getCmd(), params);
		// 2010年7月28日修改, 掌厅接入, 掌厅向BOSS传递的有几个参数和网厅不一样
		String invokeChannel = msg.getHead() != null ? msg.getHead().getChannel() : "";
		invokeChannel = invokeChannel == null ? "" : invokeChannel;
		RequestParameter invokeChannelParam = new RequestParameter(ServiceConstants.INVOKE_CHANNEL, invokeChannel);
		params.add(invokeChannelParam);
		// 修改结束,掌厅接入的来源渠道传递
		return sInfo.getServiceInstance().execute(msg.getHead().getSequence());
	}

	/**
	 * 根据渠道编码获得渠道对象
	 * 
	 * @param channel
	 * @return
	 */
	private ChannelInfo queryChannelInfo(String channel)
	{
		ChannelInfo channelInfo = null;
		try
		{
			channelInfo = this.getServiceCallerPrivilegeDAO().getChannelInfo(channel);

		}
		catch (DAOException e)
		{
			logger.error("获取渠道信息出错！");
			channelInfo = null;
		}
		return channelInfo;
	}

	public static void main(String[] args)
	{
		List<ChannelLimitInfo> list = new ArrayList<ChannelLimitInfo>();
		for (int i = 0; i < 24; i++)
		{
			ChannelLimitInfo channelLimitInfo = new ChannelLimitInfo();
			channelLimitInfo.setStartTime((i < 10 ? "0" : "") + i + "0000");
			channelLimitInfo.setEndTime((i < 10 ? "0" : "") + i + "5959");
			channelLimitInfo.setMaxConnums(i);
			list.add(channelLimitInfo);

			System.out.println(channelLimitInfo.getStartTime() + "-" + channelLimitInfo.getEndTime());
		}
		String str = "223300";// DateTimeUtil.getTodayChar14().substring(8);

		ServiceExecutor_0923_bak serviceExecutor = new ServiceExecutor_0923_bak();
		ChannelLimitInfo channelLimitInfo = serviceExecutor.searChannelLimitInfo(list, str, 0, list.size() - 1);

		System.out.println("---:" + str);
		long s = System.currentTimeMillis();
		System.out.println("---:" + channelLimitInfo.getStartTime() + "-" + channelLimitInfo.getEndTime() + "-" + channelLimitInfo.getMaxConnums());
		System.out.println("---times:" + (System.currentTimeMillis() - s));

	}
}