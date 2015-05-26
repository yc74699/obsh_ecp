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
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCstudentOrderSyncService;
import com.xwtech.xwecp.service.logic.pojo.QRY610042Result;

/**
 * 在线入网订单同步CRM接口
 * @author YangXQ
 * 2015-04-14
 */
public class QueryCstudentOrderSyncServiceClientImpl extends BaseClientServiceImpl implements IQueryCstudentOrderSyncService
{
	public  QRY610042Result queryCstudentOrderSync(String 	orderid,
			String 	telnum,
			String 	studentno,
			String 	schoolno,
			String 	schoolname,
			String 	prodinfo,
			String 	linkname,
			String 	linkaddr,
			String 	linknum) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY610042";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_orderid = new RequestParameter("orderid", orderid);
		__requestData.getParams().add(__param_orderid);	
		RequestParameter __param_telnum = new RequestParameter("telnum", telnum);
		__requestData.getParams().add(__param_telnum);	
		RequestParameter __param_studentno = new RequestParameter("studentno", studentno);
		__requestData.getParams().add(__param_studentno);	
		RequestParameter __param_schoolno = new RequestParameter("schoolno", schoolno);
		__requestData.getParams().add(__param_schoolno);	
		RequestParameter __param_schoolname = new RequestParameter("schoolname", schoolname);
		__requestData.getParams().add(__param_schoolname);	
		RequestParameter __param_prodinfo = new RequestParameter("prodinfo", prodinfo);
		__requestData.getParams().add(__param_prodinfo);	
		RequestParameter __param_linkname = new RequestParameter("linkname", linkname);
		__requestData.getParams().add(__param_linkname);	
		RequestParameter __param_linkaddr = new RequestParameter("linkaddr", linkaddr);
		__requestData.getParams().add(__param_linkaddr);	
		RequestParameter __param_linknum = new RequestParameter("linknum", linknum);
		__requestData.getParams().add(__param_linknum);	
		
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
		QRY610042Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY610042Result)
			{
				__result = (QRY610042Result)__ret;
			}
			else
			{
				__result = new QRY610042Result();
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