package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import java.util.List;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IUpdatezxrworderpicService;
import com.xwtech.xwecp.service.logic.pojo.DEL010024Result; 
import com.xwtech.xwecp.service.logic.pojo.Paralist;

/**
 * 在线入网订单实名制图片更新接口
 * @author YangXQ
 * 2014-10-24
 */
public class UpdatezxrworderpicServiceClientImpl extends BaseClientServiceImpl implements IUpdatezxrworderpicService
{
	public DEL010024Result updatezxrworderpic(String order_id,String ddr_city,String humanpic,String certpicfront,
			String certpicback,String humanpic_ext,String certpicfront_ext,String certpicback_ext) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL010024";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_ddr_city = new RequestParameter("ddr_city", ddr_city);
		__requestData.getParams().add(__param_ddr_city);
		RequestParameter __param_order_id = new RequestParameter("order_id", order_id);
		__requestData.getParams().add(__param_order_id);
		RequestParameter __param_humanpic = new RequestParameter("humanpic", humanpic);
		__requestData.getParams().add(__param_humanpic);
		RequestParameter __param_certpicfront = new RequestParameter("certpicfront", certpicfront);
		__requestData.getParams().add(__param_certpicfront);
		RequestParameter __param_certpicback = new RequestParameter("certpicback", certpicback);
		__requestData.getParams().add(__param_certpicback);
		RequestParameter __param_humanpic_ext = new RequestParameter("humanpic_ext", humanpic_ext);
		__requestData.getParams().add(__param_humanpic_ext);
		RequestParameter __param_certpicfront_ext = new RequestParameter("certpicfront_ext", certpicfront_ext);
		__requestData.getParams().add(__param_certpicfront_ext);
		RequestParameter __param_certpicback_ext = new RequestParameter("certpicback_ext", certpicback_ext);
		__requestData.getParams().add(__param_certpicback_ext);
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
		DEL010024Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL010024Result)
			{
				__result = (DEL010024Result)__ret;
			}
			else
			{
				__result = new DEL010024Result();
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