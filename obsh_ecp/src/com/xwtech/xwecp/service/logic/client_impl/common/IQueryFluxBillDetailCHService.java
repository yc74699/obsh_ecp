package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY040107Result;

/**
 * 流量账单-流量套餐使用明细 池化
 * @author wangh
 *
 */
public interface IQueryFluxBillDetailCHService 
{
	public QRY040107Result qryFluxBillDetailCH(String month, String isPool) throws LIException;
}
