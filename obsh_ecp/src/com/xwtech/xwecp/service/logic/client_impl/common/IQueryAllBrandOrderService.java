package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050019Result;

public interface IQueryAllBrandOrderService
{
	public QRY050019Result queryAllBrandOrder(int queryType, String queryNo, String queryCity) throws LIException;

}