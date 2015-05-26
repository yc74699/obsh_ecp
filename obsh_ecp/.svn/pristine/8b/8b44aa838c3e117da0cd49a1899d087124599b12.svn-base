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
import com.xwtech.xwecp.service.logic.client_impl.common.IOpenpCustService;
import com.xwtech.xwecp.service.logic.pojo.DEL030003Result;

public class OpenpCustServiceClientImpl extends BaseClientServiceImpl implements IOpenpCustService
{
	public DEL030003Result openpCust(String phoneNum, String custName, String cardNum, String schoolName, String departmentName) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL030003";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_custName = new RequestParameter("custName", custName);
		__requestData.getParams().add(__param_custName);
		RequestParameter __param_cardNum = new RequestParameter("cardNum", cardNum);
		__requestData.getParams().add(__param_cardNum);
		RequestParameter __param_card = new RequestParameter("card", cardNum);
		__requestData.getParams().add(__param_card);
		RequestParameter __param_channel = new RequestParameter("channel",  __msg.getHead().getChannel());
		__requestData.getParams().add(__param_channel);
		RequestParameter __param_schoolName = new RequestParameter("schoolName", schoolName);
		__requestData.getParams().add(__param_schoolName);
		RequestParameter __param_departmentName = new RequestParameter("departmentName", departmentName);
		__requestData.getParams().add(__param_departmentName);
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
		DEL030003Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL030003Result)
			{
				__result = (DEL030003Result)__ret;
			}
			else
			{
				__result = new DEL030003Result();
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

	public DEL030003Result openpCust(String phoneNum, String custName,
			String cardNum, String addr) throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL030003";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_custName = new RequestParameter("custName", custName);
		__requestData.getParams().add(__param_custName);
		RequestParameter __param_cardNum = new RequestParameter("cardNum", cardNum);
		__requestData.getParams().add(__param_cardNum);
		RequestParameter __param_card = new RequestParameter("card", cardNum);
		__requestData.getParams().add(__param_card);
		RequestParameter __param_channel = new RequestParameter("channel",  __msg.getHead().getChannel());
		__requestData.getParams().add(__param_channel);
		RequestParameter __param_addr = new RequestParameter("addr", addr);
		__requestData.getParams().add(__param_addr);
		RequestParameter __param_schoolName = new RequestParameter("schoolName", "");
		__requestData.getParams().add(__param_schoolName);
		RequestParameter __param_departmentName = new RequestParameter("departmentName", "");
		__requestData.getParams().add(__param_departmentName);
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
		DEL030003Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL030003Result)
			{
				__result = (DEL030003Result)__ret;
			}
			else
			{
				__result = new DEL030003Result();
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