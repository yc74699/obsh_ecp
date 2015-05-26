package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040021Result;

public interface ICommonTransactMonternetService
{
	public DEL040021Result commonTransactMonternet(String phoneNum, String bizCode, String spId, String bizType) throws LIException;

}