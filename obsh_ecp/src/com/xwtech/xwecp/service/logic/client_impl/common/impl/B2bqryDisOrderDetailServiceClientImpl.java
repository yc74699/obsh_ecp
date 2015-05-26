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
import com.xwtech.xwecp.service.logic.client_impl.common.IB2bqryDisOrderDetailService;
import com.xwtech.xwecp.service.logic.pojo.B2B006Result;

public class B2bqryDisOrderDetailServiceClientImpl extends
BaseClientServiceImpl implements IB2bqryDisOrderDetailService {

	public B2B006Result b2bqryDisOrderDetail(String region, String operId,
			String queryType, String disorderId, String pageSize,
			String pageIndex) throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "B2B006";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_region = new RequestParameter("region", region);
		__requestData.getParams().add(__param_region);
		RequestParameter __param_operId = new RequestParameter("operId", operId);
		__requestData.getParams().add(__param_operId);
		RequestParameter __param_queryType = new RequestParameter("queryType", queryType);
		__requestData.getParams().add(__param_queryType);
		RequestParameter __param_disorderId = new RequestParameter("disorderId", disorderId);
		__requestData.getParams().add(__param_disorderId);
		RequestParameter __param_pageSize = new RequestParameter("pageSize", pageSize);
		__requestData.getParams().add(__param_pageSize);
		RequestParameter __param_pageIndex = new RequestParameter("pageIndex", pageIndex);
		__requestData.getParams().add(__param_pageIndex);
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
		B2B006Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof B2B006Result)
			{
				__result = (B2B006Result)__ret;
			}
			else
			{
				__result = new B2B006Result();
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
