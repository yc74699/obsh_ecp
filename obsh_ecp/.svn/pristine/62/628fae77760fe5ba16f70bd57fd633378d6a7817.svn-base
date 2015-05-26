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
import com.xwtech.xwecp.service.logic.client_impl.common.ICustomDataSubmitService;

import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.DEL011002Result;

public class CustomDataSubmitServiceClientImpl extends BaseClientServiceImpl implements ICustomDataSubmitService
{
	public DEL011002Result customDataSubmit(String phoneNum, String custName, String city, String country, String icNo, String icAddr, String postNo, String postAddr, String phoneCall, String siteId, String siteName, String siteAddr, String fetchType, String totalFee, String emsNo, String proId, String brandId, String payType, List<ProPackage> proPackages, List<ProService> proServices, List<ProIncrement> proIncrements, List<ProSelf> proSelfs, String marketId, String marketLevelId, String busiPackId,String busiGoodsId, String marketfee,String recId,String bookingId) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL011002";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_custName = new RequestParameter("custName", custName);
		__requestData.getParams().add(__param_custName);
		RequestParameter __param_city = new RequestParameter("city", city);
		__requestData.getParams().add(__param_city);
		RequestParameter __param_country = new RequestParameter("country", country);
		__requestData.getParams().add(__param_country);
		RequestParameter __param_icNo = new RequestParameter("icNo", icNo);
		__requestData.getParams().add(__param_icNo);
		RequestParameter __param_icAddr = new RequestParameter("icAddr", icAddr);
		__requestData.getParams().add(__param_icAddr);
		RequestParameter __param_postNo = new RequestParameter("postNo", postNo);
		__requestData.getParams().add(__param_postNo);
		RequestParameter __param_postAddr = new RequestParameter("postAddr", postAddr);
		__requestData.getParams().add(__param_postAddr);
		RequestParameter __param_phoneCall = new RequestParameter("phoneCall", phoneCall);
		__requestData.getParams().add(__param_phoneCall);
		RequestParameter __param_siteId = new RequestParameter("siteId", siteId);
		__requestData.getParams().add(__param_siteId);
		RequestParameter __param_siteName = new RequestParameter("siteName", siteName);
		__requestData.getParams().add(__param_siteName);
		RequestParameter __param_siteAddr = new RequestParameter("siteAddr", siteAddr);
		__requestData.getParams().add(__param_siteAddr);
		RequestParameter __param_fetchType = new RequestParameter("fetchType", fetchType);
		__requestData.getParams().add(__param_fetchType);
		RequestParameter __param_totalFee = new RequestParameter("totalFee", totalFee);
		__requestData.getParams().add(__param_totalFee);
		RequestParameter __param_emsNo = new RequestParameter("emsNo", emsNo);
		__requestData.getParams().add(__param_emsNo);
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
		RequestParameter __param_marketId = new RequestParameter("marketId", marketId);
		__requestData.getParams().add(__param_marketId);
		RequestParameter __param_marketLevelId = new RequestParameter("marketLevelId", marketLevelId);
		__requestData.getParams().add(__param_marketLevelId);
		RequestParameter __param_marketfee = new RequestParameter("marketfee", marketfee);
		__requestData.getParams().add(__param_marketfee);
		RequestParameter __param_busiPackId = new RequestParameter("busiPackId", busiPackId);
		__requestData.getParams().add(__param_busiPackId);
		RequestParameter __param_busiGoodsId = new RequestParameter("busiGoodsId", busiGoodsId);
		__requestData.getParams().add(__param_busiGoodsId);
		RequestParameter __param_recId = new RequestParameter("recId", recId);
		__requestData.getParams().add(__param_recId);
		RequestParameter __param_bookingId = new RequestParameter("bookingId", bookingId);
		__requestData.getParams().add(__param_bookingId);
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
		DEL011002Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL011002Result)
			{
				__result = (DEL011002Result)__ret;
			}
			else
			{
				__result = new DEL011002Result();
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