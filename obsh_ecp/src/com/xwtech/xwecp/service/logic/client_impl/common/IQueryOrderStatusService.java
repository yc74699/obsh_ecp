package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.QRY040088Result;

/**
 * 订单状态查询接口
 * @author YangXQ
 * 2014-10-23
 */
public interface IQueryOrderStatusService
{
	public QRY040088Result queryOrderStatus(String ddr_city,String order_id) throws LIException;

}