package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY050061Result;
import com.xwtech.xwecp.service.logic.LIException;

public interface ICountryFamilyQueryService
{
	public QRY050061Result countryFamilyQuery(String householder_phone, String master_id) throws LIException;

}