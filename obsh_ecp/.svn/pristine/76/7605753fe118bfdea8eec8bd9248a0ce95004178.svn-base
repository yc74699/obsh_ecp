package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICreateOrderInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040106Result;

public class CreateOderInfoServiceClientImpl extends BaseClientServiceImpl implements ICreateOrderInfoService {

	public DEL040106Result createOrderInfo(String phoneNum, int paytype,
			String payfee, int busitype, String operid, String marketplanid,
			String goodspackid, String busipackid, String drawflag,
			String scoreclass, String smsflag, String rewardlist, String remark)
			throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040106";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", (phoneNum == null ? "" : phoneNum));
		__requestData.getParams().add(__param_phoneNum);
		
		RequestParameter __param_paytype = new RequestParameter("paytype", paytype);
		__requestData.getParams().add(__param_paytype);
		
		RequestParameter __param_payfee = new RequestParameter("payfee", payfee == null ? "" : payfee);
		__requestData.getParams().add(__param_payfee);
		
		RequestParameter __param_busitype = new RequestParameter("busitype", busitype);
		__requestData.getParams().add(__param_busitype);
		
		RequestParameter __param_operid = new RequestParameter("operid", operid == null ? "" : operid);
		__requestData.getParams().add(__param_operid);
		
		RequestParameter __param_marketplanid = new RequestParameter("marketplanid", marketplanid == null ? "" : marketplanid);
		__requestData.getParams().add(__param_marketplanid);
		
		RequestParameter __param_goodspackid = new RequestParameter("goodspackid", goodspackid == null ? "" : goodspackid);
		__requestData.getParams().add(__param_goodspackid);
		
		RequestParameter __param_busipackid = new RequestParameter("busipackid", busipackid == null ? "" : busipackid);
		__requestData.getParams().add(__param_busipackid);
		
		RequestParameter __param_drawflag = new RequestParameter("drawflag", drawflag == null ? "" : drawflag);
		__requestData.getParams().add(__param_drawflag);
		
		RequestParameter __param_scoreclass = new RequestParameter("scoreclass", scoreclass == null ? "" : scoreclass);
		__requestData.getParams().add(__param_scoreclass);
		
		RequestParameter __param_smsflag = new RequestParameter("smsflag", smsflag == null ? "" : smsflag);
		__requestData.getParams().add(__param_smsflag);
		
		RequestParameter __param_rewardlist = new RequestParameter("rewardlist", rewardlist == null ? "" : rewardlist);
		__requestData.getParams().add(__param_rewardlist);
		
		RequestParameter __param_remark = new RequestParameter("remark", remark == null ? "" : remark);
		__requestData.getParams().add(__param_remark);
		
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
		DEL040106Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040106Result)
			{
				__result = (DEL040106Result)__ret;
			}
			else
			{
			__result = new DEL040106Result();
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
