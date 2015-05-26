package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040105Result;

public interface ICuserFundNew
{
	public DEL040105Result cuserFundNew(String cityId, String phoneNum, String amount,int isSendMsg) throws LIException;

}