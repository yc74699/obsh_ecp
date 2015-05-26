package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040022Result;

public interface IQueryAgentNameAndAddressService
{
	public QRY040022Result queryAgentNameAndAddressService(String phoneNum, String customerId, String city) throws LIException;

}