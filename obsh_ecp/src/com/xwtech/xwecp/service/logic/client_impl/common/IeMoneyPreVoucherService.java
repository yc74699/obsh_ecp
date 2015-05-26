package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040039Result;
import com.xwtech.xwecp.service.logic.LIException;

public interface IeMoneyPreVoucherService
{
	public DEL040039Result eMoneyPreVoucher(String phoneNum, String amount, String payType, String recOid, String taskOid) throws LIException;

}