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
import com.xwtech.xwecp.service.logic.client_impl.common.IB2BQueryDisOrderService;
import com.xwtech.xwecp.service.logic.pojo.B2B001Result;

public class B2BQueryDisOrderServiceClientImpl extends BaseClientServiceImpl implements IB2BQueryDisOrderService
{
	public B2B001Result queryDisOrder(String region,String operId,String queryType,String storeId, String partnerId,String status,String qrySize,String qryIndex) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "B2B001";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_region = new RequestParameter("region", region);
		__requestData.getParams().add(__param_region);
		RequestParameter __param_operId = new RequestParameter("operId", operId);
		__requestData.getParams().add(__param_operId);
		RequestParameter __param_queryType = new RequestParameter("queryType", queryType);
		__requestData.getParams().add(__param_queryType);
		RequestParameter __param_partnerId = new RequestParameter("partnerId", partnerId);
		__requestData.getParams().add(__param_partnerId);
		RequestParameter __param_storeId = new RequestParameter("storeId", storeId);
		__requestData.getParams().add(__param_storeId);
		RequestParameter __param_status = new RequestParameter("status", status);
		__requestData.getParams().add(__param_status);
		RequestParameter __param_qrySize = new RequestParameter("qrySize", qrySize);
		__requestData.getParams().add(__param_qrySize);
		RequestParameter __param_qryIndxe = new RequestParameter("qryIndex", qryIndex);
		__requestData.getParams().add(__param_qryIndxe);
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
		B2B001Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof B2B001Result)
			{
				__result = (B2B001Result)__ret;
			}
			else
			{
				__result = new B2B001Result();
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