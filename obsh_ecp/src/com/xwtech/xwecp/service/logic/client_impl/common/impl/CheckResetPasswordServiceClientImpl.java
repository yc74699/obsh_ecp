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
import com.xwtech.xwecp.service.logic.client_impl.common.ICheckResetPasswordService;
import com.xwtech.xwecp.service.logic.pojo.QRY090008Result;

public class CheckResetPasswordServiceClientImpl extends BaseClientServiceImpl implements ICheckResetPasswordService
{
	public QRY090008Result checkResetPassword(String msisdn, String flag, String subcmdid, String old_passwd, String new_passwd, String chktype, String issendsms, String callernum) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY090008";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_msisdn = new RequestParameter("msisdn", msisdn);
		__requestData.getParams().add(__param_msisdn);
		RequestParameter __param_flag = new RequestParameter("flag", flag);
		__requestData.getParams().add(__param_flag);
		RequestParameter __param_subcmdid = new RequestParameter("subcmdid", subcmdid);
		__requestData.getParams().add(__param_subcmdid);
		RequestParameter __param_old_passwd = new RequestParameter("old_passwd", old_passwd);
		__requestData.getParams().add(__param_old_passwd);
		RequestParameter __param_new_passwd = new RequestParameter("new_passwd", new_passwd);
		__requestData.getParams().add(__param_new_passwd);
		RequestParameter __param_chktype = new RequestParameter("chktype", chktype);
		__requestData.getParams().add(__param_chktype);
		RequestParameter __param_issendsms = new RequestParameter("issendsms", issendsms);
		__requestData.getParams().add(__param_issendsms);
		RequestParameter __param_callernum = new RequestParameter("callernum", callernum);
		__requestData.getParams().add(__param_callernum);
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
		QRY090008Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY090008Result)
			{
				__result = (QRY090008Result)__ret;
			}
			else
			{
				__result = new QRY090008Result();
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