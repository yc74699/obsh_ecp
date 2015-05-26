package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040060Result;

public interface IMultiNumBlackListCloseService {
	public DEL040060Result multiNumBlackListClose(String userCity,String userSid,String vnumSid) throws LIException;
}