package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY200003Result;

public interface IQueryFamilyProdService
{
	public QRY200003Result queryFamilyProdService(String familysubsid) throws LIException;

}