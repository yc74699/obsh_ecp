package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040029Result;

public interface ICheckVnetXQWMemberService
{
	public QRY040029Result checkVnetXQWMemberInfo(String phoneNum) throws LIException;

}