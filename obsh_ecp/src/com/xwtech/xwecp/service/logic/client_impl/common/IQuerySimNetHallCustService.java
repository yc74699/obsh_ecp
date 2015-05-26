package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY090015Result;

public interface IQuerySimNetHallCustService
{
	public QRY090015Result querySimNethallCust(String id_type, String mod_operating_begin_date, String mod_operating_end_date, String custcare_id, String webcustinfo_status) throws LIException;

}