package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040072Result;
/**
 *  查询用户4G套餐上月使用情况：上个月开通了多少流量，还剩下多少流量。
 *  YangXQ 
 *  2014-6-13
 */
public interface IQuery4GPkgUsedInfoService
{
	public QRY040072Result query4GPkgUsedInfo(String phoneNum) throws LIException;

}