package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.B2B001Result;
import com.xwtech.xwecp.service.logic.LIException;

public interface IB2BQueryDisOrderService
{
	public B2B001Result queryDisOrder(String region,String operId,String queryType,String storeId, String partnerId,String status,String qrySize,String qryIndxe) throws LIException;

}