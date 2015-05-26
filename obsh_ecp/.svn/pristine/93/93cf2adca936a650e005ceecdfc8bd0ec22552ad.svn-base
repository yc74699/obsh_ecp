package com.xwtech.xwecp.service.logic.client_impl.sms.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.sms.IMgrZoneMScoreService;
import com.xwtech.xwecp.service.logic.pojo.DEL020005Result;

public class MgrZoneMScoreServiceClientImpl extends BaseClientServiceImpl implements IMgrZoneMScoreService
{
	public DEL020005Result mgrZoneMScore(String msisdn, String number, String redeeAmount, String randomPwd, String flag) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL020005";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_msisdn = new RequestParameter("msisdn", msisdn);
		__requestData.getParams().add(__param_msisdn);
		RequestParameter __param_number = new RequestParameter("number", number);
		__requestData.getParams().add(__param_number);
		RequestParameter __param_redeeAmount = new RequestParameter("redeeAmount", redeeAmount);
		__requestData.getParams().add(__param_redeeAmount);
		RequestParameter __param_randomPwd = new RequestParameter("randomPwd", randomPwd);
		__requestData.getParams().add(__param_randomPwd);
		RequestParameter __param_flag = new RequestParameter("flag", flag);
		__requestData.getParams().add(__param_flag);
		__msg.setData(__requestData);
		__msg.getHead().setUser(LIInvocationContext.USER);
		__msg.getHead().setPwd(LIInvocationContext.PWD);
		LIInvocationContext __ic = LIInvocationContext.getContext();
		if(__ic != null)
		{
			__msg.getHead().setOpType(__ic.getOpType());
			__msg.getHead().setUserMobile(__ic.getUserMobile());
			__msg.getHead().setUserCity(__ic.getUserCity());
			__msg.getHead().setBizCode(__ic.getBizCode());
			__msg.getHead().setUserBrand(__ic.getUserBrand());
			__msg.getHead().setOperId(__ic.getOperId());
			((RequestData)(__msg.getData())).setContext(__ic.getContextParameter());
		}
		String __requestXML = __msg.toString();
		DEL020005Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL020005Result)
			{
				__result = (DEL020005Result)__ret;
			}
			else
			{
			__result = new DEL020005Result();
			__result.setResultCode(__ret.getResultCode());
			__result.setErrorCode(__ret.getErrorCode());
			__result.setErrorMessage(__ret.getErrorMessage());
			}
		}
		catch(Exception __e)
		{
			__errorStack = __e;
			throw new LIException(__e);
		}
		finally
		{
			long __endTime = System.currentTimeMillis();
			__client.log(__cmd, __client.getPlatformConnection().getRemoteURL(), __requestXML, __responseXML, __msg, __response, __beginTime, __endTime, __errorStack);
		}
		return __result;
	}

}