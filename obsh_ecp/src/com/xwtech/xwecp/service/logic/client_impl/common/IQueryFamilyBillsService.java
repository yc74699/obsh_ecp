package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010011Result;

public interface IQueryFamilyBillsService
{
	public QRY010011Result queryFamilyBills(int queryType, String queryMonth, String extId) throws LIException;

}