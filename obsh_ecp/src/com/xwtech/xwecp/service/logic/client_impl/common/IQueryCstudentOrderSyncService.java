package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY610042Result;

/**
 * 在线入网订单同步CRM接口
 * @author YangXQ
 * 2015-04-14
 */
public interface IQueryCstudentOrderSyncService
{
	public QRY610042Result queryCstudentOrderSync(
			String 	orderid,
			String 	telnum,
			String 	studentno,
			String 	schoolno,
			String 	schoolname,
			String 	prodinfo,
			String 	linkname,
			String 	linkaddr,
			String 	linknum
) throws LIException;

}