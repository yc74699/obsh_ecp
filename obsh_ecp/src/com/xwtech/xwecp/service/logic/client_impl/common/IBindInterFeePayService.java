package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040019Result;

public interface IBindInterFeePayService
{
	public DEL040019Result bindInterFeePay(String phoneNum, String password, String payedPhoneNum, String payedPhoneNumPwd, String interType, String dealType) throws LIException;

}