package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040001Result;

public interface IExtFeeExpireService
{
	public DEL040001Result extFeeExpire(String phoneNum, int addDate) throws LIException;

}