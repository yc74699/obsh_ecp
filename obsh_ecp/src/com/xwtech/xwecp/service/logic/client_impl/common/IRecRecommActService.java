package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL050001Result;

public interface IRecRecommActService
{
	public DEL050001Result recRecommAct(String telNum, String userseq) throws LIException;

}