package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.pojo.QRY040113Result;

/**
 * 池化
 * 套餐使用历史趋势查询
 * @author wangh
 *
 */
public interface IQueryFluxHisCHService {
	public QRY040113Result qryFluxHistoryCH(String startMonth, String endMonth, String isPool) throws LIException;
}
