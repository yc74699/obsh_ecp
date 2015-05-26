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
import com.xwtech.xwecp.service.logic.client_impl.common.IQuerySmallPayQryBalanceService;
import com.xwtech.xwecp.service.logic.pojo.QRY010030Result;
/**
 * 小额支付话费接口实现类
 * 
 * @author xufan
 * 2014-03-14
 */
public class QuerySmallPayQryBalanceServiceClientImpl extends BaseClientServiceImpl implements IQuerySmallPayQryBalanceService
{
	public QRY010030Result querySmallPayQryBalance(String sernumber, int querytype,String business_id,String startDate,String endDate) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY010030";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", sernumber);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_querytype = new RequestParameter("querytype", querytype);
		__requestData.getParams().add(__param_querytype);
		RequestParameter __param_business_id = new RequestParameter("business_id", business_id);
		__requestData.getParams().add(__param_business_id);
		RequestParameter __param_startDate = new RequestParameter("startDate", startDate);
		__requestData.getParams().add(__param_startDate);
		RequestParameter __param_endDate = new RequestParameter("endDate", endDate);
		__requestData.getParams().add(__param_endDate);
		
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
		QRY010030Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY010030Result)
			{
				__result = (QRY010030Result)__ret;
			}
			else
			{
				__ret = new QRY010030Result();
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