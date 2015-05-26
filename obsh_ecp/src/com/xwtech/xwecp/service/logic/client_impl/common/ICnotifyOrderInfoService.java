package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040107Result;

public interface ICnotifyOrderInfoService {
	   public DEL040107Result cnotifyOrderInfo(String orderId, String operId, String busifee) throws LIException;
}
