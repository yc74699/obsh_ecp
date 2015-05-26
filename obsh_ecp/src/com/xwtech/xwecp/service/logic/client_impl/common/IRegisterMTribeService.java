package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.TransferTribe;
import com.xwtech.xwecp.service.logic.pojo.DEL010002Result;

public interface IRegisterMTribeService
{
	public DEL010002Result registerMTribe(String phoneNum, List<TransferTribe> tribeList) throws LIException;

}