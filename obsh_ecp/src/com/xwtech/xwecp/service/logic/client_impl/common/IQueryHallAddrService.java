package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050018Result;

public interface IQueryHallAddrService
{
	public QRY050018Result queryHallAddr(String city, String country) throws LIException;

}