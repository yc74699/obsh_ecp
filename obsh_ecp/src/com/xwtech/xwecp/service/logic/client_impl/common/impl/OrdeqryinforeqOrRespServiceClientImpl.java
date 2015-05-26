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
import com.xwtech.xwecp.service.logic.client_impl.common.IOrdeqryinforeqOrRespService;
import com.xwtech.xwecp.service.logic.pojo.QRY010040Result;
/**
 * 集团在线入网订单查询接口
 * 
 * @author xufan
 * 2014-03-31
 */
public class OrdeqryinforeqOrRespServiceClientImpl extends BaseClientServiceImpl implements IOrdeqryinforeqOrRespService
{
	public QRY010040Result ordeqryinforeqOrResp(Map<String,Object> map) throws LIException
	{
		
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY010040";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_telnum = new RequestParameter("telnum", String.valueOf(map.get("telnum")));
		__requestData.getParams().add(__param_telnum);
		RequestParameter __param_status= new RequestParameter("status", String.valueOf(map.get("status")));
		__requestData.getParams().add(__param_status);
		RequestParameter __param_startdate = new RequestParameter("startdate", String.valueOf(map.get("startdate")));
		__requestData.getParams().add(__param_startdate);
		RequestParameter __param_enddate = new RequestParameter("enddate", String.valueOf(map.get("enddate")));
		__requestData.getParams().add(__param_enddate);
		RequestParameter __param_orderid = new RequestParameter("orderid", String.valueOf(map.get("orderid")));
		__requestData.getParams().add(__param_orderid);
		RequestParameter __param_orderregion = new RequestParameter("orderregion", map.get("orderregion"));
		__requestData.getParams().add(__param_orderregion);
		RequestParameter __param_flag = new RequestParameter("flag", map.get("flag"));
		__requestData.getParams().add(__param_flag);
		
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
		QRY010040Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY010040Result)
			{
				__result = (QRY010040Result)__ret;
			}
			else
			{
				__ret = new QRY010040Result();
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