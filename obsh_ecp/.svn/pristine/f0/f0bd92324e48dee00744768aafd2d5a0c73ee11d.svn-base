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
import com.xwtech.xwecp.service.logic.client_impl.common.ISwitchProductService;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.DEL011003Result;

public class SwitchProductServiceClientImpl extends BaseClientServiceImpl implements ISwitchProductService
{
	public DEL011003Result switchProduct(String phoneNum, String oldProId, String newProId, List<ProPackage> oldPackages, List<ProService> oldServices, List<ProIncrement> oldIncrements, List<ProPackage> closePackages, List<ProService> closeServices, List<ProIncrement> closeIncrements, List<ProPackage> openPackages, List<ProService> openServices, List<ProIncrement> openIncrements, List<ProSelf> openSelfs) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL011003";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_oldProId = new RequestParameter("oldProId", oldProId);
		__requestData.getParams().add(__param_oldProId);
		RequestParameter __param_newProId = new RequestParameter("newProId", newProId);
		__requestData.getParams().add(__param_newProId);
		RequestParameter __param_oldPackages = new RequestParameter("oldPackages", oldPackages);
		__requestData.getParams().add(__param_oldPackages);
		RequestParameter __param_oldServices = new RequestParameter("oldServices", oldServices);
		__requestData.getParams().add(__param_oldServices);
		RequestParameter __param_oldIncrements = new RequestParameter("oldIncrements", oldIncrements);
		__requestData.getParams().add(__param_oldIncrements);
		RequestParameter __param_closePackages = new RequestParameter("closePackages", closePackages);
		__requestData.getParams().add(__param_closePackages);
		RequestParameter __param_closeServices = new RequestParameter("closeServices", closeServices);
		__requestData.getParams().add(__param_closeServices);
		RequestParameter __param_closeIncrements = new RequestParameter("closeIncrements", closeIncrements);
		__requestData.getParams().add(__param_closeIncrements);
		RequestParameter __param_openPackages = new RequestParameter("openPackages", openPackages);
		__requestData.getParams().add(__param_openPackages);
		RequestParameter __param_openServices = new RequestParameter("openServices", openServices);
		__requestData.getParams().add(__param_openServices);
		RequestParameter __param_openIncrements = new RequestParameter("openIncrements", openIncrements);
		__requestData.getParams().add(__param_openIncrements);
		RequestParameter __param_openSelfs = new RequestParameter("openSelfs", openSelfs);
		__requestData.getParams().add(__param_openSelfs);
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
		DEL011003Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL011003Result)
			{
				__result = (DEL011003Result)__ret;
			}
			else
			{
				__result = new DEL011003Result();
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