package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangeScoreService;
import com.xwtech.xwecp.service.logic.pojo.DEL610053Result;

/**
 * 积分扣减接口
 * @author 张斌
 * 2015-4-27
 */
public class ChangeScoreServiceClientImpl extends BaseClientServiceImpl implements IChangeScoreService
{
	public DEL610053Result changeScore(String phoneNum, String scoreChangeAmt, String userType, String orderNo, String orderDt, String businessId) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL610053";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_scoreChangeAmt = new RequestParameter("scoreChangeAmt", scoreChangeAmt);
		__requestData.getParams().add(__param_scoreChangeAmt);
		RequestParameter __param_userType = new RequestParameter("userType", userType);
		__requestData.getParams().add(__param_userType);
		RequestParameter __param_orderNo = new RequestParameter("orderNo", orderNo);
		__requestData.getParams().add(__param_orderNo);
		RequestParameter __param_orderDt = new RequestParameter("orderDt", orderDt);
		__requestData.getParams().add(__param_orderDt);
		RequestParameter __param_businessId = new RequestParameter("businessId", businessId);
		__requestData.getParams().add(__param_businessId);
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
		DEL610053Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL610053Result)
			{
				__result = (DEL610053Result)__ret;
			}
			else
			{
				__result = new DEL610053Result();
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