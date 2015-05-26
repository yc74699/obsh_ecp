package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040114Result;

public interface IQueryFmyNewSpandProduseCHService {
	public QRY040114Result queryNewFmySpandProduseCH(String subSid, String cycle, Integer qryType, String qrysubsid, String isPool) throws LIException;

}
