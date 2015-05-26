package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL011006Result;

public interface ICurrentProRecService
{
	public DEL011006Result currentProRecService(String prodId, String pstnPwd, String append) throws LIException;

}