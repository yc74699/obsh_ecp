package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY050053Result;

public interface IGetPkgInfoService
{
	public QRY050053Result getPkgInfo(String city, String rectype, String mobile, String prodId) throws LIException;

}