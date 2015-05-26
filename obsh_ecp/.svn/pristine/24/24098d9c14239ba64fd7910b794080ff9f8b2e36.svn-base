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
import com.xwtech.xwecp.service.logic.client_impl.common.IRecbindBankctsService;
import com.xwtech.xwecp.service.logic.pojo.DEL040074Result;

public class RecbindBankctsServiceClientImpl extends BaseClientServiceImpl
		implements IRecbindBankctsService {

	public DEL040074Result recbindBankcts(String servNumber, String bankAcct,
			String binkId, String certType, String certId, String contractId,
			String accType) throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040074";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_servNumber = new RequestParameter("servNumber", servNumber);
		__requestData.getParams().add(__param_servNumber);
		RequestParameter __param_bankAcct = new RequestParameter("bankAcct", bankAcct);
		__requestData.getParams().add(__param_bankAcct);
		RequestParameter __param_binkId = new RequestParameter("binkId", binkId);
		__requestData.getParams().add(__param_binkId);
		RequestParameter __param_certType = new RequestParameter("certType", certType);
		__requestData.getParams().add(__param_certType);
		RequestParameter __param_certId = new RequestParameter("certId", certId);
		__requestData.getParams().add(__param_certId);
		RequestParameter __param_contractId = new RequestParameter("contractId", contractId);
		__requestData.getParams().add(__param_contractId);
		RequestParameter __param_accType = new RequestParameter("accType", accType);
		__requestData.getParams().add(__param_accType);
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
		DEL040074Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040074Result)
			{
				__result = (DEL040074Result)__ret;
			}
			else
			{
				__result = new DEL040074Result();
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