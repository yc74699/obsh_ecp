package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL010017Result;

public interface IFNConfrimService
{
	public DEL010017Result fnConfrim(String phoneNum, String isConfirm) throws LIException;

}