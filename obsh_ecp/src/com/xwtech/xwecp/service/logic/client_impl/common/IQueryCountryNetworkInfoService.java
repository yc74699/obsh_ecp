package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY050068Result;

public interface IQueryCountryNetworkInfoService {
	public QRY050068Result queryCouNetInfo(String region,String countryId)throws LIException;
	
	public QRY050068Result queryCouNetInfo(String region,String countryId,String name)throws LIException;
	
	public QRY050068Result queryCouNetInfo(String custId)throws LIException;
}
