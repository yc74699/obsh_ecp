package com.xwtech.xwecp.service.logic.client_impl.common.impl;

import com.xwtech.xwecp.service.client.BaseClientServiceImpl;
import com.xwtech.xwecp.msg.RequestData;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ResponseData;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryFlowPaySeqService;
import com.xwtech.xwecp.service.logic.pojo.QRY610051Result;

/**
 * 流量账户收支流水查询
 * @author 张斌
 * 2015-4-22
 */
public class QryFlowPaySeqServiceClientImpl extends BaseClientServiceImpl implements IQryFlowPaySeqService
{
	public QRY610051Result qryFlowPaySeq(String phoneNum, String beginDate, String endDate, String qryType, String isPaging, String pagingNum, String everyPageCount) throws LIException
	{
		long __beginTime = System.currentTimeMillis();
		String __cmd = "QRY610051";
		XWECPLIClient __client = XWECPLIClient.getInstance();
		ServiceMessage __msg = __client.getMsgHelper().createRequestMessage(__cmd);
		RequestData __requestData = new RequestData();
		RequestParameter __param_phoneNum = new RequestParameter("phoneNum", phoneNum);
		__requestData.getParams().add(__param_phoneNum);
		RequestParameter __param_beginDate = new RequestParameter("beginDate", beginDate);
		__requestData.getParams().add(__param_beginDate);
		RequestParameter __param_endDate = new RequestParameter("endDate", endDate);
		__requestData.getParams().add(__param_endDate);
		RequestParameter __param_qryType = new RequestParameter("qryType", qryType);
		__requestData.getParams().add(__param_qryType);
		RequestParameter __param_isPaging = new RequestParameter("isPaging", isPaging);
		__requestData.getParams().add(__param_isPaging);
		RequestParameter __param_pagingNum = new RequestParameter("pagingNum", pagingNum);
		__requestData.getParams().add(__param_pagingNum);
		RequestParameter __param_everyPageCount = new RequestParameter("everyPageCount", everyPageCount);
		__requestData.getParams().add(__param_everyPageCount);
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
		QRY610051Result __result = null;
		String __responseXML = "";
		Throwable __errorStack = null;
		ServiceMessage __response = null;
		try
		{
//			__responseXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><operation_out>  <process_code>cc_qryflowpayseq</process_code>  <sysfunc_id>70</sysfunc_id>  <response_time>20150423110437</response_time>  <request_source>102010</request_source>  <request_seq>1_136</request_seq>  <request_type/>  <content>    <ret_code>0</ret_code>    <operating_srl/>    <page_count>1</page_count>    <list>      <seqlist>        <in_servnumber>13852320074</in_servnumber>        <in_username>dddddddd</in_username>        <out_servnumber/>        <out_username/>        <income>1</income>        <flow_value>1024</flow_value>        <deal_date need=\"0\">20150420103325</deal_date>        <remark/>      </seqlist>      <seqlist>        <in_servnumber>13852320074</in_servnumber>        <in_username>dddddddd</in_username>        <out_servnumber/>        <out_username/>        <income>1</income>        <flow_value>1024</flow_value>        <deal_date need=\"0\">20150420103707</deal_date>        <remark/>      </seqlist>      <seqlist>        <in_servnumber>13852320074</in_servnumber>        <in_username>dddddddd</in_username>        <out_servnumber/>        <out_username/>        <income>1</income>        <flow_value>1024</flow_value>        <deal_date need=\"0\">20150420111645</deal_date>        <remark/>      </seqlist>      <seqlist>        <in_servnumber>13852320074</in_servnumber>        <in_username>dddddddd</in_username>        <out_servnumber/>        <out_username/>        <income>1</income>        <flow_value>1024</flow_value>        <deal_date need=\"0\">20150420112122</deal_date>        <remark/>      </seqlist>      <seqlist>        <in_servnumber>13852320074</in_servnumber>        <in_username>dddddddd</in_username>        <out_servnumber/>        <out_username/>        <income>1</income>        <flow_value>1024</flow_value>        <deal_date need=\"0\">20150420112147</deal_date>        <remark/>      </seqlist>    </list>  </content>  <response>    <resp_type>0</resp_type>    <resp_code>0000</resp_code>    <resp_desc/>  </response></operation_out>";
			__responseXML = __client.getPlatformConnection().send(__requestXML);
			__response = ServiceMessage.fromXML(__responseXML);
			BaseServiceInvocationResult __ret = ((ResponseData)(__response.getData())).getServiceResult();
			if(__ret instanceof QRY610051Result)
			{
				__result = (QRY610051Result)__ret;
			}
			else
			{
				__result = new QRY610051Result();
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