package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY090100Result;

public interface IQuerySnQqhmzh
{
	public QRY090100Result querySnqqhmzh(String msisdn, String rec_prodid) throws LIException;

}