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
import com.xwtech.xwecp.service.logic.client_impl.common.ITransResourcePresaleService;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.TerminalOrderArrayBean;
import com.xwtech.xwecp.service.logic.pojo.DEL100002Result;

public class TransResourcePresaleServiceClientImpl extends BaseClientServiceImpl implements ITransResourcePresaleService
{
	public DEL100002Result transResourcePresale(String paorderid, String saletype, String opertype, String servnumber, String actid, String privid, int precount,String imeiorgid, List<TerminalOrderArrayBean> terminalOrderList) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL100002";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_paorderid = new RequestParameter("paorderid", paorderid);
		__requestData.getParams().add(__param_paorderid);
		RequestParameter __param_saletype = new RequestParameter("saletype", saletype);
		__requestData.getParams().add(__param_saletype);
		RequestParameter __param_opertype = new RequestParameter("opertype", opertype);
		__requestData.getParams().add(__param_opertype);
		RequestParameter __param_servnumber = new RequestParameter("servnumber", servnumber);
		__requestData.getParams().add(__param_servnumber);
		RequestParameter __param_actid = new RequestParameter("actid", actid);
		__requestData.getParams().add(__param_actid);
		RequestParameter __param_privid = new RequestParameter("privid", privid);
		__requestData.getParams().add(__param_privid);
		RequestParameter __param_precount = new RequestParameter("precount", precount);
		__requestData.getParams().add(__param_precount);
		RequestParameter __param_imeiorgid = new RequestParameter("imeiorgid", imeiorgid);
		__requestData.getParams().add(__param_imeiorgid);
		RequestParameter __param_terminalOrderList = new RequestParameter("terminalOrderList", terminalOrderList);
		__requestData.getParams().add(__param_terminalOrderList);
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
		DEL100002Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL100002Result)
			{
				__result = (DEL100002Result)__ret;
			}
			else
			{
				__result = new DEL100002Result();
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

	public DEL100002Result transResourcePresale(String paorderid,
			String saletype, String opertype, String servnumber, String actid,
			String privid, int precount,
			List<TerminalOrderArrayBean> terminalOrderList) throws LIException {
		return transResourcePresale(paorderid,saletype,opertype,servnumber,actid,privid,precount,"",terminalOrderList);
	}

}