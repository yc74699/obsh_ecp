package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY010039Result;
/**
 * 分布式清单菜单查询
 * @author xufan
 * 2014-06-20
 */
public interface IQueryMenuService
{
	public QRY010039Result qryMenu(String reqChannel) throws LIException;

}