package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040048Result;

public interface IQueryGPSFluxInfoService {
	public QRY040048Result queryGPSFluxInfo(String phoneNum,String cycle)throws LIException;
}
