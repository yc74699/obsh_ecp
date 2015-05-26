package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040005Result;

public interface IPayByCardService
{
	public DEL040005Result payByCard(String fromNum, String toNum, String cardNo) throws LIException;

}