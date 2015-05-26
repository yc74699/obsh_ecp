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
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryYxfaYwbService;
import com.xwtech.xwecp.service.logic.pojo.QRY070003Result;

/**
 * 业务包查询
 * 2014-09-25
 */
public class QueryYxfaYwbServiceClientImpl extends BaseClientServiceImpl implements IQueryYxfaYwbService
{
	public QRY070003Result queryYxfaYwb(String ddr_city, String marketingbusicfg_plan_id,String prodid,String ischeck) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY070003";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_ddr_city = new RequestParameter("ddr_city", ddr_city);
		__requestData.getParams().add(__param_ddr_city);
		RequestParameter __param_marketingbusicfg_plan_id = new RequestParameter("marketingbusicfg_plan_id", marketingbusicfg_plan_id);
		__requestData.getParams().add(__param_marketingbusicfg_plan_id);
		RequestParameter __param_prodid = new RequestParameter("prodid", prodid);
		__requestData.getParams().add(__param_prodid);
		RequestParameter __param_ischeck = new RequestParameter("ischeck", ischeck);
		__requestData.getParams().add(__param_ischeck);
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
		QRY070003Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY070003Result)
			{
				__result = (QRY070003Result)__ret;
			}
			else
			{
				__result = new QRY070003Result();
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