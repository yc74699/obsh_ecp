package com.xwtech.xwecp.communication;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xwtech.tbank.rpc.message.TBankResponseMessage;
import com.xwtech.xwecp.log.BossRequestLogger;
import com.xwtech.xwecp.log.PerformanceTracer;
import com.xwtech.xwecp.service.server.DefaultServiceImpl;
import com.xwtech.xwecp.util.TbankJsonUtil;
import com.xwtech.xwecp.msg.SequenceGenerator;


public class RemoteImpl implements IRemote
{
	private static final Logger logger = Logger.getLogger(RemoteImpl.class);
	
	private ICommunicateAdapter adapter;
	
	private SequenceGenerator bossRequestIdGenerator;
	
	private static final Map<String,String> statusCode = new HashMap<String,String>(){
		{
			put("400","错误请求");
			put("401","未授权");
			put("403","禁止");
			put("404","未找到");
			put("408","请求超时");
			put("500","服务器内部错误");
			put("501","尚未实施");
			put("502","错误网关");
			put("503","服务不可用");
			put("504","网关超时");
			put("505","http版本不受支持");
			
		}
	};
	public SequenceGenerator getBossRequestIdGenerator()
	{
		return bossRequestIdGenerator; 
	}

	public void setBossRequestIdGenerator(SequenceGenerator bossRequestIdGenerator)
	{
		this.bossRequestIdGenerator = bossRequestIdGenerator;
	}

	public ICommunicateAdapter getAdapter()
	{
		return adapter;
	}

	public void setAdapter(ICommunicateAdapter adapter)
	{
		this.adapter = adapter;
	}
	
	// QRY050015 超时，单独设置超时时间而用
	public Object callRemote2(IStreamableMessage request) throws CommunicateException {
		PerformanceTracer pt = PerformanceTracer.getInstance();
		long n = 0;
		if(this.adapter != null)
		{
			n = pt.addBreakPoint();
			ICommunicator communicator = this.adapter.findCommunicatorForRequest(request);
			pt.trace("查找communicator...", n);
			String req = request.toMessage();
			long beginTime, endTime;
			long beginNano, endNano;
			beginTime = endTime = System.currentTimeMillis();
			beginNano = endNano = System.nanoTime();
			String accessId = "UNKNOWN";
			String identity = request.getIdentity();
			Exception stackTrace = null;
			if(request instanceof DefaultServiceImpl.StringTeletext)
			{
				accessId = ((DefaultServiceImpl.StringTeletext)(request)).getAccessId();
			}
			
			String ret = null;
			try
			{
				n = pt.addBreakPoint();
				ret = communicator.send3CRM(req, "");
				pt.trace("纯BOSS通信...", n);
			}
			catch(Exception e)
			{
				stackTrace = e;
			}
			/**
			 * 添加用来解决crm空响应的问题
			 */
			ret = packXML(ret);
			//end
//			endTime = System.currentTimeMillis();
			endNano = System.nanoTime();
			n = pt.addBreakPoint();
			String []responseInfo = this.getResponseInfo(ret);
			pt.trace("getResponseInfo...", n);
			n = pt.addBreakPoint();
			String bossRequestId = this.bossRequestIdGenerator.next();
			pt.trace("取BOSS请求sequence...", n);
			n = pt.addBreakPoint();
			RemoteCallContext remoteCallContext = request.getContext();
			String traceId = "";
			if(null != remoteCallContext)
			{
				traceId = remoteCallContext.getTraceId()== null ? "" : remoteCallContext.getTraceId();
					
			}
			//在context中找traceId
			
			//BossRequestLogger.log(bossRequestId, accessId, identity, beginTime, endTime, req, ret, stackTrace != null, stackTrace, responseInfo[0], responseInfo[1], responseInfo[2]);
			BossRequestLogger.log(bossRequestId, accessId, identity, beginTime, (endNano-beginNano), req, ret, stackTrace != null, stackTrace, responseInfo[0], responseInfo[1], responseInfo[2],traceId);
			pt.trace("记录BOSS访问日志...", n);
			if(stackTrace != null)
			{
				throw new CommunicateException(stackTrace);
			}
			return ret;
		}
		else
		{
			throw new CommunicateException("没有配置Communicate Filter, 通讯模块无法适配!");			
		}
	}
	public Object callRemote(IStreamableMessage request) throws CommunicateException
	{
		PerformanceTracer pt = PerformanceTracer.getInstance();
		
		
		long n = 0;
		if(this.adapter != null)
		{
			n = pt.addBreakPoint();
			ICommunicator communicator = this.adapter.findCommunicatorForRequest(request);
			pt.trace("查找communicator...", n);
			String req = request.toMessage();
			long beginTime, endTime;
			beginTime = endTime = System.currentTimeMillis();
			long beginNano, endNano;
			beginNano = endNano = System.nanoTime();
			String accessId = "UNKNOWN";
			String identity = request.getIdentity();
			Exception stackTrace = null;
			if(request instanceof DefaultServiceImpl.StringTeletext)
			{
				accessId = ((DefaultServiceImpl.StringTeletext)(request)).getAccessId();
			}
			
			String ret = null;
			try
			{
				long begtime  ;long endtime;
				n = pt.addBreakPoint();
				begtime = System.currentTimeMillis();
				ret = communicator.send2CRM(req, "");
				pt.trace("纯BOSS通信...", n);
				endtime = System.currentTimeMillis();
				Long result = endtime - begtime;
				//crm或者Boss接口响应时间超过2s 打印出来 
				if(result >= 2000 )
				{
					String str = "<process_code>";
				    int index = req.indexOf("<process_code>");
				    int endIndex = req.indexOf("</process_code>");
				    String cmd = req.substring(index+str.length(), endIndex);
					logger.info("----------------------------");
					logger.info("jsmcc_crm_"+":"+cmd+"----:"+result);
					logger.info("---------------------------");
				}
			}
			catch(Exception e)
			{
				stackTrace = e;
				logger.info("jsmcc_exception_"+stackTrace.getMessage());
			}
			/**
			 * 添加用来解决crm空响应的问题
			 */
			ret = packXML(ret);
			//end
//			endTime = System.currentTimeMillis();
			endNano = System.nanoTime();
			n = pt.addBreakPoint();
			String []responseInfo = this.getResponseInfo(ret);
			pt.trace("getResponseInfo...", n);
			n = pt.addBreakPoint();
			String bossRequestId = this.bossRequestIdGenerator.next();
			pt.trace("取BOSS请求sequence...", n);
			n = pt.addBreakPoint();
			RemoteCallContext remoteCallContext = request.getContext();
			String traceId = "";
			if(null != remoteCallContext)
			{
				traceId = remoteCallContext.getTraceId()== null ? "" : remoteCallContext.getTraceId();
					
			}
			//在context中找traceId
			
			//BossRequestLogger.log(bossRequestId, accessId, identity, beginTime, endTime, req, ret, stackTrace != null, stackTrace, responseInfo[0], responseInfo[1], responseInfo[2]);
			BossRequestLogger.log(bossRequestId, accessId, identity, beginTime, (endNano-beginNano), req, ret, stackTrace != null, stackTrace, responseInfo[0], responseInfo[1], responseInfo[2],traceId);
			pt.trace("记录BOSS访问日志...", n);
			if(stackTrace != null)
			{
				throw new CommunicateException(stackTrace);
			}
			return ret;
		}
		else
		{
			throw new CommunicateException("没有配置Communicate Filter, 通讯模块无法适配!");			
		}
	}
	
	protected String[] getResponseInfo(String responseMsg)
	{
		if(responseMsg == null)
		{
			return new String[]{"","",""};
		}
		int respResultIndexStart = responseMsg.indexOf("<resp_result>");
		int respResultIndexEnd = responseMsg.indexOf("</resp_result>");
		
		int respCodeIndexStart = responseMsg.indexOf("<resp_code>");
		int respCodeIndexEnd = responseMsg.indexOf("</resp_code>");
		
		int respDescIndexStart = responseMsg.indexOf("<resp_desc>");
		int respDescIndexEnd = responseMsg.indexOf("</resp_desc>");
		boolean hasCdata = false;
		if(respDescIndexStart > -1 && respDescIndexEnd > -1)
		{
			String tempString  = responseMsg.substring(respDescIndexStart,respDescIndexEnd);
			hasCdata = tempString.indexOf("<![") > -1;
		}
		//清单查询返回字段与其他报文不一样
		int respTypeIndexStart = responseMsg.indexOf("<resp_type>");
		int respTypeIndexEnd = responseMsg.indexOf("</resp_type>");
		
		if((respResultIndexStart == -1 && respTypeIndexStart == -1) || respCodeIndexStart == -1 )
		{
			return new String[]{"", "", ""};
		}
		else
		{
			String respResult = "";
			if(respResultIndexStart != -1)
			{
				respResult = responseMsg.substring(respResultIndexStart + 13, respResultIndexEnd);
			}
			if(respTypeIndexStart != -1)
			{
				respResult = responseMsg.substring(respTypeIndexStart + 11, respTypeIndexEnd);
			}
			String respCode = responseMsg.substring(respCodeIndexStart + 11, respCodeIndexEnd);
			String respDesc="";
			if(respDescIndexStart != -1){
				if(hasCdata)
				{
					respDesc = responseMsg.substring(respDescIndexStart + 20, respDescIndexEnd-3);
				}
				else
				{
				 respDesc = responseMsg.substring(respDescIndexStart + 11, respDescIndexEnd);
				}
			}
			return new String[]{respResult, respCode, respDesc};
		}
	}
	/**
	 * 用于CRM空响应
	 * @param ret
	 * @return
	 */
	private String packXML(String ret)
	{
		if(null == ret || "".equals(ret) || "null".equals(ret) || null != statusCode.get(ret) )
		{
			String des = null == statusCode.get(ret) ? "" : statusCode.get(ret);
			ret = "<?xml version=\"1.0\" encoding=\"GBK\"?>"+
			"<operation_out>"+
			"<process_code/>"+
			"<sysfunc_id/>"+
			"<response_time/>"+
			"<request_source/>"+
			"<request_seq/>"+
			"<request_type/>"+
			"<content/>"+
			"<response>"+
			"<resp_type>1</resp_type>"+
			"<resp_code>-99999</resp_code>"+
			"<resp_desc>CRM状态码："+ret+des+"</resp_desc>"+
			"</response>"+
			"</operation_out>";
			
		}
		return ret;
	}
	
	public static void main(String[] args) {
		
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\" ?><operation_out><response><resp_result>0maoyw</resp_result><resp_code>0000123</resp_code><resp_desc>jihongrui</resp_desc></response></operation_out>";
		new RemoteImpl().getResponseInfo(xml);
	}
    //流量银行发送请求
	public Object callJsonRemote(IStreamableMessage request)throws CommunicateException
	{
		PerformanceTracer pt = PerformanceTracer.getInstance();
		long n = 0;
		if(this.adapter != null)
		{
			n = pt.addBreakPoint();
			ICommunicator communicator = this.adapter.findCommunicatorForRequest(request);
			pt.trace("查找communicator...", n);
			String req = request.toMessage();
			long beginTime, endTime;
			beginTime = endTime = System.currentTimeMillis();
			String accessId = "UNKNOWN";
			String identity = request.getIdentity();
			Exception stackTrace = null;
			if(request instanceof DefaultServiceImpl.StringTeletext)
			{
				accessId = ((DefaultServiceImpl.StringTeletext)(request)).getAccessId();
			}
			
			String ret = null;
			try
			{
				n = pt.addBreakPoint();
				//向流量银行服务器发送请求
				ret = communicator.send2CRM(req, "");
				pt.trace("流量银行通信...", n);
			}
			catch(Exception e)
			{
				stackTrace = e;
			}
			/**
			 * 添加用来解决流量银行空响应的问题
			 */
			ret = packJSON(ret);
			
			endTime = System.currentTimeMillis();
			n = pt.addBreakPoint();
			String []responseInfo = this.getJsonResponseInfo(ret);
			pt.trace("getResponseInfo...", n);
			n = pt.addBreakPoint();
			String bossRequestId = this.bossRequestIdGenerator.next();
			pt.trace("取BOSS请求sequence...", n);
			n = pt.addBreakPoint();
			RemoteCallContext remoteCallContext = request.getContext();
			String traceId = "";
			if(null != remoteCallContext)
			{
				traceId = remoteCallContext.getTraceId()== null ? "" : remoteCallContext.getTraceId();
					
			}
			BossRequestLogger.log(bossRequestId, accessId, identity, beginTime, endTime, req, ret, stackTrace != null, stackTrace, responseInfo[0], responseInfo[1], responseInfo[2],traceId);
			pt.trace("记录访问流量银行服务日志...", n);
			if(stackTrace != null)
			{
				throw new CommunicateException(stackTrace);
			}
			return ret;
		}
		else
		{
			throw new CommunicateException("没有配置Communicate Filter, 通讯模块无法适配!");			
		}
	}
	//处理流量银行返回的结果集
	protected String[] getJsonResponseInfo(String responseMsg)
	{
		if(responseMsg == null)
		{
			return new String[]{"","",""};
		}
		String respResult = "";
		String respCode = "";
		String respDesc = "";
		try {
			TBankResponseMessage readResponse = TbankJsonUtil.readResponse(TBankResponseMessage.class, new ByteArrayInputStream(responseMsg.getBytes("UTF-8")));
			respResult = readResponse.getResponse().getRspType();
			respCode = readResponse.getResponse().getRspCode();
			respDesc = readResponse.getResponse().getRspDesc();
		}
		catch (Exception e) 
		{
		   e.printStackTrace();
		}
		return new String[]{respResult, respCode, respDesc};
	}
	/**
	 * 用于流量银行空响应
	 * @param ret
	 * @return
	 */
	private String packJSON(String ret)
	{
		if(null == ret || "".equals(ret) || "null".equals(ret) || null != statusCode.get(ret) )
		{
			ret = "{\"jsonrpc\":\"2.0\","+
			"\"id\":\"-5797640080114934769\","+
			"\"result\":{"+
			"\"actionCode\":0,"+
			"\"processTime\":1405409366421,"+
			"\"msgSender\":\"ECP\","+
			"\"svcContVer\":\"V1.0\","+
			"\"svcCont\":null,"+
			"\"response\":{"+
			"\"rspType\":\"-1\",\"rspCode\":\"-99999\",\"rspDesc\":\"流量银行空响应\"},"+
			"\"bipcode\":null,"+
			"\"bipcode\":null,"+
			"\"bipver\":\"V1.0\"}}";
		}
		return ret;
	}


}
