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
import com.xwtech.xwecp.service.logic.client_impl.common.IConverTinterestService;
import com.xwtech.xwecp.service.logic.pojo.DEL040079Result;
import com.xwtech.xwecp.service.logic.pojo.DEL040079Result;

/**
 * 新增余额利息转流量兑换功能
 * @author YangXQ
 * 2014-7-15
 */
public class ConverTinterestServiceClientImpl extends BaseClientServiceImpl implements IConverTinterestService
{
	public DEL040079Result converTinterest(String userid, String productid,String convertnumber, 
			String convertuserid,String formnum)  throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040079";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_userid = new RequestParameter("userid", userid);
		__requestData.getParams().add(__param_userid);
		RequestParameter __param_productid = new RequestParameter("productid", productid);
		__requestData.getParams().add(__param_productid);
		RequestParameter __param_convertnumber = new RequestParameter("convertnumber", convertnumber);
		__requestData.getParams().add(__param_convertnumber);
		RequestParameter __param_convertuserid = new RequestParameter("convertuserid", convertuserid);
		__requestData.getParams().add(__param_convertuserid);
		RequestParameter __param_formnum = new RequestParameter("formnum", formnum);
		__requestData.getParams().add(__param_formnum);		
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
		DEL040079Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040079Result)
			{
				__result = (DEL040079Result)__ret;
			}
			else
			{
				__result = new DEL040079Result();
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