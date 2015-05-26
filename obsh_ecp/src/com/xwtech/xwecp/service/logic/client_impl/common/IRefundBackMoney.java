package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL610030Result;

public interface IRefundBackMoney
{
	public DEL610030Result refundBackMoney(String servnumber, String orderid) throws LIException;

}