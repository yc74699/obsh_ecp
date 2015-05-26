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
import com.xwtech.xwecp.service.logic.client_impl.common.IPcustfullService;
import com.xwtech.xwecp.service.logic.pojo.DEL610050Result;

public class PcustfullServiceClientImpl extends BaseClientServiceImpl implements IPcustfullService{

	public DEL610050Result pcustFull(String userId, String custId,
			String ddrCity, String custIcNo, String custIcType,
			String custName, String custIcAddr, String custContactAdd)
			throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL610050";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_userId = new RequestParameter("userId", userId);
		__requestData.getParams().add(__param_userId);
		RequestParameter __param_custId = new RequestParameter("custId", custId);
		__requestData.getParams().add(__param_custId);
		RequestParameter __param_ddrCity = new RequestParameter("ddrCity", ddrCity);
		__requestData.getParams().add(__param_ddrCity);
		RequestParameter __param_custIcNo = new RequestParameter("custIcNo", custIcNo);
		__requestData.getParams().add(__param_custIcNo);
		RequestParameter __param_custIcType = new RequestParameter("custIcType", custIcType);
		__requestData.getParams().add(__param_custIcType);
		RequestParameter __param_custName = new RequestParameter("custName", custName);
		__requestData.getParams().add(__param_custName);
		RequestParameter __param_custIcAddr = new RequestParameter("custIcAddr", custIcAddr);
		__requestData.getParams().add(__param_custIcAddr);
		RequestParameter __param_custContactAdd = new RequestParameter("custContactAdd", custContactAdd);
		__requestData.getParams().add(__param_custContactAdd);
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
		DEL610050Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL610050Result)
			{
				__result = (DEL610050Result)__ret;
			}
			else
			{
				__ret = new DEL610050Result();
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
