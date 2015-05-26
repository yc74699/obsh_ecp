package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040022Result;

public interface IDoRevocationService
{
	public DEL040022Result doRevocation(String phoneNum) throws LIException;

}