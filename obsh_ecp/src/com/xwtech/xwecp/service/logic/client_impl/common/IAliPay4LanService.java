package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL610039Result;

public interface IAliPay4LanService
{
	public DEL610039Result aliPay4Lan(String phoneNum, String amount, String unionPaySrl) throws LIException;

}