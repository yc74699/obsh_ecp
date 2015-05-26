package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.tbank.rpc.message.TBankRequestMessage;
import com.xwtech.tbank.rpc.message.TBankResponseMessage;
import com.xwtech.tbank.rpc.message.request.TBankQueryAccountRequestContent;
import com.xwtech.tbank.rpc.message.response.TBankUsedLevel;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY040081Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.TbankJsonUtil;
/**
 * 流量银行查询档次
 * @author Mr Ou
 * @date 2014-07-17
 */
public class QueryUsedLevelInvocation extends BaseInvocation implements ILogicalService{

	private static final Logger logger = Logger.getLogger(QueryUsedLevelInvocation.class);
	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params)
	{
		
	    QRY040081Result result = new QRY040081Result();
	    //请求参数
		String phoneNum =(String)getParameters(params,"phoneNum");
		//流量银行接口方法
		String method = "queryUsedLevel";
		//组装报文对象
		TBankRequestMessage trm = new TBankRequestMessage();
		TBankQueryAccountRequestContent tarContent = new TBankQueryAccountRequestContent();
		tarContent.setMobileNo(phoneNum);
		tarContent.setQueryTime(new Date());
		trm.setSvcCont(tarContent);
		//发送请求与解析返回json
		result = getTbankResult(method,trm,accessId,params,result);
		return result;
	}
	/**
	 * 组装json报文字符串、发送请求与解析
	 * @param method
	 * @param trm
	 * @param accessId
	 * @param params
	 * @param result
	 * @return QRY040081Result
	 */
	protected QRY040081Result getTbankResult(String method,TBankRequestMessage trm,String accessId,List<RequestParameter> params,QRY040081Result result)
	{
		String reqJson = "";
		String rsJson = "";
		try
		{
			//组装请求的json字符串
			reqJson = TbankJsonUtil.internalWriteRequest(method,new Object[] { trm }, "-5797640080114934769");
			logger.info(" ====== 发送流量银行查询流量档次JSON报文 ======\n" + reqJson);
			if(null != reqJson && !"".equals(reqJson))
			{
			   rsJson = (String) this.remote.callJsonRemote(new StringTeletext(reqJson, accessId, "", this.generateCity(params)));
			   logger.info(" ====== 返回流量银行查询流量档次JSON报文 ======\n" + rsJson);
			   //解析返回json字符串
			   TBankResponseMessage readResponse = (TBankResponseMessage)TbankJsonUtil.readResponse(TBankResponseMessage.class, new ByteArrayInputStream(rsJson.getBytes("UTF-8")));
			   result.setResultCode(readResponse.getResponse().getRspType());
			   result.setErrorCode(readResponse.getResponse().getRspCode());
			   result.setErrorMessage(readResponse.getResponse().getRspDesc());
			   //根据返回json报文中多态对象取值
			   TBankUsedLevel fluxTbankCode = (TBankUsedLevel)readResponse.getSvcCont();
			   if(null != fluxTbankCode)
			   {
				  result.setFluxLevelMap(fluxTbankCode.getMap());
			   }
			   return result;
			}
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			logger.info(" ====== 返回流量银行查询流量档次JSON报文错误 ======\n",e);
		}
		return null;
	}

}
