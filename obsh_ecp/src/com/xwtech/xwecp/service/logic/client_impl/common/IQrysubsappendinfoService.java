package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY060076Result;

/*
 * 校园客户夹寄卡资料录入情况查询
 * YangXQ 2014-6-18
 */
public interface IQrysubsappendinfoService
{
	public QRY060076Result qrysubsappendinfo(String telnum) throws LIException;
}