package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY070001Result;

/**
 * 查询营销方案列表 
 * 2014 - 09 -24
 */
public interface IQueryYxfaList1Service
{
	public QRY070001Result queryYxfaList1(String ddr_city) throws LIException;

}