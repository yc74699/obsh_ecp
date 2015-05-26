package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IFlowTransService;
import com.xwtech.xwecp.service.logic.pojo.QRY610050Result;

/**
 * 流量转移分发接口
 * @author 张斌
 * 2015-4-22
 */
public class FlowTransServiceClientImpl extends BaseClientServiceImpl implements IFlowTransService
{
	public QRY610050Result flowTrans(String phoneNum, String formNum, String frozeSrl, String toServNumber, String accessType, String paySubType, String flowValue, String marketId) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY610050";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_formNum = new RequestParameter("formNum", formNum);
		__requestData.getParams().add(__param_formNum);
		RequestParameter __param_frozeSrl = new RequestParameter("frozeSrl", frozeSrl);
		__requestData.getParams().add(__param_frozeSrl);
		RequestParameter __param_toServNumber = new RequestParameter("toServNumber", toServNumber);
		__requestData.getParams().add(__param_toServNumber);
		RequestParameter __param_accessType = new RequestParameter("accessType", accessType);
		__requestData.getParams().add(__param_accessType);
		RequestParameter __param_paySubType = new RequestParameter("paySubType", paySubType);
		__requestData.getParams().add(__param_paySubType);
		RequestParameter __param_flowValue = new RequestParameter("flowValue", flowValue);
		__requestData.getParams().add(__param_flowValue);
		RequestParameter __param_marketId = new RequestParameter("marketId", marketId);
		__requestData.getParams().add(__param_marketId);
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
		QRY610050Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
//			__responseXML = "<?xml version=\"1.0\" encoding=\"GBK\"?><operation_out><process_code>cc_flowtrans</process_code><sysfunc_id>70</sysfunc_id><response_time>20150421172205</response_time><response_seq>1_136</response_seq><request_type/><content><ret_code>0</ret_code><operating_srl>20150421172136747285</operating_srl></content><response><resp_type>0</resp_type><resp_code>0000</resp_code><resp_desc/></response></operation_out>";
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY610050Result)
			{
				__result = (QRY610050Result)__ret;
			}
			else
			{
				__result = new QRY610050Result();
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