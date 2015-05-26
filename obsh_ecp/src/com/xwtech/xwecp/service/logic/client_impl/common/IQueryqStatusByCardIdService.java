package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050038Result;

public interface IQueryqStatusByCardIdService
{
	public QRY050038Result queryStatusByCardId(String certType, String certId) throws LIException;

}