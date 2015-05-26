package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY060074Result;

public interface IGetSuperPkgMemProdService
{
	public QRY060074Result getSuperChooseProd(String msisdn, String packageId, String mainprodid) throws LIException;

}