package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040006Result;

public interface IChangeUserPassService
{
	public QRY040006Result changeUserPass(String phoneNum, String oldPass, String newPass, int checkOld, int type) throws LIException;

}