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
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactFamilyNetService;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.FamilyShortInfo;
import com.xwtech.xwecp.service.logic.pojo.DEL080002Result;

public class TransactFamilyNetServiceClientImpl extends BaseClientServiceImpl implements ITransactFamilyNetService
{
	public DEL080002Result transactFamilyNet(String operateType, String availableDate, String homeMemberUserId, String productId, List<FamilyShortInfo> familyShorts) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL080002";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_operateType = new RequestParameter("operateType", operateType);
		__requestData.getParams().add(__param_operateType);
		RequestParameter __param_availableDate = new RequestParameter("availableDate", availableDate);
		__requestData.getParams().add(__param_availableDate);
		RequestParameter __param_homeMemberUserId = new RequestParameter("homeMemberUserId", homeMemberUserId);
		__requestData.getParams().add(__param_homeMemberUserId);
		RequestParameter __param_productId = new RequestParameter("productId", productId);
		__requestData.getParams().add(__param_productId);
		RequestParameter __param_familyShorts = new RequestParameter("familyShorts", familyShorts);
		__requestData.getParams().add(__param_familyShorts);
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
		DEL080002Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL080002Result)
			{
				__result = (DEL080002Result)__ret;
			}
			else
			{
				__result = new DEL080002Result();
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