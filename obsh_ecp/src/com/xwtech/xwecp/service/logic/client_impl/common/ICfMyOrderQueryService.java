package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010036Result;
/**
 * 融合套餐查询接口
 * @author xufan
 * 2014-06-11
 */
public interface ICfMyOrderQueryService
{
	public QRY010036Result cfmyorderquery(String orderid,String telnum) throws LIException;

}