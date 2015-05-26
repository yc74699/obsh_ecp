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
import com.xwtech.xwecp.service.logic.client_impl.common.IBindInterFeePayService;
import com.xwtech.xwecp.service.logic.pojo.DEL040019Result;

public class BindInterFeePayServiceClientImpl extends BaseClientServiceImpl implements IBindInterFeePayService
{
	public DEL040019Result bindInterFeePay(String phoneNum, String password, String payedPhoneNum, String payedPhoneNumPwd, String interType, String dealType) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040019";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_password = new RequestParameter("password", password);
		__requestData.getParams().add(__param_password);
		RequestParameter __param_payedPhoneNum = new RequestParameter("payedPhoneNum", payedPhoneNum);
		__requestData.getParams().add(__param_payedPhoneNum);
		RequestParameter __param_payedPhoneNumPwd = new RequestParameter("payedPhoneNumPwd", payedPhoneNumPwd);
		__requestData.getParams().add(__param_payedPhoneNumPwd);
		RequestParameter __param_interType = new RequestParameter("interType", interType);
		__requestData.getParams().add(__param_interType);
		RequestParameter __param_dealType = new RequestParameter("dealType", dealType);
		__requestData.getParams().add(__param_dealType);
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
		DEL040019Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040019Result)
			{
				__result = (DEL040019Result)__ret;
			}
			else
			{
				__result = new DEL040019Result();
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