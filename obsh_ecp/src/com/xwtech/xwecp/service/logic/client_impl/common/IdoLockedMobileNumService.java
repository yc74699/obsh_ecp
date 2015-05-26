package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040051Result;

public interface IdoLockedMobileNumService
{
	public DEL040051Result doLockedMobileNum(String lockType, List<String> orderId) throws LIException;

}