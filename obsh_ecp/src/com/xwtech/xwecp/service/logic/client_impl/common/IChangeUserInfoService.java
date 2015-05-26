package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL030002Result;

public interface IChangeUserInfoService
{
	public DEL030002Result changeUserInfo(String phoneNum, String contact, String conAddr, String postCode, String custPhone, String custEmail,String icCard) throws LIException;

}