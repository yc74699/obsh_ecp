package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCdrBctchQryService;
import com.xwtech.xwecp.service.logic.pojo.QRY010038Result;

public class QueryCdrBctchQryServiceClientImpl extends BaseClientServiceImpl implements IQueryCdrBctchQryService
{
	public QRY010038Result cdrBctchQry(Map<String,Object> map,List<Map<String,Object>>userList) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY010038";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_batchNo = new RequestParameter("batchNo", map.get("batchNo"));
		__requestData.getParams().add(__param_batchNo);
		RequestParameter __param_cdrType = new RequestParameter("cdrType", map.get("cdrType"));
		__requestData.getParams().add(__param_cdrType);
		RequestParameter __param_beginDay = new RequestParameter("beginDay", map.get("beginDay"));
		__requestData.getParams().add(__param_beginDay);
		RequestParameter __param_endDay = new RequestParameter("endDay", map.get("endDay"));
		__requestData.getParams().add(__param_endDay);
		RequestParameter __param_userList = new RequestParameter("userList",userList);
		__requestData.getParams().add(__param_userList);
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
		QRY010038Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY010038Result)
			{
				__result = (QRY010038Result)__ret;
			}
			else
			{
				__result = new QRY010038Result();
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