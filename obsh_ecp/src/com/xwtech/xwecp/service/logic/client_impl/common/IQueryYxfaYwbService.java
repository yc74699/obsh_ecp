package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY070003Result;

/**
 * 业务包查询
 * 2014-09-25
 */
public interface IQueryYxfaYwbService
{
	public QRY070003Result queryYxfaYwb(String ddr_city, String marketingbusicfg_plan_id,String prodid,String ischeck) throws LIException;

}