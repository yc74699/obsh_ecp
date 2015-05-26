package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IBucheckdealUnionpayService;
import com.xwtech.xwecp.service.logic.client_impl.common.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040073Result;

/**
 * 掌营银联充值业务检查接口 
 * @author YangXQ
 * 2014-6-27
 */
public class BucheckdealUnionpayServiceClientImpl extends BaseClientServiceImpl implements IBucheckdealUnionpayService{
	public QRY040073Result bucheckdealUnionpay(String user_msisdn,String clt_operating_srl,
			long ddr_city,String bu_card_id,String bu_busi_type) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY040073";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_user_msisdn = new RequestParameter("user_msisdn", user_msisdn);
		__requestData.getParams().add(__param_user_msisdn);
		RequestParameter __param_clt_operating_srl = new RequestParameter("clt_operating_srl", clt_operating_srl);
		__requestData.getParams().add(__param_clt_operating_srl);
		RequestParameter __param_ddr_city = new RequestParameter("ddr_city", ddr_city);
		__requestData.getParams().add(__param_ddr_city);
		RequestParameter __param_bu_card_id = new RequestParameter("bu_card_id", bu_card_id);
		__requestData.getParams().add(__param_bu_card_id);
		RequestParameter __param_bu_busi_type = new RequestParameter("bu_busi_type", bu_busi_type);
		__requestData.getParams().add(__param_bu_busi_type);	
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
		QRY040073Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY040073Result)
			{
				__result = (QRY040073Result)__ret;
			}
			else
			{
			__result = new QRY040073Result();
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