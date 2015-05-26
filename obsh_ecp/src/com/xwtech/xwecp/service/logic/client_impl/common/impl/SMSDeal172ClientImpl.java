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
import com.xwtech.xwecp.service.logic.client_impl.common.ISMSDeal172;
import com.xwtech.xwecp.service.logic.pojo.DEL010014Result;

public class SMSDeal172ClientImpl extends BaseClientServiceImpl implements ISMSDeal172
{
	public DEL010014Result smsDeal172(String msisdn, long ddrCity, long feeType, String user172Password, String user172OldPassword, String oprType, long packageCode, long auto1861Code) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL010014";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_msisdn = new RequestParameter("msisdn", msisdn);
		__requestData.getParams().add(__param_msisdn);
		RequestParameter __param_ddrCity = new RequestParameter("ddrCity", ddrCity);
		__requestData.getParams().add(__param_ddrCity);
		RequestParameter __param_feeType = new RequestParameter("feeType", feeType);
		__requestData.getParams().add(__param_feeType);
		RequestParameter __param_user172Password = new RequestParameter("user172Password", user172Password);
		__requestData.getParams().add(__param_user172Password);
		RequestParameter __param_user172OldPassword = new RequestParameter("user172OldPassword", user172OldPassword);
		__requestData.getParams().add(__param_user172OldPassword);
		RequestParameter __param_oprType = new RequestParameter("oprType", oprType);
		__requestData.getParams().add(__param_oprType);
		RequestParameter __param_packageCode = new RequestParameter("packageCode", packageCode);
		__requestData.getParams().add(__param_packageCode);
		RequestParameter __param_auto1861Code = new RequestParameter("auto1861Code", auto1861Code);
		__requestData.getParams().add(__param_auto1861Code);
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
		DEL010014Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL010014Result)
			{
				__result = (DEL010014Result)__ret;
			}
			else
			{
				__result = new DEL010014Result();
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