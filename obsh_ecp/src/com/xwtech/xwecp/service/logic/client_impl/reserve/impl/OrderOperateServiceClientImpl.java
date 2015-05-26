package com.xwtech.xwecp.service.logic.client_impl.reserve.impl;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.reserve.IOrderOperateService;
import com.xwtech.xwecp.service.logic.pojo.OrderUpdateInfo;
import com.xwtech.xwecp.service.logic.pojo.RES004Result;

public class OrderOperateServiceClientImpl extends BaseClientServiceImpl
		implements IOrderOperateService {

    //预约订单操作
	private RES004Result orderManager(String orderId, String operType,
			OrderUpdateInfo orderUpdateInfo) throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "RES004";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		
		RequestParameter __param_orderId = new RequestParameter("orderId", orderId);
		__requestData.getParams().add(__param_orderId);
		RequestParameter __param_operType = new RequestParameter("operType", operType);
		__requestData.getParams().add(__param_operType);
		RequestParameter __param_orderUpdateInfo = new RequestParameter("orderUpdateInfo", orderUpdateInfo);
		__requestData.getParams().add(__param_orderUpdateInfo);
		
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
		RES004Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof RES004Result)
			{
				__result = (RES004Result)__ret;
			}
			else
			{
			__result = new RES004Result();
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
	
    //预约订单取消 1-取消
	public RES004Result orderCancel(String orderId) throws LIException {
		return this.orderManager(orderId, "1", null);
	}

	//预约订单修改 2-修改
	public RES004Result orderUpdate(String orderId, OrderUpdateInfo orderUpdateInfo)
			throws LIException {
		return this.orderManager(orderId, "2", orderUpdateInfo);
	}

}
