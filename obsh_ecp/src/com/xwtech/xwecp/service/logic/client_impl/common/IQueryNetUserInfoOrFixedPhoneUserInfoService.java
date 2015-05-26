package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040021Result;

public interface IQueryNetUserInfoOrFixedPhoneUserInfoService
{
	public QRY040021Result queryNetUserInfoOrFixedPhoneUserInfo(String phoneNum, String password, String city, String loginType) throws LIException;

}