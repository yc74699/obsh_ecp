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
import com.xwtech.xwecp.service.logic.client_impl.common.IOrderMarketActService;
import java.util.List;
import com.xwtech.xwecp.service.logic.pojo.UserMarketBInfo;
import com.xwtech.xwecp.service.logic.pojo.DEL040040Result;

public class OrderMarketActServiceClientImpl extends BaseClientServiceImpl implements IOrderMarketActService
{
	public DEL040040Result orderMarketAct(String servicesType, String phoneNum, String effectFlag, String bossMmsPackId, int smsFlag, int status, int idType, int receiveType, String gradeAmount, String busiPackId, List<UserMarketBInfo> userMarketBaseInfo,String rewardList) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL040040";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_servicesType = new RequestParameter("servicesType", servicesType);
		__requestData.getParams().add(__param_servicesType);
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_effectFlag = new RequestParameter("effectFlag", effectFlag);
		__requestData.getParams().add(__param_effectFlag);
		RequestParameter __param_smsFlag = new RequestParameter("smsFlag", smsFlag);
		__requestData.getParams().add(__param_smsFlag);
		RequestParameter __param_status = new RequestParameter("status", status);
		__requestData.getParams().add(__param_status);
		RequestParameter __param_idType = new RequestParameter("idType", idType);
		__requestData.getParams().add(__param_idType);
		RequestParameter __param_receiveType = new RequestParameter("receiveType", receiveType);
		__requestData.getParams().add(__param_receiveType);
		RequestParameter __param_gradeAmount = new RequestParameter("gradeAmount", gradeAmount);
		__requestData.getParams().add(__param_gradeAmount);
		RequestParameter __param_userMarketBaseInfo = new RequestParameter("userMarketBaseInfo", userMarketBaseInfo);
		__requestData.getParams().add(__param_userMarketBaseInfo);
		/**--注意点是，如果rewardlist不为空，原来的支持单个奖品传入的参数:bossmms_pack_id 和 marketingbusiinfo_busi_pack_id 必须为空*/
		RequestParameter __param_rewardList = new RequestParameter("rewardList", rewardList);
		__requestData.getParams().add(__param_rewardList);
		RequestParameter __param_busiPackId = new RequestParameter("busiPackId", busiPackId);		
//		if(null != rewardList && !"".equals(rewardList))
//		{
//			__param_busiPackId = new RequestParameter("busiPackId", "");;
//		}
		__requestData.getParams().add(__param_busiPackId);
		RequestParameter __param_bossMmsPackId = new RequestParameter("bossMmsPackId", bossMmsPackId);
//		if(null != rewardList && !"".equals(rewardList))
//		{
//			__param_bossMmsPackId=new RequestParameter("bossMmsPackId", "");;
//		}
		__requestData.getParams().add(__param_bossMmsPackId);
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
		DEL040040Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL040040Result)
			{
				__result = (DEL040040Result)__ret;
			}
			else
			{
				__result = new DEL040040Result();
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