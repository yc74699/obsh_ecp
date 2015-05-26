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
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactBossBusinessService;
import com.xwtech.xwecp.service.logic.pojo.DEL010005Result;

public class TransactBossBusinessServiceClientImpl extends BaseClientServiceImpl implements ITransactBossBusinessService
{
	public DEL010005Result transactBossBusiness(String phoneNum, String id, int oprType, int chooseFlag, String beginDate, String endDate, String reserve1, String reserve2) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL010005";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_id = new RequestParameter("id", id);
		__requestData.getParams().add(__param_id);
		RequestParameter __param_oprType = new RequestParameter("oprType", oprType);
		__requestData.getParams().add(__param_oprType);
		RequestParameter __param_chooseFlag = new RequestParameter("chooseFlag", chooseFlag);
		__requestData.getParams().add(__param_chooseFlag);
		RequestParameter __param_beginDate = new RequestParameter("beginDate", beginDate);
		__requestData.getParams().add(__param_beginDate);
		RequestParameter __param_endDate = new RequestParameter("endDate", endDate);
		__requestData.getParams().add(__param_endDate);
		RequestParameter __param_reserve1 = new RequestParameter("reserve1", reserve1);
		__requestData.getParams().add(__param_reserve1);
		RequestParameter __param_reserve2 = new RequestParameter("reserve2", reserve2);
		__requestData.getParams().add(__param_reserve2);
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
		DEL010005Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL010005Result)
			{
				__result = (DEL010005Result)__ret;
			}
			else
			{
				__result = new DEL010005Result();
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