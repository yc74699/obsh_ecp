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
import com.xwtech.xwecp.service.logic.client_impl.common.ITransTerminalSaleChangeService;
import com.xwtech.xwecp.service.logic.pojo.DEL100003Result;

public class TransTerminalSaleChangeServiceClientImpl extends BaseClientServiceImpl implements ITransTerminalSaleChangeService
{
	public DEL100003Result transTerminalSaleChange(String functionType, String paorderid, String orderid, String terminalBrand, String terminalType, String imei1, String imei2, String opertype, String saleprice,String salestates,String imeiorgid) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL100003";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_functionType = new RequestParameter("functionType", functionType);
		__requestData.getParams().add(__param_functionType);
		RequestParameter __param_paorderid = new RequestParameter("paorderid", paorderid);
		__requestData.getParams().add(__param_paorderid);
		RequestParameter __param_orderid = new RequestParameter("orderid", orderid);
		__requestData.getParams().add(__param_orderid);
		RequestParameter __param_terminalBrand = new RequestParameter("terminalBrand", terminalBrand);
		__requestData.getParams().add(__param_terminalBrand);
		RequestParameter __param_terminalType = new RequestParameter("terminalType", terminalType);
		__requestData.getParams().add(__param_terminalType);
		RequestParameter __param_imei1 = new RequestParameter("imei1", imei1);
		__requestData.getParams().add(__param_imei1);
		RequestParameter __param_imei2 = new RequestParameter("imei2", imei2);
		__requestData.getParams().add(__param_imei2);
		RequestParameter __param_opertype = new RequestParameter("opertype", opertype);
		__requestData.getParams().add(__param_opertype);
		RequestParameter __param_saleprice = new RequestParameter("saleprice", saleprice);
		__requestData.getParams().add(__param_saleprice);
		RequestParameter __param_salestates = new RequestParameter("salestates", salestates);
		__requestData.getParams().add(__param_salestates);
		RequestParameter __param_imeiorgid = new RequestParameter("imeiorgid", imeiorgid);
		__requestData.getParams().add(__param_imeiorgid);
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
		DEL100003Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL100003Result)
			{
				__result = (DEL100003Result)__ret;
			}
			else
			{
				__result = new DEL100003Result();
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
	public DEL100003Result transTerminalSaleChange(String functionType,
			String paorderid, String orderid, String terminalBrand,
			String terminalType, String imei1, String imei2, String opertype,
			String saleprice, String salestates) throws LIException {
		return transTerminalSaleChange(functionType,paorderid,orderid,terminalBrand,terminalType,imei1,imei2,opertype,salestates,"");
	}
}