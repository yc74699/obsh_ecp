package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.tbank.rpc.message.TBankRequestMessage;
import com.xwtech.tbank.rpc.message.TBankResponseMessage;
import com.xwtech.tbank.rpc.message.request.TBankCloseAccountRequestContent;
import com.xwtech.tbank.rpc.message.request.TBankOpenAccountRequestContent;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040080Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.util.TbankJsonUtil;
/**
 * 流量银行开通与关闭账户
 * @author Mr Ou
 * @date 2014-07-17
 */
public class OperateTbankAccountInvocation extends BaseInvocation implements ILogicalService{
	
	private static final Logger logger = Logger.getLogger(OperateTbankAccountInvocation.class);

	public BaseServiceInvocationResult executeService(String accessId,ServiceConfig config, List<RequestParameter> params) 
	{
		DEL040080Result result = new DEL040080Result();
		//请求参数
		String phoneNum =(String)getParameters(params,"phoneNum");
		String accountType =(String)getParameters(params,"accountType");
		String operType =(String)getParameters(params,"operType");
		
		//流量银行接口方法
		String method = "";
		//组装报文对象
		TBankRequestMessage trm = new TBankRequestMessage();
		//operType为1时开通流量银行
		if(!"".equals(operType) && "1".equals(operType))
		{
			method = "openAccount";
			//组装开通流量银行json报文
			TBankOpenAccountRequestContent openAccountrContent = new TBankOpenAccountRequestContent();
			openAccountrContent.setAccountType(accountType);
			openAccountrContent.setMobileNo(phoneNum);
			openAccountrContent.setQueryTime(new Date());
			trm.setSvcCont(openAccountrContent); 
			//发送请求与解析返回json
			result = getTbankResult(method,trm,accessId,params,result);
		}
		//operType为2时关闭流量银行
		else if(!"".equals(operType) && "2".equals(operType))
		{
			method = "closeAccount";
			//组装关闭流量银行json报文
			TBankCloseAccountRequestContent closeAccountContent = new TBankCloseAccountRequestContent();
			closeAccountContent.setAccountType(accountType);
			closeAccountContent.setMobileNo(phoneNum);
			closeAccountContent.setQueryTime(new Date());
			trm.setSvcCont(closeAccountContent);
			//发送请求与解析返回json
			result = getTbankResult(method,trm,accessId,params,result);
		}
		else
		{
			result.setResultCode("-1");
			result.setErrorMessage("操作类型错误!(1:开通流量银行账户,2：关闭流量银行账户)");
		}
		return result;
	}
	/**
	 * 组装json报文字符串、发送请求与解析
	 * @param method
	 * @param trm
	 * @param accessId
	 * @param params
	 * @param result
	 * @return DEL040080Result
	 */
	protected DEL040080Result getTbankResult(String method,TBankRequestMessage trm,String accessId,List<RequestParameter> params,DEL040080Result result)
	{
		String reqJson = "";
		String rsJson = "";
		try
		{
			//组装请求的json字符串
			reqJson = TbankJsonUtil.internalWriteRequest(method,new Object[] { trm }, "-5797640080114934769");
			logger.info(" ====== 发送流量银行账户开关JSON报文 ======\n" + reqJson);
			if(null != reqJson && !"".equals(reqJson))
			{
			   rsJson = (String) this.remote.callJsonRemote(new StringTeletext(reqJson, accessId, "", this.generateCity(params)));
			   logger.info(" ====== 返回流量银行账户开关JSON报文 ======\n" + rsJson);
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
			logger.info(" ====== 请求流量银行账户开关JSON错误 ======\n",e);
		}
		return null;
	}

}
