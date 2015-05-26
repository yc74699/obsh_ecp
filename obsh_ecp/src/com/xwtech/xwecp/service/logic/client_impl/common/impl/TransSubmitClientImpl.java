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
import com.xwtech.xwecp.service.logic.client_impl.common.ITransSubmit;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.Cusrtemplprodt;
import com.xwtech.xwecp.service.logic.pojo.Cusrtransproddt;
import com.xwtech.xwecp.service.logic.pojo.Cusertransspdt;
import com.xwtech.xwecp.service.logic.pojo.Cusertransnoticedt;
import com.xwtech.xwecp.service.logic.pojo.DEL011004Result;

public class TransSubmitClientImpl extends BaseClientServiceImpl implements ITransSubmit
{
	public DEL011004Result transSubmit(String phoneNum, String userid, String fromCity, String toCity, String newProId, String templateNo, String newPwd, List<Cusrtemplprodt> cusrtemplprodtList, List<Cusrtransproddt> cusrtransproddtList, List<Cusertransspdt> cusertransspdtList, List<Cusertransnoticedt> cusertransnoticedtList) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL011004";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_userid = new RequestParameter("userid", userid);
		__requestData.getParams().add(__param_userid);
		RequestParameter __param_fromCity = new RequestParameter("fromCity", fromCity);
		__requestData.getParams().add(__param_fromCity);
		RequestParameter __param_toCity = new RequestParameter("toCity", toCity);
		__requestData.getParams().add(__param_toCity);
		RequestParameter __param_newProId = new RequestParameter("newProId", newProId);
		__requestData.getParams().add(__param_newProId);
		RequestParameter __param_templateNo = new RequestParameter("templateNo", templateNo);
		__requestData.getParams().add(__param_templateNo);
		RequestParameter __param_newPwd = new RequestParameter("newPwd", newPwd);
		__requestData.getParams().add(__param_newPwd);
		RequestParameter __param_cusrtemplprodtList = new RequestParameter("cusrtemplprodtList", cusrtemplprodtList);
		__requestData.getParams().add(__param_cusrtemplprodtList);
		RequestParameter __param_cusrtransproddtList = new RequestParameter("cusrtransproddtList", cusrtransproddtList);
		__requestData.getParams().add(__param_cusrtransproddtList);
		RequestParameter __param_cusertransspdtList = new RequestParameter("cusertransspdtList", cusertransspdtList);
		__requestData.getParams().add(__param_cusertransspdtList);
		RequestParameter __param_cusertransnoticedtList = new RequestParameter("cusertransnoticedtList", cusertransnoticedtList);
		__requestData.getParams().add(__param_cusertransnoticedtList);
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
		DEL011004Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL011004Result)
			{
				__result = (DEL011004Result)__ret;
			}
			else
			{
				__result = new DEL011004Result();
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