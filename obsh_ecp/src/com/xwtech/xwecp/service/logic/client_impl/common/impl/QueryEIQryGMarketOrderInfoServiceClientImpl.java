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
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryEIQryGMarketOrderInfoService;
import com.xwtech.xwecp.service.logic.pojo.QRY010031Result;
import com.xwtech.xwecp.service.logic.pojo.QRY010032Result;
/**
 * 集团在线入网订单查询接口
 * 
 * @author xufan
 * 2014-03-31
 */
public class QueryEIQryGMarketOrderInfoServiceClientImpl extends BaseClientServiceImpl implements IQueryEIQryGMarketOrderInfoService
{
	public QRY010032Result queryEIQryGMarketOrderInfo(Map<String,Object> map) throws LIException
	{
		
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY010032";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_region = new RequestParameter("region", String.valueOf(map.get("region")));
		__requestData.getParams().add(__param_region);
		RequestParameter __param_oper_source= new RequestParameter("oper_source", String.valueOf(map.get("oper_source")));
		__requestData.getParams().add(__param_oper_source);
		RequestParameter __param_oper_id = new RequestParameter("oper_id", String.valueOf(map.get("oper_id")));
		__requestData.getParams().add(__param_oper_id);
		RequestParameter __param_page_size = new RequestParameter("page_size", String.valueOf(map.get("page_size")));
		__requestData.getParams().add(__param_page_size);
		RequestParameter __param_page_index = new RequestParameter("page_index", String.valueOf(map.get("page_index")));
		__requestData.getParams().add(__param_page_index);
		RequestParameter __param_qrycrset = new RequestParameter("qrycrset", map.get("qrycrset"));
		__requestData.getParams().add(__param_qrycrset);
		
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
		QRY010032Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY010032Result)
			{
				__result = (QRY010032Result)__ret;
			}
			else
			{
				__ret = new QRY010032Result();
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