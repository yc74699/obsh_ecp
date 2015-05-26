package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGPRSGrpMemberMgr;
import com.xwtech.xwecp.service.logic.client_impl.common.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040101Result;
/**
*定向白名单
*/
public class GPRSGrpMemberMgrClientImpl extends BaseClientServiceImpl implements IGPRSGrpMemberMgr
{
	public DEL040101Result gPRSGrpMemberMgr(String SERVNUMBER, String serviceid,String opertype, String prodid, String statusdate,String cityNum) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040101";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_SERVNUMBER = new RequestParameter("SERVNUMBER", SERVNUMBER);
		__requestData.getParams().add(__param_SERVNUMBER);
		RequestParameter __param_serviceid = new RequestParameter("serviceid", serviceid);
		__requestData.getParams().add(__param_serviceid);
		RequestParameter __param_opertype = new RequestParameter("opertype", opertype);
		__requestData.getParams().add(__param_opertype);
		RequestParameter __param_prodid = new RequestParameter("prodid", prodid);
		__requestData.getParams().add(__param_prodid);
		RequestParameter __param_statusdate = new RequestParameter("statusdate", statusdate);
		__requestData.getParams().add(__param_statusdate);
		RequestParameter __param_cityNum = new RequestParameter("cityNum", cityNum);
		__requestData.getParams().add(__param_cityNum);
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
		DEL040101Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040101Result)
			{
				__result = (DEL040101Result)__ret;
			}
			else
			{
			__result = new DEL040101Result();
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