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
import com.xwtech.xwecp.service.logic.client_impl.common.ITransMarketPlanService;
import com.xwtech.xwecp.service.logic.pojo.DEL100005Result;

public class TransMarketPlanServiceClientImpl extends BaseClientServiceImpl implements ITransMarketPlanService
{
	public DEL100005Result transMarketPlan(String functionType, String servnumber, String actid, String privid, String packid, String busidpackid, String gettype, String orderid,String checkFlag,String operid,String rewardList) throws LIException
	{  
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL100005";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_functionType = new RequestParameter("functionType", functionType);
		__requestData.getParams().add(__param_functionType);
		RequestParameter __param_servnumber = new RequestParameter("servnumber", servnumber);
		__requestData.getParams().add(__param_servnumber);
		RequestParameter __param_actid = new RequestParameter("actid", actid);
		__requestData.getParams().add(__param_actid);
		RequestParameter __param_privid = new RequestParameter("privid", privid);
		__requestData.getParams().add(__param_privid);
		RequestParameter __param_packid = new RequestParameter("packid", packid);
		__requestData.getParams().add(__param_packid);
		RequestParameter __param_busidpackid = new RequestParameter("busidpackid", busidpackid);
		__requestData.getParams().add(__param_busidpackid);
		RequestParameter __param_gettype = new RequestParameter("gettype", gettype);
		__requestData.getParams().add(__param_gettype);
		RequestParameter __param_orderid = new RequestParameter("orderid", orderid);
		__requestData.getParams().add(__param_orderid);
		RequestParameter __param_checkFlag = new RequestParameter("checkFlag", checkFlag);
		__requestData.getParams().add(__param_checkFlag);
		RequestParameter __param_operid = new RequestParameter("operid", operid);
		__requestData.getParams().add(__param_operid);
		RequestParameter __param_rewardList = new RequestParameter("rewardList", rewardList);
		__requestData.getParams().add(__param_rewardList);
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
		DEL100005Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL100005Result)
			{
				__result = (DEL100005Result)__ret;
			}
			else
			{
				__result = new DEL100005Result();
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