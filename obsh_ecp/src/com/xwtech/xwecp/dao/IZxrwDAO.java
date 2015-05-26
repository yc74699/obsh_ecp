package com.xwtech.xwecp.dao;

import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.service.logic.pojo.LanOrderInfo;
import com.xwtech.xwecp.service.logic.pojo.ZxrwSiteOfficeInfo;
import com.xwtech.xwecp.service.logic.pojo.ZxrwOrderInfo;

public interface IZxrwDAO {
	
	/**
	 * 根据号码和流水号查询锁号状态(商城调用)
	 * @param mobile - 号码
	 * @param orderId - 流水号
	 * @throws DAOException 
	 */
	public String queryMobileLockState(String mobile, String orderId) throws DAOException;
	
	/**
	 * 根据号码和流水号查询锁号状态(商城调用)
	 * @param mobile - 号码
	 * @param orderId - 流水号
	 * @throws DAOException 
	 */
	public String queryMobileState(String mobile, String orderId) throws DAOException;
	
	/**
	 * 锁号操作(商城调用)
	 * @param mobile - 号码
	 * @param lockedType - 锁号类型(商城使用 - 3:60分钟, 4:24小时)
	 * @param orderIds - UUID流水号集
	 */
	public Map<String, String> lockedMobile(String lockedType, List<String> orderIds)  throws DAOException;
	
	/**
	 * 根据流水号列表查询在线入网资料(商城使用)
	 * @param orderIds - 流水号字符串
	 */
	public List<ZxrwOrderInfo> queryZxrwSubmits(String orderIds)  throws DAOException;
	
	/**
	 * 根据地市和县市获取营业厅信息
	 * @param cityCode - 地市编码
	 * @param countyCode - 县市编码
	 */
	public List<ZxrwSiteOfficeInfo> querySaleOfficeInfo(String dq, String xs) throws DAOException;
	
	/**
	 * 根据流水号查询宽带预约订单信息
	 * @param orderId - 编码
	 * @return 订单实体类
	 * @throws DAOException 
	 */
	public LanOrderInfo queryLanOrderInfo(String orderId) throws DAOException;
}
