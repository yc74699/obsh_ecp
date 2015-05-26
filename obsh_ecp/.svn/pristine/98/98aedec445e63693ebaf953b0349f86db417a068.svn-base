package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangprodNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040035Result;

public class ChangprodNewServiceClientImpl extends BaseClientServiceImpl implements IChangprodNewService
{
	public DEL040035Result changeProdNew(String city, String rectype, String prodId, String addProductSet, String delProductSet, String affectTime, String mobile, String serviceId) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040035";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_city = new RequestParameter("city", city);
		__requestData.getParams().add(__param_city);
		RequestParameter __param_rectype = new RequestParameter("rectype", rectype);
		__requestData.getParams().add(__param_rectype);
		RequestParameter __param_prodId = new RequestParameter("prodId", prodId);
		__requestData.getParams().add(__param_prodId);
		RequestParameter __param_addProductSet = new RequestParameter("addProductSet", addProductSet);
		__requestData.getParams().add(__param_addProductSet);
		RequestParameter __param_delProductSet = new RequestParameter("delProductSet", delProductSet);
		__requestData.getParams().add(__param_delProductSet);
		RequestParameter __param_affectTime = new RequestParameter("affectTime", affectTime);
		__requestData.getParams().add(__param_affectTime);
		RequestParameter __param_mobile = new RequestParameter("mobile", mobile);
		__requestData.getParams().add(__param_mobile);
		RequestParameter __param_serviceId = new RequestParameter("serviceId", serviceId);
		__requestData.getParams().add(__param_serviceId);
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
		DEL040035Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040035Result)
			{
				__result = (DEL040035Result)__ret;
			}
			else
			{
				__result = new DEL040035Result();
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