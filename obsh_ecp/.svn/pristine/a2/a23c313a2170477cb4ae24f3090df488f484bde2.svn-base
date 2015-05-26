package com.xwtech.xwecp.service.logic.client_impl.reserve.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.reserve.IQryAmountByOfficeService;
import com.xwtech.xwecp.service.logic.pojo.RES002Result;

public class QryAmountByOfficeServiceClientImpl extends BaseClientServiceImpl implements IQryAmountByOfficeService
{
	public RES002Result qryAmountByOffice(String officeId, String expectTime, String expectPeriod, String amountFlag) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "RES002";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_officeId = new RequestParameter("officeId", officeId);
		__requestData.getParams().add(__param_officeId);
		RequestParameter __param_expectTime = new RequestParameter("expectTime", expectTime);
		__requestData.getParams().add(__param_expectTime);
		RequestParameter __param_expectPeriod = new RequestParameter("expectPeriod", expectPeriod);
		__requestData.getParams().add(__param_expectPeriod);
		RequestParameter __param_amountFlag = new RequestParameter("amountFlag", amountFlag);
		__requestData.getParams().add(__param_amountFlag);
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
		RES002Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof RES002Result)
			{
				__result = (RES002Result)__ret;
			}
			else
			{
			__result = new RES002Result();
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