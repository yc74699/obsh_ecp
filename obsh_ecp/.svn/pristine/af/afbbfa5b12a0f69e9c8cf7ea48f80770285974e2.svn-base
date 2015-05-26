package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import java.util.List;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITpSimOpenPrdService;
import com.xwtech.xwecp.service.logic.pojo.DEL110004Result;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.SimMarketInfo;


public class TpSimOpenPrdServiceClientImpl extends BaseClientServiceImpl implements ITpSimOpenPrdService
{
	public DEL110004Result doTpSimopenPrd(String msisdn, String imsi, String custName, String custAddr, String city, String country, String icNo, String icAddr, String totalFee, String proId, String brandId, String payType, List<ProPackage> proPackages, List<ProService> proServices, List<ProIncrement> proIncrements, List<ProSelf> proSelfs, List<SimMarketInfo> simMarketInfo) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL110004";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_msisdn = new RequestParameter("msisdn", msisdn);
		__requestData.getParams().add(__param_msisdn);
		RequestParameter __param_imsi = new RequestParameter("imsi", imsi);
		__requestData.getParams().add(__param_imsi);
		RequestParameter __param_custName = new RequestParameter("custName", custName);
		__requestData.getParams().add(__param_custName);
		RequestParameter __param_custAddr = new RequestParameter("custAddr", custAddr);
		__requestData.getParams().add(__param_custAddr);
		RequestParameter __param_city = new RequestParameter("city", city);
		__requestData.getParams().add(__param_city);
		RequestParameter __param_country = new RequestParameter("country", country);
		__requestData.getParams().add(__param_country);
		RequestParameter __param_icNo = new RequestParameter("icNo", icNo);
		__requestData.getParams().add(__param_icNo);
		RequestParameter __param_icAddr = new RequestParameter("icAddr", icAddr);
		__requestData.getParams().add(__param_icAddr);
		RequestParameter __param_totalFee = new RequestParameter("totalFee", totalFee);
		__requestData.getParams().add(__param_totalFee);
		RequestParameter __param_proId = new RequestParameter("proId", proId);
		__requestData.getParams().add(__param_proId);
		RequestParameter __param_brandId = new RequestParameter("brandId", brandId);
		__requestData.getParams().add(__param_brandId);
		RequestParameter __param_payType = new RequestParameter("payType", payType);
		__requestData.getParams().add(__param_payType);
		RequestParameter __param_proPackages = new RequestParameter("proPackages", proPackages);
		__requestData.getParams().add(__param_proPackages);
		RequestParameter __param_proServices = new RequestParameter("proServices", proServices);
		__requestData.getParams().add(__param_proServices);
		RequestParameter __param_proIncrements = new RequestParameter("proIncrements", proIncrements);
		__requestData.getParams().add(__param_proIncrements);
		RequestParameter __param_proSelfs = new RequestParameter("proSelfs", proSelfs);
		__requestData.getParams().add(__param_proSelfs);
		RequestParameter __param_simMarketInfo = new RequestParameter("simMarketInfo", simMarketInfo);
		__requestData.getParams().add(__param_simMarketInfo);
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
		DEL110004Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL110004Result)
			{
				__result = (DEL110004Result)__ret;
			}
			else
			{
				__result = new DEL110004Result();
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