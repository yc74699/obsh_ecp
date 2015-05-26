package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IFmyorderVerifyService;
import com.xwtech.xwecp.service.logic.pojo.DEL610040Result;
import com.xwtech.xwecp.service.logic.pojo.Deviceinfo;
import com.xwtech.xwecp.service.logic.pojo.Fmymeminfo;
import com.xwtech.xwecp.service.logic.pojo.ProductInfoSubmit;
import com.xwtech.xwecp.service.logic.pojo.Propertyinfo;
import com.xwtech.xwecp.service.logic.pojo.RWDInfo;

/**
 * 订单校验 实现
 * @author xufan
 * 2014-8-29
 *
 */
public class FmyorderVerifyServiceClientImpl extends BaseClientServiceImpl implements IFmyorderVerifyService
{
	public DEL610040Result myorderVerify(Map<Object,String> mapSubmit,List<RWDInfo> rwdinfoList, 
			List<ProductInfoSubmit> productionList, List<Deviceinfo> deviceinfoList,
			List<Propertyinfo> propertyinfoList, List<Fmymeminfo> fmymeminfoList,List<Fmymeminfo> vitualinfoList ) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "DEL610040";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();	
		
		RequestParameter __param_telnum = new RequestParameter("telnum", mapSubmit.get("telnum"));
		__requestData.getParams().add(__param_telnum);
		RequestParameter __param_totalfee = new RequestParameter("totalfee", mapSubmit.get("totalfee"));
		__requestData.getParams().add(__param_totalfee);
		RequestParameter _param_paytype = new RequestParameter("paytype",mapSubmit.get("paytype"));		
		__requestData.getParams().add(_param_paytype);
		RequestParameter __param_ispayed = new RequestParameter("ispayed", mapSubmit.get("ispayed"));
		__requestData.getParams().add(__param_ispayed);
		RequestParameter __param_taskoid = new RequestParameter("taskoid", mapSubmit.get("taskoid"));
		__requestData.getParams().add(__param_taskoid);
		RequestParameter _param_fmyprodid = new RequestParameter("fmyprodid",mapSubmit.get("fmyprodid"));
		__requestData.getParams().add(_param_fmyprodid);
		RequestParameter _param_fmyeffecttype= new RequestParameter("fmyeffecttype",mapSubmit.get("fmyeffecttype"));
		__requestData.getParams().add(_param_fmyeffecttype);
		
		//19人家庭产品
		RequestParameter _param_virtualprod = new RequestParameter("virtualprod",mapSubmit.get("virtualprod"));
		__requestData.getParams().add(_param_virtualprod);
		RequestParameter _param_virtualffecttype= new RequestParameter("virtualffecttype",mapSubmit.get("virtualffecttype"));
		__requestData.getParams().add(_param_virtualffecttype);
		
		RequestParameter __param_rwdinfoList = new RequestParameter("rwdinfoList",rwdinfoList);
		__requestData.getParams().add(__param_rwdinfoList);	
		RequestParameter __param_productionList = new RequestParameter("productionList",productionList);
		__requestData.getParams().add(__param_productionList);	
		RequestParameter __param_deviceinfoList = new RequestParameter("deviceinfoList",deviceinfoList);
		__requestData.getParams().add(__param_deviceinfoList);	
		RequestParameter __param_propertyinfoList = new RequestParameter("propertyinfoList",propertyinfoList);
		__requestData.getParams().add(__param_propertyinfoList);	
		RequestParameter __param_fmymeminfoList= new RequestParameter("fmymeminfoList",fmymeminfoList);
		__requestData.getParams().add(__param_fmymeminfoList);	
		//19人家庭V网
		RequestParameter __param_vitualinfoList= new RequestParameter("vitualinfoList",vitualinfoList);
		__requestData.getParams().add(__param_vitualinfoList);	
		
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
		DEL610040Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof DEL610040Result)
			{
				__result = (DEL610040Result)__ret;
			}
			else
			{
				__result = new DEL610040Result();
				__result.setResultCode(__ret.getResultCode());
				__result.setErrorCode(__ret.getErrorCode());
				__result.setErrorMessage(__ret.getErrorMessage());
			}
		}
		catch(Exception __e)
		{
			__errorStack = __e;
			
		}
		finally
		{
			long __endTime = System.currentTimeMillis();
			__client.log(__cmd, __client.getPlatformConnection().getRemoteURL(), __requestXML, __responseXML, __msg, __response, __beginTime, __endTime, __errorStack);
		}
		return __result;
	}

}