package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.EXT010001Result;
import com.xwtech.xwecp.service.logic.pojo.EXT010002Result;

public interface IGetMallMobileInfoService {
	public EXT010002Result getTableServerInfo(String phoneNum, String brandNum, String areaNum, String userId, String consume, String methodName) throws LIException;
}
