package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY210010Result;

public interface ICheckUserPackageMessagesService
{
	public QRY210010Result checkUserPackageMessages(String ddrC6ity, String customerCode, String telNumber) throws LIException;

}