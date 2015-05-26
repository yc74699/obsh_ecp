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
import com.xwtech.xwecp.service.logic.client_impl.common.IScoreExchangeCoinService;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.ScoreChangeCoin;
import com.xwtech.xwecp.service.logic.pojo.DEL020003Result;

public class ScoreExchangeCoinServiceClientImpl extends BaseClientServiceImpl implements IScoreExchangeCoinService
{
	public DEL020003Result scoreExchangeCoin(String smsFlag, String scoreExchangeCatalog, String ddrCity, String scoreGsmUserId, String scoreDescription, String scoreDetailDestUserId, List<ScoreChangeCoin> scoreChangeCoins) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL020003";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_smsFlag = new RequestParameter("smsFlag", smsFlag);
		__requestData.getParams().add(__param_smsFlag);
		RequestParameter __param_scoreExchangeCatalog = new RequestParameter("scoreExchangeCatalog", scoreExchangeCatalog);
		__requestData.getParams().add(__param_scoreExchangeCatalog);
		RequestParameter __param_ddrCity = new RequestParameter("ddrCity", ddrCity);
		__requestData.getParams().add(__param_ddrCity);
		RequestParameter __param_scoreGsmUserId = new RequestParameter("scoreGsmUserId", scoreGsmUserId);
		__requestData.getParams().add(__param_scoreGsmUserId);
		RequestParameter __param_scoreDescription = new RequestParameter("scoreDescription", scoreDescription);
		__requestData.getParams().add(__param_scoreDescription);
		RequestParameter __param_scoreDetailDestUserId = new RequestParameter("scoreDetailDestUserId", scoreDetailDestUserId);
		__requestData.getParams().add(__param_scoreDetailDestUserId);
		RequestParameter __param_scoreChangeCoins = new RequestParameter("scoreChangeCoins", scoreChangeCoins);
		__requestData.getParams().add(__param_scoreChangeCoins);
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
		DEL020003Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL020003Result)
			{
				__result = (DEL020003Result)__ret;
			}
			else
			{
				__result = new DEL020003Result();
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