package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050042Result;

public interface IGetUsrTransBusiInfo
{
	public QRY050042Result getUsrTransBusiInfo(String phoneNum, String userId, String fromCity, String toCity, String proId) throws LIException;

}