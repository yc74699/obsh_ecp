package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040058Result;

public interface IMultiNumChgStatusService {
	public DEL040058Result multiNumChgStatus(String userCity,String userSid,String vnumSid,String operType,String startTime,String endTime) throws LIException;
}
