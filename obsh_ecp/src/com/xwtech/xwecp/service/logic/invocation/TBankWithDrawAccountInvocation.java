package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.tbank.rpc.message.TBankRequestMessage;
import com.xwtech.tbank.rpc.message.TBankResponseMessage;
import com.xwtech.tbank.rpc.message.request.TBankWithDrawAccountRequestContent;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040084Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.TbankJsonUtil;
/**
 * 流量银行支取流量
 * @author Mr Ou
 * @date 2014-07-18
 */
public class TBankWithDrawAccountInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(TBankWithDrawAccountInvocation.class);
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		DEL040084Result result = new DEL040084Result();
		//请求入参
		String phoneNum =(String)getParameters(params,"phoneNum");//手机号码
		String oprType =(String)getParameters(params,"oprType");//操作类型 只支持 1 
		String bizNo =(String)getParameters(params,"bizNo");//业务类型 流量宝、话费宝
		String amount =(String)getParameters(params,"amount");//流量(M)
		
		//流量银行接口方法
		String method = "withDrawAccount";
		//组装报文对象
		TBankRequestMessage trm = new TBankRequestMessage();
		trm.setBIPCode(oprType);
		TBankWithDrawAccountRequestContent tarContent = new TBankWithDrawAccountRequestContent();
		tarContent.setMobileNo(phoneNum);
		tarContent.setBizNo(bizNo);
		tarContent.setAmount(amount);
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
	 * @return DEL040084Result
	 */
	protected DEL040084Result getTbankResult(String method,TBankRequestMessage trm,String accessId,List<RequestParameter> params,DEL040084Result result)
	{
		String reqJson = "";
		String rsJson = "";
		try
		{
			//组装请求的json字符串
			reqJson = TbankJsonUtil.internalWriteRequest(method,new Object[] { trm }, "-5797640080114934769");
			logger.info(" ====== 发送流量银行支取流量JSON报文 ======\n" + reqJson);
			if(null != reqJson && !"".equals(reqJson))
			{
			   rsJson = (String) this.remote.callJsonRemote(new StringTeletext(reqJson, accessId, "", this.generateCity(params)));
			   logger.info(" ====== 返回流量银行支取流量JSON报文 ======\n" + rsJson);
			   //解析返回json字符串
			   TBankResponseMessage readResponse = (TBankResponseMessage)TbankJsonUtil.readResponse(TBankResponseMessage.class, new ByteArrayInputStream(rsJson.getBytes("UTF-8")));
			   result.setResultCode(readResponse.getResponse().getRspType());
			   result.setErrorCode(readResponse.getResponse().getRspCode());
			   result.setErrorMessage(readResponse.getResponse().getRspDesc());
			   return result;
			}
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			logger.info(" ====== 返回流量银行支取流量JSON报文错误 ======\n",e);
		}
		return null;
	}

}
