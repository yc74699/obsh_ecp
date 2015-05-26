package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY090004Result;

public interface IQueryChkProdchgNewService
{
	public QRY090004Result chkProdchg(String ddr_city, String user_id, String old_prodid, String new_prodid) throws LIException;

}