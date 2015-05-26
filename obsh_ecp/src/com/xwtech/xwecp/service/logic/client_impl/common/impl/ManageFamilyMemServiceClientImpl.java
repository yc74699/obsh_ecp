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
import com.xwtech.xwecp.service.logic.client_impl.common.IManageFamilyMemService;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.NewMemberInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY200002Result;

public class ManageFamilyMemServiceClientImpl extends BaseClientServiceImpl implements IManageFamilyMemService
{
	public QRY200002Result manageFamilyMemService(String familysubsid, String oprtsrc, String oprType, List<NewMemberInfo> newMemberInfo) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY200002";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_familysubsid = new RequestParameter("familysubsid", familysubsid);
		__requestData.getParams().add(__param_familysubsid);
		RequestParameter __param_oprtsrc = new RequestParameter("oprtsrc", oprtsrc);
		__requestData.getParams().add(__param_oprtsrc);
		RequestParameter __param_oprType = new RequestParameter("oprType", oprType);
		__requestData.getParams().add(__param_oprType);
		RequestParameter __param_newMemberInfo = new RequestParameter("newMemberInfo", newMemberInfo);
		__requestData.getParams().add(__param_newMemberInfo);
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
		QRY200002Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY200002Result)
			{
				__result = (QRY200002Result)__ret;
			}
			else
			{
				__result = new QRY200002Result();
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