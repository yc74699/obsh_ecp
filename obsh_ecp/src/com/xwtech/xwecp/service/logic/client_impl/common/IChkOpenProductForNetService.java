package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY090013Result;

public interface IChkOpenProductForNetService
{
	public QRY090013Result chkOpenProductForNet(String certid) throws LIException;

}