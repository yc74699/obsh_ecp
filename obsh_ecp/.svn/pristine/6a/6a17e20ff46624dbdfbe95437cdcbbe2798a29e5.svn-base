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
import com.xwtech.xwecp.service.logic.client_impl.common.ISumbitSiteOrderService;
import com.xwtech.xwecp.service.logic.pojo.SAL207Result;

public class SumbitSiteOrderServiceClientImpl extends BaseClientServiceImpl implements ISumbitSiteOrderService
{
	public SAL207Result sumbitSiteOrder(String oper_id, String order_id, String org_id, String certid, String certtype, String custname, String servnumber, String linkphone, String model_id, String res_price, String active_fee, String paytype, String ispaid, String recorgid, String isMarketing, String remark,String planid,String color) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "SAL207";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_oper_id = new RequestParameter("oper_id", oper_id);
		__requestData.getParams().add(__param_oper_id);
		RequestParameter __param_order_id = new RequestParameter("order_id", order_id);
		__requestData.getParams().add(__param_order_id);
		RequestParameter __param_org_id = new RequestParameter("org_id", org_id);
		__requestData.getParams().add(__param_org_id);
		RequestParameter __param_certid = new RequestParameter("certid", certid);
		__requestData.getParams().add(__param_certid);
		RequestParameter __param_certtype = new RequestParameter("certtype", certtype);
		__requestData.getParams().add(__param_certtype);
		RequestParameter __param_custname = new RequestParameter("custname", custname);
		__requestData.getParams().add(__param_custname);
		RequestParameter __param_servnumber = new RequestParameter("servnumber", servnumber);
		__requestData.getParams().add(__param_servnumber);
		RequestParameter __param_linkphone = new RequestParameter("linkphone", linkphone);
		__requestData.getParams().add(__param_linkphone);
		RequestParameter __param_model_id = new RequestParameter("model_id", model_id);
		__requestData.getParams().add(__param_model_id);
		RequestParameter __param_res_price = new RequestParameter("res_price", res_price);
		__requestData.getParams().add(__param_res_price);
		RequestParameter __param_active_fee = new RequestParameter("active_fee", active_fee);
		__requestData.getParams().add(__param_active_fee);
		RequestParameter __param_paytype = new RequestParameter("paytype", paytype);
		__requestData.getParams().add(__param_paytype);
		RequestParameter __param_ispaid = new RequestParameter("ispaid", ispaid);
		__requestData.getParams().add(__param_ispaid);
		RequestParameter __param_recorgid = new RequestParameter("recorgid", recorgid);
		__requestData.getParams().add(__param_recorgid);
		RequestParameter __param_isMarketing = new RequestParameter("isMarketing", isMarketing);
		__requestData.getParams().add(__param_isMarketing);
		RequestParameter __param_remark = new RequestParameter("remark", remark);
		__requestData.getParams().add(__param_remark);
		RequestParameter __param_planid = new RequestParameter("planid", planid);
		__requestData.getParams().add(__param_planid);
		RequestParameter __param_color = new RequestParameter("color", color);
		__requestData.getParams().add(__param_color);
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
		SAL207Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof SAL207Result)
			{
				__result = (SAL207Result)__ret;
			}
			else
			{
				__result = new SAL207Result();
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