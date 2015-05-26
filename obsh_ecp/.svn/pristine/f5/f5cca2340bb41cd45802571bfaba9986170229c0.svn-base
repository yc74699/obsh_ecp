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
import com.xwtech.xwecp.service.logic.client_impl.common.ICountryFamilyManageService;
import com.xwtech.xwecp.service.logic.pojo.DEL040038Result;

public class CountryFamilyManageServiceClientImpl extends BaseClientServiceImpl implements ICountryFamilyManageService
{
	public DEL040038Result countryFamilyManage(int id_type, String homeuser_id, String family_name, String family_address, String householder_name, String householder_phone, int householder_level, String family_phone, int family_broad_band, String householder_vocation, String householder_address, String communication, String notes, String create_time, String master_id, String master_service_id, String householder_phs, String family_phs) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040038";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_id_type = new RequestParameter("id_type", id_type);
		__requestData.getParams().add(__param_id_type);
		RequestParameter __param_homeuser_id = new RequestParameter("homeuser_id", homeuser_id);
		__requestData.getParams().add(__param_homeuser_id);
		RequestParameter __param_family_name = new RequestParameter("family_name", family_name);
		__requestData.getParams().add(__param_family_name);
		RequestParameter __param_family_address = new RequestParameter("family_address", family_address);
		__requestData.getParams().add(__param_family_address);
		RequestParameter __param_householder_name = new RequestParameter("householder_name", householder_name);
		__requestData.getParams().add(__param_householder_name);
		RequestParameter __param_householder_phone = new RequestParameter("householder_phone", householder_phone);
		__requestData.getParams().add(__param_householder_phone);
		RequestParameter __param_householder_level = new RequestParameter("householder_level", householder_level);
		__requestData.getParams().add(__param_householder_level);
		RequestParameter __param_family_phone = new RequestParameter("family_phone", family_phone);
		__requestData.getParams().add(__param_family_phone);
		RequestParameter __param_family_broad_band = new RequestParameter("family_broad_band", family_broad_band);
		__requestData.getParams().add(__param_family_broad_band);
		RequestParameter __param_householder_vocation = new RequestParameter("householder_vocation", householder_vocation);
		__requestData.getParams().add(__param_householder_vocation);
		RequestParameter __param_householder_address = new RequestParameter("householder_address", householder_address);
		__requestData.getParams().add(__param_householder_address);
		RequestParameter __param_communication = new RequestParameter("communication", communication);
		__requestData.getParams().add(__param_communication);
		RequestParameter __param_notes = new RequestParameter("notes", notes);
		__requestData.getParams().add(__param_notes);
		RequestParameter __param_create_time = new RequestParameter("create_time", create_time);
		__requestData.getParams().add(__param_create_time);
		RequestParameter __param_master_id = new RequestParameter("master_id", master_id);
		__requestData.getParams().add(__param_master_id);
		RequestParameter __param_master_service_id = new RequestParameter("master_service_id", master_service_id);
		__requestData.getParams().add(__param_master_service_id);
		RequestParameter __param_householder_phs = new RequestParameter("householder_phs", householder_phs);
		__requestData.getParams().add(__param_householder_phs);
		RequestParameter __param_family_phs = new RequestParameter("family_phs", family_phs);
		__requestData.getParams().add(__param_family_phs);
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
		DEL040038Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040038Result)
			{
				__result = (DEL040038Result)__ret;
			}
			else
			{
				__result = new DEL040038Result();
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