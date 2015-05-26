package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY070004Result;

/**
 * 奖品包查询
 * 2014 - 09 -24
 */
public interface IQueryYxfaLpbService
{
	public QRY070004Result queryYxfaLpb(String ddr_city, String marketingplan_plan_id,String prodid, String ischeck) throws LIException;

}