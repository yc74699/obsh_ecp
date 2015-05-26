package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY050058Result;

public interface IQueryOrderInfoABCService
{
	public QRY050058Result queryABCOrderInfo(String region, String qryhis, String qryvalue) throws LIException;

}