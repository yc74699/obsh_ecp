package com.xwtech.xwecp.service.logic.client_impl.common;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050008Result;

public interface IQueryMobileTypeService
{
	public QRY050008Result queryMobileType(String brandId) throws LIException;

}