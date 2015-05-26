package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY050056Result;

public interface IQueryIsFamileyMember
{
	public QRY050056Result queryIsFamileyMember(String msisdn) throws LIException;

}