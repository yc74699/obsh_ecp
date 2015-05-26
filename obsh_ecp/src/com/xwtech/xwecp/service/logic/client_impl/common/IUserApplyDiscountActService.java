package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040046Result;

public interface IUserApplyDiscountActService
{
	public DEL040046Result userApplyDiscountAct(String phoneNum, String prodid, String discount, String effectType, String expireOffset, String dealFlag) throws LIException;

}