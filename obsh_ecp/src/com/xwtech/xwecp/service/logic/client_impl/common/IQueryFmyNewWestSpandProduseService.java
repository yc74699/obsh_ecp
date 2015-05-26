package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010029Result;

public interface IQueryFmyNewWestSpandProduseService {
	public QRY010029Result queryNewWestFmySpandProduse(String subSid, String cycle, Integer qryType, String qrysubsid) throws LIException;
}