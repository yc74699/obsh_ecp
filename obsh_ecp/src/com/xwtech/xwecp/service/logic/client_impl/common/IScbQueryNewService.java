package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040063Result;

public interface IScbQueryNewService {
	public QRY040063Result scbQueryNew(String phoneNum, String beginTime, String endTime) throws LIException;
}
