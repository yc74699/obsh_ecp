package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL050004Result;

public interface IUpdHomememBeraux
{
	public DEL050004Result updHomememBeraux(String familysubsid, String subid, String memberPhone, String returnType, String payType) throws LIException;

}