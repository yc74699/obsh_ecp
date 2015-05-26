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
import com.xwtech.xwecp.service.logic.client_impl.common.IMarketPlanApply;
import com.xwtech.xwecp.service.logic.pojo.DEL110002Result;

public class MarketPlanApplyClientImpl extends BaseClientServiceImpl implements IMarketPlanApply
{
	public DEL110002Result marketPlanApply(String planid, String subsid, String drawFlag, String rewardList, String goodsPackId, String busiPackId) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL110002";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_planid = new RequestParameter("planid", planid);
		__requestData.getParams().add(__param_planid);
		RequestParameter __param_subsid = new RequestParameter("subsid", subsid);
		__requestData.getParams().add(__param_subsid);
		RequestParameter __param_drawFlag = new RequestParameter("drawFlag", drawFlag);
		__requestData.getParams().add(__param_drawFlag);
		RequestParameter __param_rewardList = new RequestParameter("rewardList", rewardList);
		__requestData.getParams().add(__param_rewardList);
		RequestParameter __param_goodsPackId = new RequestParameter("goodsPackId", goodsPackId);
		__requestData.getParams().add(__param_goodsPackId);
		RequestParameter __param_busiPackId = new RequestParameter("busiPackId", busiPackId);
		__requestData.getParams().add(__param_busiPackId);
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
		DEL110002Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL110002Result)
			{
				__result = (DEL110002Result)__ret;
			}
			else
			{
				__result = new DEL110002Result();
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