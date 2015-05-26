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
import com.xwtech.xwecp.service.logic.client_impl.common.ICnetInstallPrd;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.CwebCustInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebPackageInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebServiceInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebIncrementInfoBean;
import com.xwtech.xwecp.service.logic.pojo.CwebSelfPlatinfoBean;
import com.xwtech.xwecp.service.logic.pojo.DEL011005Result;

public class CnetInstallPrdClientImpl extends BaseClientServiceImpl implements ICnetInstallPrd
{
	public DEL011005Result transactBroadBand(String ddrCity, List<CwebCustInfoBean> cwebCustInfoList, List<CwebPackageInfoBean> cwebPackageInfoList, List<CwebServiceInfoBean> cwebServiceInfoList, List<CwebIncrementInfoBean> cwebIncrementInfoList, List<CwebSelfPlatinfoBean> cwebSelfPlatinfoList) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL011005";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_ddrCity = new RequestParameter("ddrCity", ddrCity);
		__requestData.getParams().add(__param_ddrCity);
		RequestParameter __param_cwebCustInfoList = new RequestParameter("cwebCustInfoList", cwebCustInfoList);
		__requestData.getParams().add(__param_cwebCustInfoList);
		RequestParameter __param_cwebPackageInfoList = new RequestParameter("cwebPackageInfoList", cwebPackageInfoList);
		__requestData.getParams().add(__param_cwebPackageInfoList);
		RequestParameter __param_cwebServiceInfoList = new RequestParameter("cwebServiceInfoList", cwebServiceInfoList);
		__requestData.getParams().add(__param_cwebServiceInfoList);
		RequestParameter __param_cwebIncrementInfoList = new RequestParameter("cwebIncrementInfoList", cwebIncrementInfoList);
		__requestData.getParams().add(__param_cwebIncrementInfoList);
		RequestParameter __param_cwebSelfPlatinfoList = new RequestParameter("cwebSelfPlatinfoList", cwebSelfPlatinfoList);
		__requestData.getParams().add(__param_cwebSelfPlatinfoList);
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
		DEL011005Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL011005Result)
			{
				__result = (DEL011005Result)__ret;
			}
			else
			{
				__result = new DEL011005Result();
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