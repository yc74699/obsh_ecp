package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040051Result;

public interface IQueryGroupUserUsedInfoService
{
	public QRY040051Result queryGroupUserUsedInfo(String phoneNum) throws LIException;

}