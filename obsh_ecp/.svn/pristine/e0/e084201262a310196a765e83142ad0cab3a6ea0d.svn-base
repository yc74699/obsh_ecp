package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICashpaymentflowService;
import com.xwtech.xwecp.service.logic.client_impl.common.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040116Result;

public class CashpaymentflowServiceClientImpl extends BaseClientServiceImpl implements ICashpaymentflowService
{
	public DEL040116Result 		cashpaymentflow(String phoneNum, String amount, String paymethod, String bankcode, int payFlag,
			String formnum, String accesstype, String levelid,String paysubtype,String payflowvalue,String startdate,String enddate) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040116";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_amount = new RequestParameter("amount", amount);
		__requestData.getParams().add(__param_amount);
		RequestParameter __param_paymethod = new RequestParameter("paymethod", paymethod);
		__requestData.getParams().add(__param_paymethod);
		RequestParameter __param_bankcode = new RequestParameter("bankcode", bankcode);
		__requestData.getParams().add(__param_bankcode);
		RequestParameter __param_payFlag = new RequestParameter("payFlag", payFlag);
		__requestData.getParams().add(__param_payFlag);
		RequestParameter __param_formnum = new RequestParameter("formnum", formnum);
		__requestData.getParams().add(__param_formnum);
		RequestParameter __param_accesstype = new RequestParameter("accesstype", accesstype);
		__requestData.getParams().add(__param_accesstype);
		RequestParameter __param_levelid = new RequestParameter("levelid", levelid);
		__requestData.getParams().add(__param_levelid);
		RequestParameter __param_paysubtype = new RequestParameter("paysubtype", paysubtype);
		__requestData.getParams().add(__param_paysubtype);
		RequestParameter __param_payflowvalue = new RequestParameter("payflowvalue", payflowvalue);
		__requestData.getParams().add(__param_payflowvalue);
		RequestParameter __param_startdate = new RequestParameter("startdate", startdate);
		__requestData.getParams().add(__param_startdate);
		RequestParameter __param_enddate = new RequestParameter("enddate", enddate);
		__requestData.getParams().add(__param_enddate);
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
		DEL040116Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040116Result)
			{
				__result = (DEL040116Result)__ret;
			}
			else
			{
				__result = new DEL040116Result();
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