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
import com.xwtech.xwecp.service.logic.client_impl.common.IPrivilegeLeftCrossService;
import com.xwtech.xwecp.service.logic.pojo.QRY040061Result;

public class PrivilegeLeftCrossServiceClientImpl extends BaseClientServiceImpl implements
		IPrivilegeLeftCrossService {

	public QRY040061Result qryLeftCross(String prodid, String prod_instance_id,
			String begin_date, String end_date) throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY040061";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_prodid = new RequestParameter("prodid", prodid);
		__requestData.getParams().add(__param_prodid);
		RequestParameter __param_prod_instance_id = new RequestParameter("prod_instance_id", prod_instance_id);
		__requestData.getParams().add(__param_prod_instance_id);
		RequestParameter __param_begin_date = new RequestParameter("begin_date", begin_date);
		__requestData.getParams().add(__param_begin_date);
		RequestParameter __param_end_date = new RequestParameter("end_date", end_date);
		__requestData.getParams().add(__param_end_date);
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
		QRY040061Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY040061Result)
			{
				__result = (QRY040061Result)__ret;
			}
			else
			{
			__result = new QRY040061Result();
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
