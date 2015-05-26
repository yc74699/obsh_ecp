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
import com.xwtech.xwecp.service.logic.client_impl.common.ITransBalanceService;
import com.xwtech.xwecp.service.logic.pojo.DEL040030Result;

public class TransBalanceServiceClientImpl extends BaseClientServiceImpl implements ITransBalanceService
{
	public DEL040030Result transBalance(String formnum, String from_acctid, String from_userid, String from_msisdn, String from_subjectid, String to_acctid, String to_subjectid, String subsid, String msisdn, long transfee, String rectype, String accesstype, String paytype, String deal_type) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040030";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_formnum = new RequestParameter("formnum", formnum);
		__requestData.getParams().add(__param_formnum);
		RequestParameter __param_from_acctid = new RequestParameter("from_acctid", from_acctid);
		__requestData.getParams().add(__param_from_acctid);
		RequestParameter __param_from_userid = new RequestParameter("from_userid", from_userid);
		__requestData.getParams().add(__param_from_userid);
		RequestParameter __param_from_msisdn = new RequestParameter("from_msisdn", from_msisdn);
		__requestData.getParams().add(__param_from_msisdn);
		RequestParameter __param_from_subjectid = new RequestParameter("from_subjectid", from_subjectid);
		__requestData.getParams().add(__param_from_subjectid);
		RequestParameter __param_to_acctid = new RequestParameter("to_acctid", to_acctid);
		__requestData.getParams().add(__param_to_acctid);
		RequestParameter __param_to_subjectid = new RequestParameter("to_subjectid", to_subjectid);
		__requestData.getParams().add(__param_to_subjectid);
		RequestParameter __param_subsid = new RequestParameter("subsid", subsid);
		__requestData.getParams().add(__param_subsid);
		RequestParameter __param_msisdn = new RequestParameter("msisdn", msisdn);
		__requestData.getParams().add(__param_msisdn);
		RequestParameter __param_transfee = new RequestParameter("transfee", transfee);
		__requestData.getParams().add(__param_transfee);
		RequestParameter __param_rectype = new RequestParameter("rectype", rectype);
		__requestData.getParams().add(__param_rectype);
		RequestParameter __param_accesstype = new RequestParameter("accesstype", accesstype);
		__requestData.getParams().add(__param_accesstype);
		RequestParameter __param_paytype = new RequestParameter("paytype", paytype);
		__requestData.getParams().add(__param_paytype);
		RequestParameter __param_deal_type = new RequestParameter("deal_type", deal_type);
		__requestData.getParams().add(__param_deal_type);
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
		DEL040030Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040030Result)
			{
				__result = (DEL040030Result)__ret;
			}
			else
			{
				__result = new DEL040030Result();
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