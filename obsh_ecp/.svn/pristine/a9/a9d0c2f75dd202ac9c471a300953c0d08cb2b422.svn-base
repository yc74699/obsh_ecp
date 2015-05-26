package com.xwtech.xwecp.dao;

import java.util.List;

import com.xwtech.xwecp.pojo.OrderInitForm;
import com.xwtech.xwecp.service.logic.pojo.OrderReceiptInfo;
import com.xwtech.xwecp.service.logic.pojo.OrderUpdateInfo;
import com.xwtech.xwecp.service.logic.pojo.ReserveOfficeInfo;
import com.xwtech.xwecp.service.logic.pojo.ReserveOrderInfo;

public interface IReserveDAO {

	
	/**
	 * 根据手机号查询业务订单
	 */
	public List<ReserveOrderInfo> getBusiOrder(String orderMobile, String busiNum) throws DAOException;
	
	/**
	 * 根据手机号查询营销案业务订单
	 */
	public List<ReserveOrderInfo> getMarketOrder(String orderMobile, String busiNum) throws DAOException;
	
	/**
	 * 根据手机号查询业务（网掌短）和营销案的订单
	 */
	public List<ReserveOrderInfo> getAllOrder(String orderMobile) throws DAOException;
	
	/**
	 * 根据营业厅编码查询业务订单数量
	 */
	public String getBusiOfficeOrderNum(String officeId,String expectTime,String expectPeriod) throws DAOException;
	
	/**
	 * 根据营业厅编码查询营销案业务订单数量
	 */
	public String getMarketOfficeOrderNum(String officeId,String expectTime,String expectPeriod) throws DAOException;
	
	
	/**
	 * 获取业务名称
	 */
	public String getBusiName(String busiNum) throws DAOException;
	

	/**
	 * 添加订单主要信息
	 * 
	 * @param __form
	 * @throws DAOException
	 */
	public String addBusiOrder(OrderInitForm __form)throws DAOException;
	
	
	/**
	 * 获取营销案名称
	 */
	public String getMarketName(String marketNum) throws DAOException;
	
	/**
	 * 根据营业厅编码营业厅名称
	 */
	public ReserveOfficeInfo getReserveOfficeInfo(String officeId) throws DAOException;
	
//	/**
//	 * 根据营业厅编码查询业务订单
//	 */
//	public List<ReserveOrderInfo> getBusiOfficeOrder(String officeId,String expectTime,String expectPeriod) throws DAOException;
//	
//	/**
//	 * 根据营业厅编码查询营销案业务订单
//	 */
//	public List<ReserveOrderInfo> getMarketOfficeOrder(String officeId,String expectTime,String expectPeriod) throws DAOException;
//	
	
	
	/**
	 * 取消业务订单
	 */
	public int cancelBusiName(String busiNum,String orderMobile) throws DAOException;
	
	/**
	 * 取消营销案订单
	 */
	public int cancelMarketName(String marketNum,String orderMobile) throws DAOException;

	/**
	 * 添加营销案订单
	 * @param __form
	 * @throws DAOException
	 */
	public String addMarketOrder(OrderInitForm __form)throws DAOException;
	
	/**
	 * 根据订单编号 取消业务订单
	 */
	public int cancelBusiOrderByOrderId(String orderId) throws DAOException;
	
	/**
	 * 根据订单编号 取消营销案订单
	 */
	public int cancelMarketOrderByOrderId(String orderId) throws DAOException;
	
	/**
	 * 根据订单编号 修改业务订单
	 */
	public int updateBusiOrderByOrderId(String orderId,OrderUpdateInfo orderUpdateInfo) throws DAOException;
	
	/**
	 * 根据订单编号 修改营销订单
	 */
	public int updateMarketOrderByOrderId(String orderId,OrderUpdateInfo orderUpdateInfo) throws DAOException;
	
	/**
	 * 根据订单号查询业务订单
	 */
	public List<ReserveOrderInfo> getBusiOrder(String orderId) throws DAOException;
	
	/**
	 * 根据订单号查询营销案业务订单
	 */
	public List<ReserveOrderInfo> getMarketOrder(String orderId) throws DAOException;
	
	/**
	 * 根据订单编号 进行业务订单回单
	 */
	public int receiptBusiOrderByOrderId(String orderId,OrderReceiptInfo orderReceiptInfo) throws DAOException;
	
	/**
	 * 根据订单编号 进行营销案订单回单
	 */
	public int receiptMarketOrderByOrderId(String orderId,OrderReceiptInfo orderReceiptInfo) throws DAOException;
}
