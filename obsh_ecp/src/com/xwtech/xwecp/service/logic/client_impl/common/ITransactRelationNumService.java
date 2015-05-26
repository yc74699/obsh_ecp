package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040003Result;

public interface ITransactRelationNumService
{
	public DEL040003Result transactRelationNum(String mainNum, String subNum, int oprType, int chooseFlag, String offerType) throws LIException;

}