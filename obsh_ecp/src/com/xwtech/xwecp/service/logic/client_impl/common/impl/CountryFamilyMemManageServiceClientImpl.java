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
import com.xwtech.xwecp.service.logic.client_impl.common.ICountryFamilyMemManageService;
import com.xwtech.xwecp.service.logic.pojo.DEL040037Result;

public class CountryFamilyMemManageServiceClientImpl extends BaseClientServiceImpl implements ICountryFamilyMemManageService
{
	public DEL040037Result countryFamilyMemManage(int idType, String oid, String homeUserId, String servNumber, int isHouseHold, String notes, String operId, String createTime, String houseHolderName, String houseHolderPhs, String houseHolderVocation, String houseHolderAddress) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040037";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_idType = new RequestParameter("idType", idType);
		__requestData.getParams().add(__param_idType);
		RequestParameter __param_oid = new RequestParameter("oid", oid);
		__requestData.getParams().add(__param_oid);
		RequestParameter __param_homeUserId = new RequestParameter("homeUserId", homeUserId);
		__requestData.getParams().add(__param_homeUserId);
		RequestParameter __param_servNumber = new RequestParameter("servNumber", servNumber);
		__requestData.getParams().add(__param_servNumber);
		RequestParameter __param_isHouseHold = new RequestParameter("isHouseHold", isHouseHold);
		__requestData.getParams().add(__param_isHouseHold);
		RequestParameter __param_notes = new RequestParameter("notes", notes);
		__requestData.getParams().add(__param_notes);
		RequestParameter __param_operId = new RequestParameter("operId", operId);
		__requestData.getParams().add(__param_operId);
		RequestParameter __param_createTime = new RequestParameter("createTime", createTime);
		__requestData.getParams().add(__param_createTime);
		RequestParameter __param_houseHolderName = new RequestParameter("houseHolderName", houseHolderName);
		__requestData.getParams().add(__param_houseHolderName);
		RequestParameter __param_houseHolderPhs = new RequestParameter("houseHolderPhs", houseHolderPhs);
		__requestData.getParams().add(__param_houseHolderPhs);
		RequestParameter __param_houseHolderVocation = new RequestParameter("houseHolderVocation", houseHolderVocation);
		__requestData.getParams().add(__param_houseHolderVocation);
		RequestParameter __param_houseHolderAddress = new RequestParameter("houseHolderAddress", houseHolderAddress);
		__requestData.getParams().add(__param_houseHolderAddress);
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
		DEL040037Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040037Result)
			{
				__result = (DEL040037Result)__ret;
			}
			else
			{
				__result = new DEL040037Result();
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