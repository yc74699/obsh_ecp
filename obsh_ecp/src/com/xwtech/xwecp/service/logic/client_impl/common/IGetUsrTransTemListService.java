package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050051Result;

public interface IGetUsrTransTemListService
{
	public QRY050051Result getUsrTransTemList(String userId, String proId, String fromCity) throws LIException;

}