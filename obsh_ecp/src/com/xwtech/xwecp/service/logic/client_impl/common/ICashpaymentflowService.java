package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040116Result;

public interface ICashpaymentflowService
{
	public DEL040116Result 		cashpaymentflow(String phoneNum, String amount, String paymethod, String bankcode, int payFlag,
			String formnum, String accesstype, String levelid,String paysubtype,String payflowvalue,String startdate,String enddate) throws LIException;

}