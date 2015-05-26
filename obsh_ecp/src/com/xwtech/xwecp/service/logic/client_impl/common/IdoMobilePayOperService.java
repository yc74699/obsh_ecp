package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040052Result;
import com.xwtech.xwecp.service.logic.LIException;

public interface IdoMobilePayOperService
{
	public DEL040052Result domobilePayOper(String mobile, String oprtype, String trigamt, String drawamt, String ispost) throws LIException;

}