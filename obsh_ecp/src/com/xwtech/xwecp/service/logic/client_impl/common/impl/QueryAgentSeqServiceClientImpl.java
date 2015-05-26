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
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAgentSeqService;
import com.xwtech.xwecp.service.logic.pojo.QRY010026Result;

public class QueryAgentSeqServiceClientImpl extends BaseClientServiceImpl implements IQueryAgentSeqService
{
	public QRY010026Result queryAgentSeq(String id_type, String user_id, long ad_yearmonthday, long aquery_start_date, long aquery_end_date, long wfseq_type, String acctbkpayseq_undo_flag) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY010026";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_id_type = new RequestParameter("id_type", id_type);
		__requestData.getParams().add(__param_id_type);
		RequestParameter __param_user_id = new RequestParameter("user_id", user_id);
		__requestData.getParams().add(__param_user_id);
		RequestParameter __param_ad_yearmonthday = new RequestParameter("ad_yearmonthday", ad_yearmonthday);
		__requestData.getParams().add(__param_ad_yearmonthday);
		RequestParameter __param_aquery_start_date = new RequestParameter("aquery_start_date", aquery_start_date);
		__requestData.getParams().add(__param_aquery_start_date);
		RequestParameter __param_aquery_end_date = new RequestParameter("aquery_end_date", aquery_end_date);
		__requestData.getParams().add(__param_aquery_end_date);
		RequestParameter __param_wfseq_type = new RequestParameter("wfseq_type", wfseq_type);
		__requestData.getParams().add(__param_wfseq_type);
		RequestParameter __param_acctbkpayseq_undo_flag = new RequestParameter("acctbkpayseq_undo_flag", acctbkpayseq_undo_flag);
		__requestData.getParams().add(__param_acctbkpayseq_undo_flag);
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
		QRY010026Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY010026Result)
			{
				__result = (QRY010026Result)__ret;
			}
			else
			{
				__result = new QRY010026Result();
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