package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040062Result;

public interface IChangeNumService {
	public QRY040062Result checkOut(String iccSerial, String puk2) throws LIException;
}
