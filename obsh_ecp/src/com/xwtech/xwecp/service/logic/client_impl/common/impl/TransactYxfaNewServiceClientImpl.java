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
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactYxfaNewService;
import com.xwtech.xwecp.service.logic.pojo.DEL090001Result;

public class TransactYxfaNewServiceClientImpl extends BaseClientServiceImpl implements ITransactYxfaNewService
{
	public DEL090001Result transactYxfaNew(String bossmms_services_type, String ddr_city, String usermarketingbaseinfo_user_id, int id_type, String detail_operating_srl, String marketingbusiinfo_busi_pack_id, String bossmms_pack_id, String creditreleasegrade_grade_amount, String usermarketingbaseinfo_plan_id, String bossmarket_rec_type) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL090001";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_bossmms_services_type = new RequestParameter("bossmms_services_type", bossmms_services_type);
		__requestData.getParams().add(__param_bossmms_services_type);
		RequestParameter __param_ddr_city = new RequestParameter("ddr_city", ddr_city);
		__requestData.getParams().add(__param_ddr_city);
		RequestParameter __param_usermarketingbaseinfo_user_id = new RequestParameter("usermarketingbaseinfo_user_id", usermarketingbaseinfo_user_id);
		__requestData.getParams().add(__param_usermarketingbaseinfo_user_id);
		RequestParameter __param_id_type = new RequestParameter("id_type", id_type);
		__requestData.getParams().add(__param_id_type);
		RequestParameter __param_detail_operating_srl = new RequestParameter("detail_operating_srl", detail_operating_srl);
		__requestData.getParams().add(__param_detail_operating_srl);
		RequestParameter __param_marketingbusiinfo_busi_pack_id = new RequestParameter("marketingbusiinfo_busi_pack_id", marketingbusiinfo_busi_pack_id);
		__requestData.getParams().add(__param_marketingbusiinfo_busi_pack_id);
		RequestParameter __param_bossmms_pack_id = new RequestParameter("bossmms_pack_id", bossmms_pack_id);
		__requestData.getParams().add(__param_bossmms_pack_id);
		RequestParameter __param_creditreleasegrade_grade_amount = new RequestParameter("creditreleasegrade_grade_amount", creditreleasegrade_grade_amount);
		__requestData.getParams().add(__param_creditreleasegrade_grade_amount);
		RequestParameter __param_usermarketingbaseinfo_plan_id = new RequestParameter("usermarketingbaseinfo_plan_id", usermarketingbaseinfo_plan_id);
		__requestData.getParams().add(__param_usermarketingbaseinfo_plan_id);
		RequestParameter __param_bossmarket_rec_type = new RequestParameter("bossmarket_rec_type", bossmarket_rec_type);
		__requestData.getParams().add(__param_bossmarket_rec_type);
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
		DEL090001Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL090001Result)
			{
				__result = (DEL090001Result)__ret;
			}
			else
			{
				__result = new DEL090001Result();
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