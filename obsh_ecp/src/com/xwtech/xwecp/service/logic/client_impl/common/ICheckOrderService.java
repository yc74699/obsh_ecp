package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040008Result;

public interface ICheckOrderService
{
	public DEL040008Result checkOrder(String phoneNum, String checkSrl, String amount, String bossDate, int payFlag) throws LIException;

}