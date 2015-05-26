package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryNewRealTimeBillingCHService;
import com.xwtech.xwecp.service.logic.pojo.QRY040108Result;

public class QueryNewRealTimeBillingCHServiceClientImpl extends BaseClientServiceImpl implements IQueryNewRealTimeBillingCHService {

	public QRY040108Result queryNewRealTimeBillingCH(String userMsisdn,
			Long idType, String modeId, String startcycle, String endcycle,
			Integer billType, Integer billlType, String isPool)
			throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY040108";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_userMsisdn = new RequestParameter("userMsisdn", userMsisdn);
		__requestData.getParams().add(__param_userMsisdn);
		RequestParameter __param_idType = new RequestParameter("idType", idType);
		__requestData.getParams().add(__param_idType);
		RequestParameter __param_modeId = new RequestParameter("modeId", modeId);
		__requestData.getParams().add(__param_modeId);
		RequestParameter __param_startcycle = new RequestParameter("startcycle", startcycle);
		__requestData.getParams().add(__param_startcycle);
		RequestParameter __param_endcycle = new RequestParameter("endcycle", endcycle);
		__requestData.getParams().add(__param_endcycle);
		RequestParameter __param_billType = new RequestParameter("billType", billType);
		__requestData.getParams().add(__param_billType);
		RequestParameter __param_billlType = new RequestParameter("billlType", billlType);
		__requestData.getParams().add(__param_billlType);
		RequestParameter __param_isPool = new RequestParameter("isPool", isPool);
		__requestData.getParams().add(__param_isPool);
		
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
		QRY040108Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY040108Result)
			{
				__result = (QRY040108Result)__ret;
			}
			else
			{
				__result = new QRY040108Result();
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
