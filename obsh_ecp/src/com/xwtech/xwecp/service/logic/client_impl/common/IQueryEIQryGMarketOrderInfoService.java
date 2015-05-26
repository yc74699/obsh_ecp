package com.xwtech.xwecp.service.logic.client_impl.common;

import java.util.Map;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010032Result;
/**
 * 集团在线入网订单查询
 * 
 * @author xufan
 * 2014-03-31
 */
public interface IQueryEIQryGMarketOrderInfoService
{
	public QRY010032Result queryEIQryGMarketOrderInfo(Map<String,Object>map) throws LIException;

}