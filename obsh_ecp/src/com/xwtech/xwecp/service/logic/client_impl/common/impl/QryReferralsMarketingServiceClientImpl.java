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
import com.xwtech.xwecp.service.logic.client_impl.common.IQryReferralsMarketingService;
import com.xwtech.xwecp.service.logic.pojo.QRY060002Result;
import com.xwtech.xwecp.service.logic.pojo.QRY060003Result;

/**
 * 营销推荐专区查询
 * @author 张斌
 * 2015-4-14
 */
public class QryReferralsMarketingServiceClientImpl extends BaseClientServiceImpl implements IQryReferralsMarketingService
{
	public QRY060003Result qryReferralsMarketing(String phoneNum, String operType, String ifParseProdList, String isNeedChk, String channelId) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY060003";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_operType = new RequestParameter("operType", operType);
		__requestData.getParams().add(__param_operType);
		RequestParameter __param_ifParseProdList = new RequestParameter("ifParseProdList", ifParseProdList);
		__requestData.getParams().add(__param_ifParseProdList);
		RequestParameter __param_isNeedChk = new RequestParameter("isNeedChk", isNeedChk);
		__requestData.getParams().add(__param_isNeedChk);
		RequestParameter __param_channelId = new RequestParameter("channelId", channelId);
		__requestData.getParams().add(__param_channelId);
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
		QRY060003Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY060003Result)
			{
				__result = (QRY060003Result)__ret;
			}
			else
			{
				__result = new QRY060003Result();
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