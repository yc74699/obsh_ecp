package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.EXT010001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY060075Result;

public interface IGetTableServerInfoService {
	public EXT010001Result getTableServerInfo(String phoneNum, String brandNum, String areaNum, String userId, String consume, String methodName, String placeId, String channelId) throws LIException;
}
