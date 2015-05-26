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
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryDdbQqueryService;
import com.xwtech.xwecp.service.logic.pojo.QRY010037Result;
/**
 * 分布式清单查询
 * @author xufan
 * 2014-06-11
 */
public class QueryDdbQqueryServiceClientImpl extends BaseClientServiceImpl implements IQueryDdbQqueryService
{
	public QRY010037Result qryDdbQquery(Map<String,Object> map,List<Map<String,Object>>paramList) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY010037";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_user_id = new RequestParameter("user_id", map.get("user_id"));
		__requestData.getParams().add(__param_user_id);
		RequestParameter __param_begin_day = new RequestParameter("begin_day", map.get("begin_day"));
		__requestData.getParams().add(__param_begin_day);
		RequestParameter __param_end_day = new RequestParameter("end_day", map.get("end_day"));
		__requestData.getParams().add(__param_end_day);
		RequestParameter __param_qry_no = new RequestParameter("qry_no", map.get("qry_no"));
		__requestData.getParams().add(__param_qry_no);
		RequestParameter __param_page_num = new RequestParameter("page_num", map.get("page_num"));
		__requestData.getParams().add(__param_page_num);
		RequestParameter __param_key = new RequestParameter("key", map.get("key"));
		__requestData.getParams().add(__param_key);
		RequestParameter __param_is_detect = new RequestParameter("is_detect", map.get("is_detect"));
		__requestData.getParams().add(__param_is_detect);
		RequestParameter __param_req_channel = new RequestParameter("req_channel",map.get("req_channel"));
		__requestData.getParams().add(__param_req_channel);
		RequestParameter __param_paramList = new RequestParameter("paramList",paramList);
		__requestData.getParams().add(__param_paramList);
		for(int i=0;i<paramList.size();i++){
			if("cdr_type".equals(paramList.get(i).get("param_id"))){
				RequestParameter __param_cdr_type = new RequestParameter("cdr_type",String.valueOf(paramList.get(i).get("param_value")));
				__requestData.getParams().add(__param_cdr_type);
			}
		}
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
		QRY010037Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
			
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY010037Result)
			{
				__result = (QRY010037Result)__ret;
			}
			else
			{
				__result = new QRY010037Result();
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