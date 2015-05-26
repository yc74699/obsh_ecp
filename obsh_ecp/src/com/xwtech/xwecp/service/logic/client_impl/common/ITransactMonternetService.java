package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.List;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.SpBiz;
import com.xwtech.xwecp.service.logic.pojo.DEL040004Result;

public interface ITransactMonternetService
{
	public DEL040004Result transactMonternet(String phoneNum, List<SpBiz> spBiz, String bizCode, String spId) throws LIException;

}