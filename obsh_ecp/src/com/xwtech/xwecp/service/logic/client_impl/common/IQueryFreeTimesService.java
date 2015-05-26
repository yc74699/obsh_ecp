package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY050067Result;

public interface IQueryFreeTimesService {
	
	public QRY050067Result getLeftFreeTimes(String phoneNum,String city) throws Exception;

}
