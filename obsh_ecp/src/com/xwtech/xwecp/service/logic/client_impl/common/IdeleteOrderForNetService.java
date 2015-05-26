package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.DEL040041Result;
/**
 * 定单删除
 * @author xwtec
 *
 */
public interface IdeleteOrderForNetService
{
	public DEL040041Result deleteOrderForNet(String orderid) throws LIException;

}