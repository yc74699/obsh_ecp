package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040005Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040006Result;

public interface IPersonInfoService
{
	public QRY040005Result checkUserInfo(String phoneNum, String icType, String icNo) throws LIException;

	public QRY040006Result changeUserPass(String phoneNum, String oldPass, String newPass, String checkOld) throws LIException;

}