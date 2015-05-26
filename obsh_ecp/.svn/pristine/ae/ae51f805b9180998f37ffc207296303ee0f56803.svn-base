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
import com.xwtech.xwecp.service.logic.client_impl.common.IChgAccsettleTypeService;
import com.xwtech.xwecp.service.logic.pojo.DEL040075Result;

public class ChgAccsettleTypeServiceClientImpl extends BaseClientServiceImpl
		implements IChgAccsettleTypeService {

	public DEL040075Result chgAccsettleType(String servNumber, String payType,
			String exchangeType, String bankAcct, String drawamt,
			String trigamt, String onlyModdtamt, String bankId,
			String drawType, String acctType,String subpaytype) throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040075";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_servNumber = new RequestParameter("servNumber", servNumber);
		__requestData.getParams().add(__param_servNumber);
		RequestParameter __param_payType = new RequestParameter("payType", payType);
		__requestData.getParams().add(__param_payType);
		RequestParameter __param_exchangeType = new RequestParameter("exchangeType", exchangeType);
		__requestData.getParams().add(__param_exchangeType);
		RequestParameter __param_bankAcct = new RequestParameter("bankAcct", bankAcct);
		__requestData.getParams().add(__param_bankAcct);
		RequestParameter __param_drawamt = new RequestParameter("drawamt", drawamt);
		__requestData.getParams().add(__param_drawamt);
		RequestParameter __param_trigamt = new RequestParameter("trigamt", trigamt);
		__requestData.getParams().add(__param_trigamt);
		RequestParameter __param_onlyModdtamt = new RequestParameter("onlyModdtamt", onlyModdtamt);
		__requestData.getParams().add(__param_onlyModdtamt);
		RequestParameter __param_bankId = new RequestParameter("bankId", bankId);
		__requestData.getParams().add(__param_bankId);
		RequestParameter __param_drawType = new RequestParameter("drawType", drawType);
		__requestData.getParams().add(__param_drawType);
		RequestParameter __param_acctType = new RequestParameter("acctType", acctType);
		__requestData.getParams().add(__param_acctType);
		RequestParameter __param_subpaytype = new RequestParameter("subpaytype", subpaytype);
		__requestData.getParams().add(__param_subpaytype);
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
		DEL040075Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040075Result)
			{
				__result = (DEL040075Result)__ret;
			}
			else
			{
				__result = new DEL040075Result();
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