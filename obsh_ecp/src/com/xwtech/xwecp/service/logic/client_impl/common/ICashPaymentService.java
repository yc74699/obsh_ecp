package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY610046Result;

public interface ICashPaymentService {
	
	public QRY610046Result cashPayment(String ddr_city,String servnumber,String formnum,String paytype,String accesstype,String levelid,String payfee
			,String paysubtype,String payflowvalue,String startdate,String enddate)throws LIException;
}
