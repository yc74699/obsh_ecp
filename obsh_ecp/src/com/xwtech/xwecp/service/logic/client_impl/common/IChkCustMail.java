package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040042Result;

public interface IChkCustMail
{
	public QRY040042Result chkCustMail(String phoneNum, String mailAddress) throws LIException;

}