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
import com.xwtech.xwecp.service.logic.client_impl.common.IGiveOrTakeRedPackService;
import com.xwtech.xwecp.service.logic.pojo.DEL040077Result;
/**
 * 红包赠送与领取接口
 * 
 * @author xufan
 * 2014-03-27
 */
public class GiveOrTakeRedPackServiceClientImpl extends BaseClientServiceImpl implements IGiveOrTakeRedPackService
{
	public DEL040077Result giveOrTakeRedPack(Map<String,Object> map) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040077";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_msisdn1 = new RequestParameter("msisdn1", String.valueOf(map.get("msisdn1")));
		__requestData.getParams().add(__param_msisdn1);
		RequestParameter __param_user_id1= new RequestParameter("user_id1", String.valueOf(map.get("user_id1")));
		__requestData.getParams().add(__param_user_id1);
		RequestParameter __param_msisdn2 = new RequestParameter("msisdn2", String.valueOf(map.get("msisdn2")));
		__requestData.getParams().add(__param_msisdn2);
		RequestParameter __param_user_id2= new RequestParameter("user_id2", String.valueOf(map.get("user_id2")));
		__requestData.getParams().add(__param_user_id2);
		RequestParameter __param_package_type= new RequestParameter("package_type", String.valueOf(map.get("package_type")));
		__requestData.getParams().add(__param_package_type);
		RequestParameter __param_package_code= new RequestParameter("package_code", String.valueOf(map.get("package_code")));
		__requestData.getParams().add(__param_package_code);
		RequestParameter __param_package_inure_mode= new RequestParameter("package_inure_mode", String.valueOf(map.get("package_inure_mode")));
		__requestData.getParams().add(__param_package_inure_mode);
		RequestParameter __param_donateoid= new RequestParameter("donateoid", String.valueOf(map.get("donateoid")));
		__requestData.getParams().add(__param_donateoid);
		RequestParameter __param_giveOrTake= new RequestParameter("giveOrTake", String.valueOf(map.get("giveOrTake")));
		__requestData.getParams().add(__param_giveOrTake);
		
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
		DEL040077Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040077Result)
			{
				__result = (DEL040077Result)__ret;
			}
			else
			{
				__ret = new DEL040077Result();
				__result.setResultCode(__ret.getResultCode());
				__result.setErrorCode(__ret.getErrorCode());
				__result.setErrorMessage(__ret.getErrorMessage());
			}
		}
		catch(Exception __e)
		{
			__errorStack = __e;
			__e.printStackTrace();
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