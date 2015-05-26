package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY080001Result;

public interface ISendSmsMsgService
{
	public QRY080001Result sendSmsMsg(String mobile, String content, String spCode, String serviceId) throws LIException;

}