package com.xwtech.xwecp.msg;


import java.text.SimpleDateFormat;
import java.util.Date;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;

import org.apache.log4j.Logger;


public class MessageHelper
{
	private static final Logger logger = Logger.getLogger(MessageHelper.class);
	
	private String channel;
	
	private SequenceGenerator sequenceGenerator = new SequenceGenerator();
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public MessageHelper()
	{
		this("");
	}
	
	public MessageHelper(String channel)
	{
		channel = channel==null?"":channel;
		this.channel = channel;
	}
	
	/**
	 * 正常请求消息
	 * @param cmd
	 * @return
	 */
	public ServiceMessage createRequestMessage(String cmd)
	{
		ServiceMessage msg = new ServiceMessage();
		MessageHead head = new MessageHead();
		head.setChannel(channel);
		head.setCmd(cmd);
		head.setSequence(sequenceGenerator.next());
		head.setType(MessageTypeConstants.IN);
		head.setClientTime(this.getCurrentTime());	
		head.setServerTime("");
		MessageData data = new RequestData();
		msg.setHead(head);
		msg.setData(data);
		return msg;
	}
	
	/**
	 * 没有接入权限的失败消息
	 * @param request
	 * @return
	 */
	public ServiceMessage createNoAccessPrivilegeResponseMessage(ServiceMessage request)
	{
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
		ret.setErrorCode("-9999");
		ret.setErrorMessage("接口调用失败 -- 当前渠道无接入ECP的权限!");
		ret.setResultCode("-1");
		ResponseData responseData = new ResponseData();
		responseData.setServiceResult(ret);
		msg.setData(responseData);
		return msg;
	}
	
	/**
	 * 没有相应接口调用权限的失败消息
	 * @param request
	 * @return
	 */
	public ServiceMessage createNoInvokePrivilegeResponseMessage(ServiceMessage request)
	{
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
		ret.setErrorCode("-9999");
		ret.setErrorMessage("接口调用失败 -- 当前渠道无调用该接口的权限!");
		ret.setResultCode("-1");
		ResponseData responseData = new ResponseData();
		responseData.setServiceResult(ret);
		msg.setData(responseData);
		return msg;
	}
	
	/**
	 * 渠道超出流量阀值的失败消息
	 * @param request
	 * @return
	 */
	public ServiceMessage createOverFlowResponseMessage(ServiceMessage request)
	{
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
		ret.setErrorCode("-9999");
		ret.setErrorMessage("接口调用失败 -- 当前渠道已经超出流量阀值!");
		ret.setResultCode("-1");
		ResponseData responseData = new ResponseData();
		responseData.setServiceResult(ret);
		msg.setData(responseData);
		return msg;
	}
	
	/**
	 * 接口处理失败的消息
	 * @param request
	 * @return
	 */
	public ServiceMessage createErrorResponseMessage(ServiceMessage request)
	{
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
		ret.setErrorCode("-10000");
		ret.setErrorMessage("接口调用失败 -- 平台内部错误!");
		ret.setResultCode("-1");
		ResponseData responseData = new ResponseData();
		responseData.setServiceResult(ret);
		msg.setData(responseData);
		return msg;
	}
	
	public ServiceMessage createNoCRMRegionResponseMessage(ServiceMessage request)
	{
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
		ret.setErrorCode("-409");
		ret.setErrorMessage("该地市对应的CRM服务已奔溃，无法请求");
		ret.setResultCode("-1");
		ResponseData responseData = new ResponseData();
		responseData.setServiceResult(ret);
		msg.setData(responseData);
		return msg;
	}
	
	/**
	 * 正常返回消息
	 * @param request
	 * @return
	 */
	public ServiceMessage createResponseMessage(ServiceMessage request)
	{
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
		return msg;
	}
	
	public String getCurrentTime()
	{
		return format.format(new Date());
	}
}
