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
import com.xwtech.xwecp.service.logic.client_impl.common.IModifyCZZXRWUserInfoService;
import com.xwtech.xwecp.service.logic.pojo.DEL040028Result;

public class ModifyCZZXRWUserInfoServiceClientImpl extends BaseClientServiceImpl implements IModifyCZZXRWUserInfoService
{
	public DEL040028Result modifyCZZXRWUserInfo(String phoneNum, String custAddr, String custName, String custEmail, String custpostCode, String custId, String icNo, String icAddr, String linkName, String linkPhone, String linkAddr, String homeTel, String officeTel, String brokerId, String brokerAddr, String brokerTel, String brokerName) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040028";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_custAddr = new RequestParameter("custAddr", custAddr);
		__requestData.getParams().add(__param_custAddr);
		RequestParameter __param_custName = new RequestParameter("custName", custName);
		__requestData.getParams().add(__param_custName);
		RequestParameter __param_custEmail = new RequestParameter("custEmail", custEmail);
		__requestData.getParams().add(__param_custEmail);
		RequestParameter __param_custpostCode = new RequestParameter("custpostCode", custpostCode);
		__requestData.getParams().add(__param_custpostCode);
		RequestParameter __param_custId = new RequestParameter("custId", custId);
		__requestData.getParams().add(__param_custId);
		RequestParameter __param_icNo = new RequestParameter("icNo", icNo);
		__requestData.getParams().add(__param_icNo);
		RequestParameter __param_icAddr = new RequestParameter("icAddr", icAddr);
		__requestData.getParams().add(__param_icAddr);
		RequestParameter __param_linkName = new RequestParameter("linkName", linkName);
		__requestData.getParams().add(__param_linkName);
		RequestParameter __param_linkPhone = new RequestParameter("linkPhone", linkPhone);
		__requestData.getParams().add(__param_linkPhone);
		RequestParameter __param_linkAddr = new RequestParameter("linkAddr", linkAddr);
		__requestData.getParams().add(__param_linkAddr);
		RequestParameter __param_homeTel = new RequestParameter("homeTel", homeTel);
		__requestData.getParams().add(__param_homeTel);
		RequestParameter __param_officeTel = new RequestParameter("officeTel", officeTel);
		__requestData.getParams().add(__param_officeTel);
		RequestParameter __param_brokerId = new RequestParameter("brokerId", brokerId);
		__requestData.getParams().add(__param_brokerId);
		RequestParameter __param_brokerAddr = new RequestParameter("brokerAddr", brokerAddr);
		__requestData.getParams().add(__param_brokerAddr);
		RequestParameter __param_brokerTel = new RequestParameter("brokerTel", brokerTel);
		__requestData.getParams().add(__param_brokerTel);
		RequestParameter __param_brokerName = new RequestParameter("brokerName", brokerName);
		__requestData.getParams().add(__param_brokerName);
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
		DEL040028Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040028Result)
			{
				__result = (DEL040028Result)__ret;
			}
			else
			{
				__result = new DEL040028Result();
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