package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY610047Result;

public interface IPaymentBalanceService {
	public QRY610047Result paymentBalance(String ddr_city,String servnumber,String formnum,String paytype,String accesstype,String levelid,String payfee
			,String paysubtype,String payflowvalue,String startdate,String enddate,String taskoid)throws LIException;
}
