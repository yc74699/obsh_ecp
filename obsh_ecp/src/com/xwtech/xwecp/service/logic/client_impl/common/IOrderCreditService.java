package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040114Result;

public interface IOrderCreditService {
	
	public DEL040114Result orderCredit(String msisdn , String city) throws LIException;
}
