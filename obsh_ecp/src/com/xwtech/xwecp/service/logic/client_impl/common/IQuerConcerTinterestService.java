package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040077Result;

/**
 * 新增兑换信息查询
 * @author YangXQ
 * 2014-7-15
 */
public interface IQuerConcerTinterestService
{
	public QRY040077Result queryConcerTinterest(String userid, String startday,String endday) throws LIException;
}