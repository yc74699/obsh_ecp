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
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFmyNewSpandProduseCHService;
import com.xwtech.xwecp.service.logic.pojo.QRY040114Result;

public class QueryFmyNewSpandProduseCHServiceClientImpl extends BaseClientServiceImpl implements IQueryFmyNewSpandProduseCHService {

	public QRY040114Result queryNewFmySpandProduseCH(String subSid,
			String cycle, Integer qryType, String qrysubsid, String isPool)
			throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY040114";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_subSid = new RequestParameter("subSid", subSid);
		__requestData.getParams().add(__param_subSid);
		RequestParameter __param_cycle = new RequestParameter("cycle", cycle);
		__requestData.getParams().add(__param_cycle);
		RequestParameter __param_qryType = new RequestParameter("qryType", qryType);
		__requestData.getParams().add(__param_qryType);
		RequestParameter __param_qrysubsid = new RequestParameter("qrysubsid", qrysubsid);
		__requestData.getParams().add(__param_qrysubsid);
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
		QRY040114Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY040114Result)
			{
				__result = (QRY040114Result)__ret;
			}
			else
			{
				__result = new QRY040114Result();
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
