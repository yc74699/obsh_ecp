package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040076Result;

/**
 * 新增用户余额利息明细查询功能
 * @author YangXQ
 * 2014-7-15
 */
public interface IQueryAcctbkinterstDetailService
{
	public QRY040076Result queryAcctbkinterstDetail(String userid, String startday,String endday) throws LIException;
}