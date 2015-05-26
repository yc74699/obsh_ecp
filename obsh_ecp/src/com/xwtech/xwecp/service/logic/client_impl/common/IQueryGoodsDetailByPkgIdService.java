package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY070007Result;

import com.xwtech.xwecp.service.logic.LIException;
public interface IQueryGoodsDetailByPkgIdService
{
	public QRY070007Result queryGoodsDetailByPkgId(String actId, String packid) throws LIException;

}