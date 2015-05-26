package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY050076Result;

public interface IQueryIsYingXingCard
{
	public QRY050076Result queryIsYingXingCard(String phoneNum, String areaNum) throws LIException;

}