package com.xwtech.xwecp.service.logic.client_impl.common.impl;

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
import com.xwtech.xwecp.service.logic.client_impl.common.INetcardChgconfirmService;
import com.xwtech.xwecp.service.logic.pojo.DEL040078Result;

public class NetcardChgconfirmServiceClientImpl extends BaseClientServiceImpl implements INetcardChgconfirmService
{
	public DEL040078Result netcardChgconfirm(Map<String,Object> map) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040078";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_telnum = new RequestParameter("telnum", map.get("telnum"));
		__requestData.getParams().add(__param_telnum);
		RequestParameter __param_ecardsn = new RequestParameter("ecardsn",  map.get("ecardsn"));
		__requestData.getParams().add(__param_ecardsn);
		RequestParameter __param_password = new RequestParameter("password",  map.get("password"));
		__requestData.getParams().add(__param_password);
		RequestParameter __param_temptelnum = new RequestParameter("temptelnum",  map.get("temptelnum"));
		__requestData.getParams().add(__param_temptelnum);
		RequestParameter __param_type = new RequestParameter("type",  map.get("type"));
		__requestData.getParams().add(__param_type);
		RequestParameter __param_ecardimsi = new RequestParameter("ecardimsi",  map.get("ecardimsi"));
		__requestData.getParams().add(__param_ecardimsi);
		RequestParameter __param_restype = new RequestParameter("restype",  map.get("restype"));
		__requestData.getParams().add(__param_restype);
		RequestParameter __param_cardregion = new RequestParameter("cardregion",  map.get("cardregion"));
		__requestData.getParams().add(__param_cardregion);
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
		DEL040078Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040078Result)
			{
				__result = (DEL040078Result)__ret;
			}
			else
			{
				__result = new DEL040078Result();
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