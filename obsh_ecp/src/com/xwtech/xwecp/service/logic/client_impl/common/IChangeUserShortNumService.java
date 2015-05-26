package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL080001Result;

public interface IChangeUserShortNumService
{
	public DEL080001Result changeUserShortNum(String groupUserId, String memberMobileNum, String memberMobileShortNum) throws LIException;

}