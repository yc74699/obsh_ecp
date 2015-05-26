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
import com.xwtech.xwecp.service.logic.client_impl.common.IProvChainService;
import com.xwtech.xwecp.service.logic.pojo.DEL040115Result;
import com.xwtech.xwecp.service.logic.pojo.ProvchainOperDetail;

public class ProvChainServiceClientImpl extends BaseClientServiceImpl implements IProvChainService {

	public DEL040115Result synchronousPhoneNum(String operSrl, String opType,
			String orgId, String brandId, String resTypeId, String vendorId,
			List<ProvchainOperDetail> operList) throws LIException {
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040115";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_operSrl = new RequestParameter("operSrl", operSrl);
		__requestData.getParams().add(__param_operSrl);
		RequestParameter __param_opType = new RequestParameter("opType", opType);
		__requestData.getParams().add(__param_opType);
		RequestParameter __param_orgId = new RequestParameter("orgId", orgId);
		__requestData.getParams().add(__param_orgId);
		RequestParameter __param_brandId = new RequestParameter("brandId", brandId);
		__requestData.getParams().add(__param_brandId);
		RequestParameter __param_resTypeId = new RequestParameter("resTypeId", resTypeId);
		__requestData.getParams().add(__param_resTypeId);
		RequestParameter __param_vendorId = new RequestParameter("vendorId", vendorId);
		__requestData.getParams().add(__param_vendorId);
		RequestParameter __param_operList = new RequestParameter("operList", operList);
		__requestData.getParams().add(__param_operList);
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
		DEL040115Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040115Result)
			{
				__result = (DEL040115Result)__ret;
			}
			else
			{
				__result = new DEL040115Result();
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
