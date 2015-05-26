package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.LIException;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetPkgMoreInfoService;
import com.xwtech.xwecp.service.logic.pojo.QRY050079Result;

/**
 * 查询用户新主题产品下增值产品树关系(校园4G专用)
 * @author 张斌
 * 2015-5-4
 */
public class GetPkgMoreInfoServiceClientImpl extends BaseClientServiceImpl implements IGetPkgMoreInfoService
{
	public QRY050079Result getPkgMoreInfo(String city, String rectype, String mobile, String prodId) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY050079";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_city = new RequestParameter("city", city);
		__requestData.getParams().add(__param_city);
		RequestParameter __param_rectype = new RequestParameter("rectype", rectype);
		__requestData.getParams().add(__param_rectype);
		RequestParameter __param_mobile = new RequestParameter("mobile", mobile);
		__requestData.getParams().add(__param_mobile);
		RequestParameter __param_prodId = new RequestParameter("prodId", prodId);
		__requestData.getParams().add(__param_prodId);
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
		QRY050079Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY050079Result)
			{
				__result = (QRY050079Result)__ret;
			}
			else
			{
				__result = new QRY050079Result();
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