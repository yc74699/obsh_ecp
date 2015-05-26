package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010023Result;

public interface IQueryFmyNewSpandProduseService {
	public QRY010023Result queryNewFmySpandProduse(String subSid, String cycle, Integer qryType, String qrysubsid) throws LIException;
}