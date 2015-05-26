package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import java.util.List;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IVirtualProdInstallMemChkService;
import com.xwtech.xwecp.service.logic.pojo.DEL610048Result;
import com.xwtech.xwecp.service.logic.pojo.MemberinfoIn;

public class VirtualProdInstallMemChkServiceClientImpl extends BaseClientServiceImpl implements IVirtualProdInstallMemChkService
{
	/**
	 * 10.家庭V网订购时校验副号
	 */
	public DEL610048Result virtualProdInstallMemChk(String familysubsid,
			String pkgprodid, String increprodid, String effecttype,
			List<MemberinfoIn> memberInfoIn) throws LIException
	{
		
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL610048";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_familySubSid = new RequestParameter("familysubsid", familysubsid);
		__requestData.getParams().add(__param_familySubSid);
		RequestParameter __param_pkgProdId = new RequestParameter("pkgprodid", pkgprodid);
		__requestData.getParams().add(__param_pkgProdId);
		RequestParameter __param_increProdId = new RequestParameter("increprodid", increprodid);
		__requestData.getParams().add(__param_increProdId);
		RequestParameter __param_effectType = new RequestParameter("effecttype", effecttype);
		__requestData.getParams().add(__param_effectType);
		RequestParameter __param_memberInfoIn = new RequestParameter("memberInfoIn", memberInfoIn);
		__requestData.getParams().add(__param_memberInfoIn);
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
		DEL610048Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL610048Result)
			{
				__result = (DEL610048Result)__ret;
			}
			else
			{
				__result = new DEL610048Result();
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
