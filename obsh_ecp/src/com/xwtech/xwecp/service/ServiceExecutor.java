package com.xwtech.xwecp.service;

//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.xwtech.xwecp.Jedis.RedisInformation;
import com.xwtech.xwecp.dao.DAOException;
import com.xwtech.xwecp.dao.IServiceCallerPrivilegeDAO;
import com.xwtech.xwecp.flow.works.WorkFlowFacade;
import com.xwtech.xwecp.log.LInterfaceAccessLogger;
import com.xwtech.xwecp.log.PerformanceTracer;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.msg.MessageData;
import com.xwtech.xwecp.msg.MessageHead;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.MessageTypeConstants;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.SequenceGenerator;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.pojo.ChannelInfo;
//import com.xwtech.xwecp.service.logic.pojo.EcpLogger;
     
public class ServiceExecutor {
	private static final Logger logger = Logger.getLogger(ServiceExecutor.class);

	private ServiceLocator serviceLocator = null;

	private MessageHelper messageHelper = null;

	private IServiceCallerPrivilegeDAO serviceCallerPrivilegeDAO = null;

	private SequenceGenerator accessIdGenerator;

	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	//ECP服务需要对渠道地市限制的渠道号  
	private static StringBuffer limitChannel = new StringBuffer();
	
	private String ISOPENPOWER = "0";
	
	static 
	{
		limitChannel.append("obsh_channel")     //网厅
		            .append(",")
		            .append("wap_channel")      //掌厅WAP
		            .append(",")
		            .append("jsmcc_channel")    //手机客户端
		            .append(",")
		            .append("openapi_channel"); //能力平台
		
	}
	
	public static interface ServiceConstants {
		// 地市路由参数
		String USER_CITY = "fixed_ddr_city";

		// 来源渠道参数
		String INVOKE_CHANNEL = "fixed_invoke_channel";
	}

	public SequenceGenerator getAccessIdGenerator() {
		return accessIdGenerator;
	}

	public void setAccessIdGenerator(SequenceGenerator accessIdGenerator) {
		this.accessIdGenerator = accessIdGenerator;
	}

	public MessageHelper getMessageHelper() {
		return messageHelper;
	}

	public void setMessageHelper(MessageHelper messageHelper) {
		this.messageHelper = messageHelper;
	}

	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public void setServiceLocator(ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	public IServiceCallerPrivilegeDAO getServiceCallerPrivilegeDAO() {
		return serviceCallerPrivilegeDAO;
	}

	public void setServiceCallerPrivilegeDAO(IServiceCallerPrivilegeDAO serviceCallerPrivilegeDAO) {
		this.serviceCallerPrivilegeDAO = serviceCallerPrivilegeDAO;
	}

	/**
	 * 接口处理
	 * 
	 * @param xmlRequest
	 * @param clientIp
	 * @return
	 */
	public String executeService(String xmlRequest, String clientIp) {
		PerformanceTracer pt = PerformanceTracer.getInstance();
		// long n = 0;
		// long accessTime = System.currentTimeMillis();
		long n = pt.addBreakPoint();
		long accessTime = n;
		String accessId = this.accessIdGenerator.next();
		pt.trace("取到sequence[" + accessId + "]...", n);
		ServiceMessage requestMsg = null;
		ServiceMessage responseMsg = null;
		String clientAccessId = "";
		String channel = "";  //渠道
		String cityId = "";   //地市
		long responseTime = accessTime;
		Exception stackTrace = null;
		String traceId = "";
		try {
			n = pt.addBreakPoint();
			requestMsg = this.xmlMessage2Object(xmlRequest);
			pt.trace("反序列化XML请求报文成JAVA对象...", n);
			clientAccessId = requestMsg.getHead().getSequence();
			requestMsg.getHead().setSequence(accessId);
			
			//获取渠道号
		    channel = requestMsg.getHead().getChannel();
			//获取地市
			cityId = requestMsg.getHead().getUserCity();
	        RequestData data  = (RequestData)requestMsg.getData();
			
//			traceId=(String) data.getContext().toContextParameter().get("traceId");
//			traceId = traceId==null ? "":traceId;
			 if(null != data)
			 {
				 InvocationContext context = data.getContext();
				 if(null != context)
				 {
					 Map<String,Object> map = context.toContextParameter();
					 if(null != map)
					 {
						 traceId = (String) map.get("traceId");
					 }
				 }
			 }
			 //traceId = (String)data.getContext().toContextParameter().get("traceId");
		      traceId = traceId == null ? "" : traceId; 
		    //判断渠道是否需要屏蔽地市
		    g:if(limitChannel.indexOf(channel) != -1 )
		    {
		    	//需要屏蔽渠道的地市有作屏蔽，返回ECP拼装的报文格式返回
		    	if(!isOpenPow(channel, cityId) &&( null != cityId && !"".equals(cityId)))
		    	{
		    		String xmlResponse = "";
					n = pt.addBreakPoint();
					responseTime = n;
					responseMsg = this.createErrorResponseMessage(requestMsg);
					xmlResponse = responseMsg.toString();
					LInterfaceAccessLogger.log(xmlRequest, xmlResponse, accessId, requestMsg, accessTime, responseTime, responseMsg, clientIp, stackTrace, clientAccessId,traceId);
					return xmlResponse;
		    	}
		    	//需要屏蔽渠道的地市没有作屏蔽，直接跳出当前判断
		    	else
		    	{ 
		    		break g;
		    	}
		    }
			
//			if(!serviceCallerPrivilegeDAO.isOpenPower(channel, cityId) &&( null != cityId && !"".equals(cityId)))
//			{
//				String xmlResponse = "";
//				n = pt.addBreakPoint();
//				responseTime = n;
//				responseMsg = this.createErrorResponseMessage(requestMsg);
//				xmlResponse = responseMsg.toString();
//				LInterfaceAccessLogger.log(xmlRequest, xmlResponse, accessId, requestMsg, accessTime, responseTime, responseMsg, clientIp, stackTrace, clientAccessId,traceId);
//				return xmlResponse;
//			}
			
			// 获得渠道信息
			// 接入渠道编码
//			String channel = requestMsg.getHead().getChannel();
			ChannelInfo channelInfo = this.queryChannelInfo(channel);
                              
			// 控制限制 获得返回结果
			responseMsg = WorkFlowFacade.getInstance().startExec(this, messageHelper, requestMsg, clientIp.split("#")[0], channelInfo);
		}
		catch (Exception e) {
			logger.error(e, e);
			stackTrace = e;
		}

		if ( stackTrace != null ) {
			responseMsg = messageHelper.createErrorResponseMessage(requestMsg);
		}
//		
//		n = pt.addBreakPoint();
//		responseTime = n;
//		//添加可视化分析ECP日志
//		responseMsg = doWitdResponse(accessId,requestMsg,  accessTime, responseTime,responseMsg, clientIp,
//				stackTrace, clientAccessId,traceId);
//		
		// write access log
		String xmlResponse = null;
		if ( responseMsg != null ) {
			n = pt.addBreakPoint();
			xmlResponse = responseMsg.toString();
			pt.trace("序列化成XML报文...", n);
		}
		// responseTime = System.currentTimeMillis();
		n = pt.addBreakPoint();
		responseTime = n;
		LInterfaceAccessLogger.log(xmlRequest, xmlResponse, accessId, requestMsg, accessTime, responseTime, responseMsg, clientIp, stackTrace, clientAccessId,traceId);
	    //LInterfaceAccessLogger.log(xmlRequest, xmlResponse, accessId, requestMsg, accessTime, responseTime, responseMsg, clientIp, stackTrace, clientAccessId);

		pt.trace("记录日志...", n);

		return xmlResponse;
	}

	private ServiceMessage xmlMessage2Object(String xmlRequest) throws Exception {
		ServiceMessage msg = ServiceMessage.fromXML(xmlRequest);
		return msg;
	}

	private ServiceInfo findService(String cmd, List<RequestParameter> params) throws Exception {
		return this.serviceLocator.locate(cmd, params);
	}

	/**
	 * 添加渠道流量信息到队列中
	 * 
	 * @param channel
	 * @param cmd
	 */
	/*
	 * protected void addChannelFlowInfo(ServiceMessage msg) { String channel =
	 * msg.getHead().getChannel(); String cmd = msg.getHead().getCmd();
	 * ChannelFlowInfo flowInfo = new ChannelFlowInfo();
	 * flowInfo.setChannel(channel); flowInfo.setCmd(cmd);
	 * flowInfo.setRequestTime(DateTimeUtil.getTodayChar17());
	 * 
	 * ChannelFlowQueue.getInstance().push(flowInfo); }
	 */
	/**
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult callService(ServiceMessage msg) throws Exception
	{
		List<RequestParameter> params = ((RequestData) (msg.getData())).getParams();
		InvocationContext context = ((RequestData) (msg.getData())).getContext();
		if ( context != null ) {
			Map<String, Object> map = context.toContextParameter();
			for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
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

		/***********************************************************************
		 * 2011-10-10 增加分渠道参数内容organ_id request_source等内容
		 */
		// 获得渠道对应的参数配置内容
		Map<String, String> map = this.getServiceCallerPrivilegeDAO().getChannelExtParams(invokeChannel);
		if ( map != null && !map.isEmpty() ) {
			Iterator<String> iterator = map.keySet().iterator();
			String key = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				RequestParameter parameter = new RequestParameter(key, map.get(key) == null ? "" : map.get(key));
				params.add(parameter);
			}
		}
		/***********************************************************************
		 * 修改结束
		 **********************************************************************/

		// 2011-11-15 maofw修改 在增加ServiceInfo对象中增加ServiceMessage对象内容
		sInfo.setServiceMessage(msg);
		// 修改结束
		
		RedisInformation ri = new RedisInformation();
//		return sInfo.getServiceInstance().execute(msg.getHead().getSequence());
		return ri.handleResultToRedis(sInfo);
	}

	/*
	 * 根据渠道编码获得渠道对象
	 * 
	 * @param channel
	 * @return
	 */
	private ChannelInfo queryChannelInfo(String channel) {
		ChannelInfo channelInfo = null;
		try {
			channelInfo = this.getServiceCallerPrivilegeDAO().getChannelInfo(channel);

		}
		catch (DAOException e) {
			logger.error("获取渠道信息出错！");
			channelInfo = null;
		}
		return channelInfo;
	}

	
	/**
	 * 接口处理失败的消息
	 * @param request
	 * @return
	 */
	public ServiceMessage createErrorResponseMessage(ServiceMessage request)
	{
		//获取渠道号
	    String channel = request.getHead().getChannel();
		//获取地市
		String cityId = request.getHead().getUserCity();
		ServiceMessage msg = new ServiceMessage();
		MessageHead head = new MessageHead();
		head.setChannel(request.getHead().getChannel());
		head.setCmd(request.getHead().getCmd());
		head.setSequence(request.getHead().getSequence());
		head.setType(MessageTypeConstants.OUT);
		head.setClientTime(request.getHead().getClientTime());
		head.setServerTime(this.getCurrentTime());
		MessageData data = new ResponseData();
		msg.setHead(head);
		msg.setData(data);
		BaseServiceInvocationResult ret = new BaseServiceInvocationResult();
		ret.setErrorCode("-10101");
		ret.setErrorMessage("渠道:"+channel+"的地市  :"+cityId+"没有ECP服务的接入权限");
		ret.setResultCode("-1");
		ResponseData responseData = new ResponseData();
		responseData.setServiceResult(ret);
		msg.setData(responseData);
		return msg;
	}
	
	/**
	 * @desc  添加ECP服务接口返回日志
	 * @param accessId
	 * @param request
	 * @param accessTime
	 * @param responseTime
	 * @param response
	 * @param clientIp
	 * @param stackTrace
	 * @param clientAccessId
	 * @param userTraceId
	 * @return ServiceMessage
	 */
//	public static ServiceMessage doWitdResponse(String accessId,
//			ServiceMessage request, long accessTime, long responseTime,
//			ServiceMessage response, String clientIp,
//			Throwable stackTrace, String clientAccessId,String userTraceId)
//	{
//		final String f_access_id = accessId;
//		final String f_logic_number = request.getHead().getCmd();
//		final String f_client_ip = clientIp;
//		final String f_channel_num = request.getHead().getChannel();
//		final StringBuffer f_channel_user = new StringBuffer();
//
//		if (request.getHead().getUser() != null
//				&& request.getHead().getUser().trim().length() > 0)
//		{
//			f_channel_user.append(request.getHead().getUser());
//		}
//		else
//		{
//			f_channel_user.append("UNKNOWN");
//		}
//		final String f_is_error = stackTrace == null ? "0" : "1";
//		final String f_result_code = ((ResponseData) response.getData())
//				.getServiceResult().getResultCode();
//		final String f_error_code = ((ResponseData) response.getData())
//				.getServiceResult().getErrorCode();
//		final String f_error_msg = ((ResponseData) response.getData())
//				.getServiceResult().getErrorMessage();
//		final String f_user_mobile = request.getHead().getUserMobile();
//		final String f_user_brand = request.getHead().getUserBrand();
//		final String f_user_city = request.getHead().getUserCity();
//		final String f_biz_code = request.getHead().getBizCode();
//		final StringBuffer f_error_stack = new StringBuffer();
//		if (stackTrace != null)
//		{
//			StringWriter sw = new StringWriter();
//			PrintWriter pw = new PrintWriter(sw);
//			stackTrace.printStackTrace(pw);
//			f_error_stack.append(sw.getBuffer().toString());
//			pw.close();
//			try
//			{
//				sw.close();
//			}
//			catch (IOException e)
//			{
//				logger.error(e, e);
//			}
//		}
//        EcpLogger ecpLogger = new EcpLogger();
//		
//		ecpLogger.setAccess_id(f_access_id);
//		ecpLogger.setLogic_number(f_logic_number);
//		ecpLogger.setAccess_time(String.valueOf(accessTime));
//		ecpLogger.setResponse_time(String.valueOf(responseTime));
//		ecpLogger.setClient_ip(f_client_ip);
//		ecpLogger.setChannel_num(f_channel_num);
//		ecpLogger.setChannel_user(f_channel_user.toString());
//		
//		ecpLogger.setIs_error(f_is_error);
//		ecpLogger.setResult_code(f_result_code);
//		ecpLogger.setError_code(f_error_code);
//		ecpLogger.setError_msg(f_error_msg);
//		ecpLogger.setUser_mobile(f_user_mobile);
//		ecpLogger.setUser_brand(f_user_brand);
//		ecpLogger.setUser_city(f_user_city);
//		ecpLogger.setBiz_code(f_biz_code);
//		ecpLogger.setError_stack(f_error_stack.toString());
//		ecpLogger.setClient_access_id(f_access_id);
//		ecpLogger.setTrace_Id(userTraceId);
//		BaseServiceInvocationResult rt = ((ResponseData)(response.getData())).getServiceResult();
//		rt.setEcpLogger(ecpLogger);
//		return response;
//	}
	//判断渠道是否屏蔽所调用地市
	public  boolean isOpenPow(String channel,String city)
	{
		boolean flag = false;
		try
		{
			String flagStr = "";
			Map<String,String> obsh_city_maps = serviceCallerPrivilegeDAO.getChannelLimitCityInfos();
			if(null != obsh_city_maps && obsh_city_maps.size() >=0)
			{
				flagStr = obsh_city_maps.get(channel+"_"+city);
				if(ISOPENPOWER.equals(flagStr))
				{
					flag = true;
				}
			}
		} 
		catch (DAOException e) 
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	public String getCurrentTime()
	{
		return format.format(new Date());
	}
	
	public static void main(String[] args) {
	}
}