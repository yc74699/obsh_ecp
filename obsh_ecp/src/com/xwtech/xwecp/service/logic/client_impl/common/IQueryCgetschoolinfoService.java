package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY610040Result;

/**
 * 新生随机码查询学校接口
 * @author YangXQ
 * 2015-04-14
 */
public interface IQueryCgetschoolinfoService
{
	public QRY610040Result queryCgetschoolinfo(String studentno) throws LIException;

}