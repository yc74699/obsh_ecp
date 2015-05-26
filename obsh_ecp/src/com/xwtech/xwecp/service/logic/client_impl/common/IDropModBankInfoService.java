package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040066Result;

public interface IDropModBankInfoService
{
	public DEL040066Result dropModBankInfo(String phoneNum, String opertype, String contractinfo, String channeltype) throws LIException;

}