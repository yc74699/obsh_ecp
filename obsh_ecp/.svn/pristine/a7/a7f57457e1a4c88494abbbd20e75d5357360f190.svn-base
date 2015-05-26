package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IOdcExchangeService;
import com.xwtech.xwecp.service.logic.pojo.DEL070001Result;

public class OdcExchangeServiceClientImpl extends BaseClientServiceImpl implements IOdcExchangeService
{
	public DEL070001Result odcExchange(String mobile, String odcNum, String merAwardNum, String merAccountNum, String merAccountPwd, String fromIP) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL070001";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_mobile = new RequestParameter("mobile", mobile);
		__requestData.getParams().add(__param_mobile);
		RequestParameter __param_odcNum = new RequestParameter("odcNum", odcNum);
		__requestData.getParams().add(__param_odcNum);
		RequestParameter __param_merAwardNum = new RequestParameter("merAwardNum", merAwardNum);
		__requestData.getParams().add(__param_merAwardNum);
		RequestParameter __param_merAccountNum = new RequestParameter("merAccountNum", merAccountNum);
		__requestData.getParams().add(__param_merAccountNum);
		RequestParameter __param_merAccountPwd = new RequestParameter("merAccountPwd", merAccountPwd);
		__requestData.getParams().add(__param_merAccountPwd);
		RequestParameter __param_fromIP = new RequestParameter("fromIP", fromIP);
		__requestData.getParams().add(__param_fromIP);
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
		DEL070001Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL070001Result)
			{
				__result = (DEL070001Result)__ret;
			}
			else
			{
				__result = new DEL070001Result();
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