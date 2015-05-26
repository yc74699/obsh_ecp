package com.xwtech.xwecp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.OrderIdGenerator;
import com.xwtech.xwecp.msg.OrderMarketIdGenerator;
import com.xwtech.xwecp.pojo.OrderInitForm;
import com.xwtech.xwecp.service.logic.pojo.OrderReceiptInfo;
import com.xwtech.xwecp.service.logic.pojo.OrderUpdateInfo;
import com.xwtech.xwecp.service.logic.pojo.ReserveOfficeInfo;
import com.xwtech.xwecp.service.logic.pojo.ReserveOrderInfo;
import com.xwtech.xwecp.util.StringUtil;

public class ReserveDAOImpl extends BaseDAO implements IReserveDAO {

	private static final Logger logger = Logger.getLogger(ReserveDAOImpl.class);
	
	private OrderMarketIdGenerator orderMarketIdGenerator;

	private OrderIdGenerator orderIdGenerator;
	
	public OrderIdGenerator getOrderIdGenerator() {
		return orderIdGenerator;
	}

	public void setOrderIdGenerator(OrderIdGenerator orderIdGenerator) {
		this.orderIdGenerator = orderIdGenerator;
	}

	public OrderMarketIdGenerator getOrderMarketIdGenerator() {
		return orderMarketIdGenerator;
	}

	public void setOrderMarketIdGenerator(
			OrderMarketIdGenerator orderMarketIdGenerator) {
		this.orderMarketIdGenerator = orderMarketIdGenerator;
	}

//	String USER = "zb.";//ConfigurationRead.getInstance().getValue("zxrwuser") + ".";
	String USER = "ecos4.";
	/**
	 * 根据手机号查询业务订单
	 */
	public List<ReserveOrderInfo> getBusiOrder(String orderMobile, String busiNum)
			throws DAOException {
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("select F_ORDER_ID   ,");   
		sql.append(" F_OFFICE_ID      ,");   
		sql.append(" F_ORDER_MOBILE	 ,"); 		
		sql.append(" F_CITY	 ,"); 
		sql.append(" F_BRAND	 ,"); 
		sql.append(" F_BUSI_NAME	 ,"); 		
		sql.append(" F_BUSI_NUM	 ,"); 	
		sql.append(" F_ORDER_CHANNEL	 ,"); 	
		sql.append(" F_APPLY_FLAG	 ,"); 				
		sql.append(" F_ORDER_TIME	 ,"); 
		sql.append(" F_DONE_TIME	 ,"); 	
		sql.append(" F_EXPECT_TIME	 ,"); 
		sql.append(" F_EXPECT_PERIOD ,");
		sql.append(" F_STATE	 ,"); 
		sql.append(" F_RES_BZ ,");
		sql.append(" F_ERROR_RESULT	 ,"); 
		sql.append(" F_ERROR_BZ FROM  ");
		sql.append(USER);
		sql.append("RE_BUSI_ORDER T ");
		sql.append(" where F_ORDER_STATE ='1' AND T.F_ORDER_MOBILE = ?");
		List<Object> args = new ArrayList<Object>();
		args.add(orderMobile);
		if (!StringUtil.isNull(busiNum) && !"".equals(busiNum)) {
			sql.append(" AND T.F_BUSI_NUM = ? ");
			args.add(busiNum);
		}
		List<Map> list = null;
		try {
			logger.debug("执行订单查询SQL：=====>" + sql.toString());
			list = getJdbcTemplate().queryForList(sql.toString(), args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("手机号查询业务订单失败！");
			throw new DAOException(e);
		}
		List<ReserveOrderInfo> retList = null;
		if (null != list && list.size() > 0) {
			retList = new ArrayList<ReserveOrderInfo>();
			for (Map map : list) {
				retList.add(orderBusiMapBean(map));
			}
		}
		return retList;
	}
	
	
	/**
	 * 根据手机号查询营销案业务订单
	 */
	public List<ReserveOrderInfo> getMarketOrder(String orderMobile, String busiNum)
			throws DAOException {
		StringBuilder sql = new StringBuilder();
		sql.append("select F_ORDER_ID   ,");   
		sql.append(" F_OFFICE_ID      ,");   
		sql.append(" F_ORDER_MOBILE	 ,"); 		
		sql.append(" F_CITY	 ,"); 
		sql.append(" F_BRAND	 ,"); 
		sql.append(" F_MARKET_NAME	 ,"); 		
		sql.append(" F_BUSI_NUM	 ,"); 	
		sql.append(" F_ORDER_CHANNEL	 ,"); 	
		sql.append(" F_APPLY_FLAG	 ,"); 				
		sql.append(" F_ORDER_TIME	 ,"); 
		sql.append(" F_DONE_TIME	 ,"); 	
		sql.append(" F_EXPECT_TIME	 ,");
		sql.append(" F_EXPECT_PERIOD ,");
		sql.append(" F_STATE	 ,"); 
		sql.append(" F_OPERATE_BZ  ,");
		sql.append(" F_ERROR_RESULT	 ,"); 
		sql.append(" F_ERROR_BZ FROM  ");
		sql.append(USER);
		sql.append("RE_MARKET_ORDER T ");
		sql.append(" where F_ORDER_STATE ='1' AND T.F_ORDER_MOBILE = ?");
		List<Object> args = new ArrayList<Object>();
		args.add(orderMobile);
		if (!StringUtil.isNull(busiNum) && !"".equals(busiNum)) {
			sql.append(" AND T.F_BUSI_NUM = ? ");
			args.add(busiNum);
		}
		List<Map> list = null;
		try {
			logger.debug("执行订单查询SQL：=====>" + sql.toString());
			list = getJdbcTemplate().queryForList(sql.toString(), args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("手机号查询营销案订单失败！");
			throw new DAOException(e);
		}
		List<ReserveOrderInfo> retList = null;
		if (null != list && list.size() > 0) {
			retList = new ArrayList<ReserveOrderInfo>();
			for (Map map : list) {
				retList.add(orderMarketMapBean(map));
			}
		}
		return retList;
	}
	
	/**
	 * 根据手机号查询业务（网掌短）和营销案的订单
	 */
	public List<ReserveOrderInfo> getAllOrder(String orderMobile)
			throws DAOException {
		//业务
		StringBuilder b_sql = new StringBuilder();
		b_sql.append("select F_ORDER_ID   ,");   
		b_sql.append(" F_OFFICE_ID      ,");   
		b_sql.append(" F_ORDER_MOBILE	 ,"); 		
		b_sql.append(" F_CITY	 ,"); 
		b_sql.append(" F_BRAND	 ,"); 
		b_sql.append(" F_BUSI_NAME	 ,"); 		
		b_sql.append(" F_BUSI_NUM	 ,"); 	
		b_sql.append(" F_ORDER_CHANNEL	 ,"); 	
		b_sql.append(" F_APPLY_FLAG	 ,"); 				
		b_sql.append(" F_ORDER_TIME	 ,"); 
		b_sql.append(" F_DONE_TIME	 ,"); 	
		b_sql.append(" F_EXPECT_TIME	 ,"); 
		b_sql.append(" F_EXPECT_PERIOD ,");
		b_sql.append(" F_STATE	 ,"); 
		b_sql.append(" F_RES_BZ  ,");
		b_sql.append(" F_ERROR_RESULT	 ,"); 
		b_sql.append(" F_ERROR_BZ FROM  ");
		b_sql.append(USER);
		b_sql.append("RE_BUSI_ORDER T ");
		b_sql.append(" where F_ORDER_STATE ='1' AND T.F_ORDER_MOBILE = ?");
		List<Object> args = new ArrayList<Object>();
		args.add(orderMobile);
		List<Map> list = null;
		try {
			logger.debug("执行订单查询SQL：=====>" + b_sql.toString());
			list = getJdbcTemplate().queryForList(b_sql.toString(), args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("手机号查询业务订单失败！");
			throw new DAOException(e);
		}
		List<ReserveOrderInfo> retList1 = null;
		if (null != list && list.size() > 0) {
			retList1 = new ArrayList<ReserveOrderInfo>();
			for (Map map : list) {
				retList1.add(orderBusiMapBean(map));
			}
		}
		
		//营销案
		StringBuilder m_sql = new StringBuilder();
		m_sql.append("select F_ORDER_ID   ,");  
		m_sql.append(" F_ORDER_MOBILE	 ,");
		m_sql.append(" F_CITY	 ,");
		m_sql.append(" F_MARKET_NAME	 ,");
		m_sql.append(" F_BUSI_NUM	 ,"); 
		m_sql.append(" F_APPLY_FLAG	 ,"); 				
		m_sql.append(" F_ORDER_TIME	 ,"); 
		m_sql.append(" F_DONE_TIME	 ,"); 
		m_sql.append(" F_STATE	 ,");
		m_sql.append(" F_OPERATE_BZ  ,");
		m_sql.append(" F_BRAND	 ,");
		m_sql.append(" F_OFFICE_ID      ,");   
		m_sql.append(" F_ORDER_CHANNEL	 ,"); 	
		m_sql.append(" F_EXPECT_TIME	 ,");
		m_sql.append(" F_EXPECT_PERIOD ,");
		m_sql.append(" F_ORDER_STATE ,"); 
		m_sql.append(" F_ERROR_RESULT	 ,"); 
		m_sql.append(" F_ERROR_BZ FROM  ");
		m_sql.append(USER);
		m_sql.append("RE_MARKET_ORDER T ");
		m_sql.append(" where F_ORDER_STATE ='1' AND T.F_ORDER_MOBILE = ?");
		List<Object> args2 = new ArrayList<Object>();
		args2.add(orderMobile);

		List<Map> list2 = null;
		try {
			logger.debug("执行订单查询SQL：=====>" + m_sql.toString());
			list2 = getJdbcTemplate().queryForList(m_sql.toString(), args2.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("手机号查询营销案订单失败！");
			throw new DAOException(e);
		}
		List<ReserveOrderInfo> retList2 = null;
		if (null != list2 && list2.size() > 0) {
			retList2 = new ArrayList<ReserveOrderInfo>();
			for (Map map : list2) {
				retList2.add(orderMarketMapBean(map));
			}
		}
		List<ReserveOrderInfo> retList = new ArrayList<ReserveOrderInfo>();
		if(null != retList1 ){
		retList.addAll(retList1);
		}
		if(null != retList2 ){
		retList.addAll(retList2);
		}
		return retList;
	}
	
	
	
	
	private ReserveOrderInfo orderBusiMapBean(Map map) {
		ReserveOrderInfo order = new ReserveOrderInfo();
		
		order.setOrderId(null == map.get("F_ORDER_ID") ? null : map.get("F_ORDER_ID").toString());
		order.setOrderMobile(null == map.get("F_ORDER_MOBILE") ? null : map.get("F_ORDER_MOBILE").toString());		
		order.setCityCode(null == map.get("F_CITY") ? null : map.get("F_CITY").toString());
		order.setBusiName(null == map.get("F_BUSI_NAME") ? null : map.get("F_BUSI_NAME").toString());
		order.setBusiNum(null == map.get("F_BUSI_NUM") ? null : map.get("F_BUSI_NUM").toString());
		order.setOrderChannel(null == map.get("F_ORDER_CHANNEL") ? null : map.get("F_ORDER_CHANNEL").toString());
		order.setApplyFlag(null == map.get("F_APPLY_FLAG") ? null : map.get("F_APPLY_FLAG").toString());
		order.setOrderTime(null == map.get("F_ORDER_TIME") ? null : map.get("F_ORDER_TIME").toString());
		order.setDoneTime(null == map.get("F_DONE_TIME") ? null : map.get("F_DONE_TIME").toString());
		order.setResBz(null == map.get("F_RES_BZ") ? null : map.get("F_RES_BZ").toString());
		order.setIsSms(null == map.get("F_IS_SMS") ? null : map.get("F_IS_SMS").toString());
		order.setBrandCode(null == map.get("F_BRAND") ? null : map.get("F_BRAND").toString());
		order.setOfficeId(null == map.get("F_OFFICE_ID") ? null : map.get("F_OFFICE_ID").toString());
		order.setExpectTime(null == map.get("F_EXPECT_TIME") ? null : map.get("F_EXPECT_TIME").toString());
		order.setExpectPeriod(null == map.get("F_EXPECT_PERIOD") ? null : map.get("F_EXPECT_PERIOD").toString());
		order.setState(null == map.get("F_STATE") ? null : map.get("F_STATE").toString());
		order.setOrderState(null == map.get("F_ORDER_STATE") ? null : map.get("F_ORDER_STATE").toString());
		order.setError_reuslt(null == map.get("F_ERROR_RESULT") ? null : map.get("F_ERROR_RESULT").toString());
		order.setBusiType("1");//1-业务 2-营销案
		order.setError_bz(null == map.get("F_ERROR_BZ") ? null : map.get("F_ERROR_BZ").toString());
		return order;
	}

	private ReserveOrderInfo orderMarketMapBean(Map map) {
		ReserveOrderInfo order = new ReserveOrderInfo();
		
		order.setOrderId(null == map.get("F_ORDER_ID") ? null : map.get("F_ORDER_ID").toString());
		order.setOrderMobile(null == map.get("F_ORDER_MOBILE") ? null : map.get("F_ORDER_MOBILE").toString());
		order.setCityCode(null == map.get("F_CITY") ? null : map.get("F_CITY").toString());
		order.setMarketName(null == map.get("F_MARKET_NAME") ? null : map.get("F_MARKET_NAME").toString());
		order.setBusiNum(null == map.get("F_BUSI_NUM") ? null : map.get("F_BUSI_NUM").toString());
		order.setApplyFlag(null == map.get("F_APPLY_FLAG") ? null : map.get("F_APPLY_FLAG").toString());
		order.setOrderTime(null == map.get("F_ORDER_TIME") ? null : map.get("F_ORDER_TIME").toString());
		order.setDoneTime(null == map.get("F_DONE_TIME") ? null : map.get("F_DONE_TIME").toString());
		order.setState(null == map.get("F_STATE") ? null : map.get("F_STATE").toString());
		order.setBz(null == map.get("F_OPERATE_BZ") ? null : map.get("F_OPERATE_BZ").toString());
		order.setIsSms(null == map.get("F_IS_SMS") ? null : map.get("F_IS_SMS").toString());
		order.setBrandCode(null == map.get("F_BRAND") ? null : map.get("F_BRAND").toString());
		order.setOfficeId(null == map.get("F_OFFICE_ID") ? null : map.get("F_OFFICE_ID").toString());
		order.setOrderChannel(null == map.get("F_ORDER_CHANNEL") ? null : map.get("F_ORDER_CHANNEL").toString());
		order.setExpectTime(null == map.get("F_EXPECT_TIME") ? null : map.get("F_EXPECT_TIME").toString());
		order.setExpectPeriod(null == map.get("F_EXPECT_PERIOD") ? null : map.get("F_EXPECT_PERIOD").toString());
		order.setOrderState(null == map.get("F_ORDER_STATE") ? null : map.get("F_ORDER_STATE").toString());
		order.setError_reuslt(null == map.get("F_ERROR_RESULT") ? null : map.get("F_ERROR_RESULT").toString());
		order.setError_bz(null == map.get("F_ERROR_BZ") ? null : map.get("F_ERROR_BZ").toString());
		order.setBusiType("2");//1-业务 2-营销案
		return order;
	}

	
	
	/**
	 * 根据营业厅编码查询业务订单数量
	 */
	public String getBusiOfficeOrderNum(String officeId,String expectTime,String expectPeriod)
			throws DAOException {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) AS AMOUNT");   
		sql.append(" from  ");
		sql.append(USER);
		sql.append(" RE_BUSI_ORDER T ");
		sql.append(" where T.F_OFFICE_ID = ?");
		List<Object> args = new ArrayList<Object>();
		args.add(officeId);
		if (!StringUtil.isNull(expectTime)) {
			sql.append(" AND T.F_EXPECT_TIME = ? ");
			args.add(expectTime);
		}
		sql.append("AND T.F_EXPECT_PERIOD in(");
		if (!StringUtil.isNull(expectPeriod)) {
			if("1".equals(expectPeriod))
			{
				sql.append("1,3");
			}else if("2".equals(expectPeriod))
			{
				sql.append("2,3");
			}else if("3".equals(expectPeriod))
			{
				sql.append("1,2,3");
			}
		}
		sql.append(")");
		try {
			List<Map> list = this.getJdbcTemplate().queryForList(sql.toString(),args.toArray());
			if(null == list || list.size() < 1)
			{
				return "0";
			}
			for(Map map : list){
				 return  null == map.get("AMOUNT").toString() ? "" : map.get("AMOUNT").toString();
			}
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("营业厅编码查询业务订单数量失败！");
			throw new DAOException(e);
		}
	}
	
	/**
	 * 根据营业厅编码查询营销案业务订单数量
	 */
	public String getMarketOfficeOrderNum(String officeId,String expectTime,String expectPeriod)
			throws DAOException {
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) AS AMOUNT");   
		sql.append(" from  ");
		sql.append(USER);
		sql.append("RE_MARKET_ORDER T ");
		sql.append(" where T.F_OFFICE_ID = ?");
		List<Object> args = new ArrayList<Object>();
		args.add(officeId);
		if (!StringUtil.isNull(expectTime)) {
			sql.append(" AND T.F_EXPECT_TIME = ? ");
			args.add(expectTime);
		}
		sql.append("AND T.F_EXPECT_PERIOD in(");
		if (!StringUtil.isNull(expectPeriod)) {
			if("1".equals(expectPeriod))
			{
				sql.append("1,3");
			}else if("2".equals(expectPeriod))
			{
				sql.append("2,3");
			}else if("3".equals(expectPeriod))
			{
				sql.append("1,2,3");
			}
		}
		sql.append(")");
		try {
			List<Map> list = this.getJdbcTemplate().queryForList(sql.toString(),args.toArray());
			if(null == list || list.size() < 1)
			{
				return "0";
			}
			for(Map map : list){
				 return  null == map.get("AMOUNT").toString() ? "" : map.get("AMOUNT").toString();
			}
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("营业厅编码查询营销案订单数量失败！");
			throw new DAOException(e);
		}
	}

	/**
	 * 获取业务名称
	 */
	public String getBusiName(String busiNum) throws DAOException
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select t.f_busi_name from ");
		sql.append(USER);
		sql.append("RE_BUSINESS_DA t where t.f_busi_num = ?");
		try
		{
			List<Map> list = this.getJdbcTemplate().queryForList(sql.toString(),new Object[]{busiNum});
			if(null == list || list.size() < 1)
			{
				return "";
			}
//			List<String> rs = new ArrayList<String>();
			for(Map map : list){
				 return  null == map.get("f_busi_name").toString() ? "" : map.get("f_busi_name").toString();
			}
			return "";
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("查询业务名称失败");
			throw new DAOException(e);
		}
	}
	
	/**
	 * 添加业务订单信息
	 */
	public String addBusiOrder(OrderInitForm order) throws DAOException 
	{
		 	
		String orderId = "";
		StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO ");
			sql.append(USER);
			sql.append("RE_BUSI_ORDER (");
			sql.append(" F_ORDER_ID   ,");   
			sql.append(" F_OFFICE_ID      ,");   
			sql.append(" F_ORDER_MOBILE	 ,"); 		
			sql.append(" F_CITY	 ,"); 
			sql.append(" F_BRAND	 ,"); 
			sql.append(" F_BUSI_NAME	 ,"); 		
			sql.append(" F_BUSI_NUM	 ,"); 	
			sql.append(" F_ORDER_CHANNEL	 ,"); 	
			sql.append(" F_APPLY_FLAG	 ,"); 				
			sql.append(" F_ORDER_TIME	 ,"); 
			sql.append(" F_DONE_TIME	 ,"); 	
			sql.append(" F_EXPECT_TIME	 ,");
			sql.append(" F_EXPECT_PERIOD ,");
			sql.append(" F_STATE	 ,"); 					
			sql.append(" F_RES_BZ ) VALUES(?,?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),?,?,?,?,?)");
			try {
				orderId = this.orderIdGenerator.next();
				Object[] b = new Object[]{
						orderId,
						order.getOfficeId(),
						order.getOrderMobile(),
						order.getCityCode(),
						order.getBrandCode(),
						order.getBusiName(),
						order.getBusiNum(),
						order.getOrderChannel(),
						order.getApplyFlag(),
						"",
						order.getExpectTime(),
						order.getExpectPeriod(),
						"0",
						order.getResBz()==null?"":order.getResBz(),
				};
				update(sql.toString(), b);
				return orderId;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("添加业务新订单失败");
				throw new DAOException(e);
			}
			
	}	
	
	/**
	 * 获取营销案名称
	 */
	public String getMarketName(String marketNum) throws DAOException
	{
		String marketName = (String)(this.getCache().get("RESERVE_MARKET_" + marketNum));
		if(StringUtils.isBlank(marketName)){
			StringBuilder sql = new StringBuilder();
			sql.append("select t.F_MARKET_LEVEL2_NAME from ");
			sql.append(USER);
			sql.append("re_boss_market_second t where t.F_MARKET_LEVEL2_ID = ?");
			try
			{
				List<Map> list = this.getJdbcTemplate().queryForList(sql.toString(),new Object[]{marketNum});
				if(null == list || list.size() < 1)
				{
					return "";
				}
//				List<String> rs = new ArrayList<String>();
				for(Map map : list){
					marketName = null == map.get("F_MARKET_LEVEL2_NAME").toString() ? "" : map.get("F_MARKET_LEVEL2_NAME").toString();
					break;
				}
				this.getCache().add("RESERVE_MARKET_" + marketNum, marketName);
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("查询营销案名称失败");
				throw new DAOException(e);
			}
		}
		return marketName;
	}
	
	/**
	 * 根据营业厅编码营业厅名称
	 */ 
	public ReserveOfficeInfo getReserveOfficeInfo(String officeId) throws DAOException
	{
		ReserveOfficeInfo officeInfo = (ReserveOfficeInfo)(this.getCache().get("RESERVE_" + officeId));
		if(StringUtil.isNull(officeInfo)){
			StringBuilder sql = new StringBuilder();
			sql.append("select t.* from ");
			sql.append(USER);
			sql.append("RE_SALEOFFICE_INFO t where t.F_OFFICE_NUM = ?");
			try
			{
				List<Map> list = this.getJdbcTemplate().queryForList(sql.toString(),new Object[]{officeId});
				if(null == list || list.size() < 1)
				{
					return null;
				}
//				List<String> rs = new ArrayList<String>();
				for(Map map : list){
					officeInfo = new ReserveOfficeInfo();
					String strOfficeName = null == map.get("F_OFFICE_NAME") ? "" : map.get("F_OFFICE_NAME").toString();
					String strOfficeNum = null == map.get("F_OFFICE_NUM") ? "" : map.get("F_OFFICE_NUM").toString();
					String strOfficeAddress = null == map.get("F_OFFICE_ADDR") ? "" : map.get("F_OFFICE_ADDR").toString();
					String strOfficeTel = null == map.get("F_OFFICE_TEL") ? "" : map.get("F_OFFICE_TEL").toString();
					officeInfo.setOfficeNum(strOfficeNum);
					officeInfo.setOfficeName(strOfficeName);
					officeInfo.setOfficeAddress(strOfficeAddress);
					officeInfo.setOfficeTel(strOfficeTel);
					break;
				}
				this.getCache().add("RESERVE_" + officeId, officeInfo);
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("查询营业厅名称失败");
				throw new DAOException(e);
			}
		}
		return officeInfo;
		
	}
	
	/**
	 * 取消业务订单
	 */
	public int cancelBusiName(String busiNum,String orderMobile) throws DAOException
	{
		StringBuilder sql = new StringBuilder();
//		sql.append("delete from RE_BUSI_ORDER t where t.f_busi_num = ? and t.F_ORDER_MOBILE = ?");
		sql.append("update  ");
		sql.append(USER);
		sql.append("RE_BUSI_ORDER t set T.F_ORDER_STATE ='0' where t.f_busi_num = ? and t.F_ORDER_MOBILE = ?");
		try
		{
			return this.getJdbcTemplate().update(sql.toString(),new Object[]{busiNum,orderMobile});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("取消业务订单失败");
			throw new DAOException(e);
		}
	}
	
	/**
	 * 取消营销案订单
	 */
	public int cancelMarketName(String marketNum,String orderMobile) throws DAOException
	{
		StringBuilder sql = new StringBuilder();
		//sql.append("delete from RE_MARKET_ORDER t where t.f_busi_num = ? and t.F_ORDER_MOBILE = ?");
		sql.append("update  ");
		sql.append(USER);
		sql.append("RE_MARKET_ORDER t set  T.F_ORDER_STATE ='0' where t.f_busi_num = ? and t.F_ORDER_MOBILE = ?");
		
		try
		{
			return this.getJdbcTemplate().update(sql.toString(),new Object[]{marketNum,orderMobile});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("取消营销案订单失败");
			throw new DAOException(e);
		}
	}
	
	/**
	 * 添加营销案订单信息
	 */
	public String addMarketOrder(OrderInitForm order) throws DAOException 
	{
		String orderId = "";
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO ");
		sql.append(USER);
		sql.append("RE_MARKET_ORDER (");
		sql.append(" F_ORDER_ID   ,");   
		sql.append(" F_OFFICE_ID      ,");   
		sql.append(" F_ORDER_MOBILE	 ,"); 		
		sql.append(" F_CITY	 ,"); 
		sql.append(" F_BRAND	 ,"); 
		sql.append(" F_MARKET_NAME	 ,"); 		
		sql.append(" F_BUSI_NUM	 ,"); 	
		sql.append(" F_ORDER_CHANNEL	 ,"); 	
		sql.append(" F_APPLY_FLAG	 ,"); 				
		sql.append(" F_ORDER_TIME	 ,"); 
		sql.append(" F_DONE_TIME	 ,"); 	
		sql.append(" F_EXPECT_TIME	 ,"); 
		sql.append(" F_EXPECT_PERIOD ,");
		sql.append(" F_STATE	 ,"); 					
		sql.append(" F_OPERATE_BZ ) VALUES(?,?,?,?,?,?,?,?,?,TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),?,?,?,?,?)");
		try {
			orderId = this.orderMarketIdGenerator.next();
			Object[] b = new Object[]{
					orderId,
					order.getOfficeId(),
					order.getOrderMobile(),
					order.getCityCode(),
					order.getBrandCode(),
					order.getMarketName(),
					order.getBusiNum(),
					order.getOrderChannel(),
					order.getApplyFlag(),
					"",
					order.getExpectTime(),
					order.getExpectPeriod(),
					"0",
					order.getResBz()==null?"":order.getResBz(),
			};
			update(sql.toString(), b);
			return orderId;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加营销案新订单失败");
			throw new DAOException(e);
		}
		
	}
	
	/**
	 * 根据订单编号 取消业务订单 
	 */
	public int cancelBusiOrderByOrderId(String orderId) throws DAOException
	{
		StringBuilder sql = new StringBuilder();
		sql.append("update  ");
		sql.append(USER);
		sql.append("RE_BUSI_ORDER t set T.F_ORDER_STATE ='2' where t.f_order_id = ?");
		try
		{
			return this.getJdbcTemplate().update(sql.toString(),new Object[]{orderId});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("取消业务订单失败");
			throw new DAOException(e);
		}
	}
	
	/**
	 *  根据订单编号 取消营销案订单
	 */
	public int cancelMarketOrderByOrderId(String orderId) throws DAOException
	{
		StringBuilder sql = new StringBuilder();
		//sql.append("delete from RE_MARKET_ORDER t where t.f_busi_num = ? and t.F_ORDER_MOBILE = ?");
		sql.append("update  ");
		sql.append(USER);
		sql.append("RE_MARKET_ORDER t set  T.F_ORDER_STATE ='2' where t.f_order_id = ?");
		
		try
		{
			return this.getJdbcTemplate().update(sql.toString(),new Object[]{orderId});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("取消营销案订单失败");
			throw new DAOException(e);
		}
	}
	
	/**
	 * 根据订单编号 修改业务订单 
	 */
	public int updateBusiOrderByOrderId(String orderId,OrderUpdateInfo orderUpdateInfo) throws DAOException
	{
		StringBuilder sql = new StringBuilder();
		sql.append("update  ");
		sql.append(USER);
		sql.append("RE_BUSI_ORDER t set t.f_office_id = ?,t.f_expect_time = ?,t.f_expect_period = ?,t.f_res_bz = ? where t.f_order_id = ?");
		try
		{
			return this.getJdbcTemplate().update(sql.toString(),new Object[]{
				orderUpdateInfo.getOfficeId(),
				orderUpdateInfo.getExpectTime(),
				orderUpdateInfo.getExpectPeriod(),
				orderUpdateInfo.getBz(),
				orderId});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("取消业务订单失败");
			throw new DAOException(e);
		}
	}
	
	/**
	 *  根据订单编号 取消营销案订单
	 */
	public int updateMarketOrderByOrderId(String orderId,OrderUpdateInfo orderUpdateInfo) throws DAOException
	{
		StringBuilder sql = new StringBuilder();
		sql.append("update  ");
		sql.append(USER);
		sql.append("RE_MARKET_ORDER t set  t.f_office_id = ?,t.f_expect_time = ?,t.f_expect_period = ?,t.f_operate_bz = ? where t.f_order_id = ?");
		
		try
		{
			return this.getJdbcTemplate().update(sql.toString(),new Object[]{
				orderUpdateInfo.getOfficeId(),
				orderUpdateInfo.getExpectTime(),
				orderUpdateInfo.getExpectPeriod(),
				orderUpdateInfo.getBz(),
				orderId});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("取消营销案订单失败");
			throw new DAOException(e);
		}
	}
	
	/**
	 * 根订单号查询业务订单
	 */
	public List<ReserveOrderInfo> getBusiOrder(String orderId)
			throws DAOException {
		StringBuilder sql = new StringBuilder();
		sql.append("select F_ORDER_ID   ,");   
		sql.append(" F_OFFICE_ID      ,");   
		sql.append(" F_ORDER_MOBILE	 ,"); 		
		sql.append(" F_CITY	 ,"); 
		sql.append(" F_BRAND	 ,"); 
		sql.append(" F_BUSI_NAME	 ,"); 		
		sql.append(" F_BUSI_NUM	 ,"); 	
		sql.append(" F_ORDER_CHANNEL	 ,"); 	
		sql.append(" F_APPLY_FLAG	 ,"); 				
		sql.append(" F_ORDER_TIME	 ,"); 
		sql.append(" F_DONE_TIME	 ,"); 	
		sql.append(" F_EXPECT_TIME	 ,"); 
		sql.append(" F_EXPECT_PERIOD ,");
		sql.append(" F_STATE	 ,"); 
		sql.append(" F_RES_BZ ,");
		sql.append(" F_ERROR_RESULT	 ,"); 
		sql.append(" F_ERROR_BZ FROM  ");
		sql.append(USER);
		sql.append("RE_BUSI_ORDER T ");
		sql.append(" where  T.F_ORDER_ID = ?");
		List<Object> args = new ArrayList<Object>();
		args.add(orderId);
		List<Map> list = null;
		try {
			logger.debug("执行订单查询SQL：=====>" + sql.toString());
			list = getJdbcTemplate().queryForList(sql.toString(), args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单号查询业务订单失败！");
			throw new DAOException(e);
		}
		List<ReserveOrderInfo> retList = null;
		if (null != list && list.size() > 0) {
			retList = new ArrayList<ReserveOrderInfo>();
			for (Map map : list) {
				retList.add(orderBusiMapBean(map));
			}
		}
		return retList;
	}
	
	/**
	 * 根据订单查询营销案业务订单
	 */
	public List<ReserveOrderInfo> getMarketOrder(String orderId)
			throws DAOException {
		StringBuilder sql = new StringBuilder();
		sql.append("select F_ORDER_ID   ,");   
		sql.append(" F_OFFICE_ID      ,");   
		sql.append(" F_ORDER_MOBILE	 ,"); 		
		sql.append(" F_CITY	 ,"); 
		sql.append(" F_BRAND	 ,"); 
		sql.append(" F_MARKET_NAME	 ,"); 		
		sql.append(" F_BUSI_NUM	 ,"); 	
		sql.append(" F_ORDER_CHANNEL	 ,"); 	
		sql.append(" F_APPLY_FLAG	 ,"); 				
		sql.append(" F_ORDER_TIME	 ,"); 
		sql.append(" F_DONE_TIME	 ,"); 	
		sql.append(" F_EXPECT_TIME	 ,");
		sql.append(" F_EXPECT_PERIOD ,");
		sql.append(" F_STATE	 ,"); 
		sql.append(" F_OPERATE_BZ  ,");
		sql.append(" F_ERROR_RESULT	 ,"); 
		sql.append(" F_ERROR_BZ FROM  ");
		sql.append(USER);
		sql.append("RE_MARKET_ORDER T ");
		sql.append(" where T.F_ORDER_ID = ?");
		List<Object> args = new ArrayList<Object>();
		args.add(orderId);
		List<Map> list = null;
		try {
			logger.debug("执行订单查询SQL：=====>" + sql.toString());
			list = getJdbcTemplate().queryForList(sql.toString(), args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单号查询营销案订单失败！");
			throw new DAOException(e);
		}
		List<ReserveOrderInfo> retList = null;
		if (null != list && list.size() > 0) {
			retList = new ArrayList<ReserveOrderInfo>();
			for (Map map : list) {
				retList.add(orderMarketMapBean(map));
			}
		}
		return retList;
	}

	public int receiptBusiOrderByOrderId(String orderId,
			OrderReceiptInfo orderReceiptInfo) throws DAOException {
		StringBuilder sql = new StringBuilder();
		sql.append("update  ");
		sql.append(USER);
		sql.append("RE_BUSI_ORDER t set t.F_STATE = ?,t.F_ERROR_RESULT = ?,t.F_ERROR_BZ = ? where t.f_order_id = ?");
		try
		{
			return this.getJdbcTemplate().update(sql.toString(),new Object[]{
				orderReceiptInfo.getOrderState(),
				orderReceiptInfo.getOrderErrorResult()==null?"0":orderReceiptInfo.getOrderErrorResult(),
				orderReceiptInfo.getOrderErrorBz()==null?"":orderReceiptInfo.getOrderErrorBz(),
				orderId});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("业务订单回单失败");
			throw new DAOException(e);
		}
	}

	public int receiptMarketOrderByOrderId(String orderId,
			OrderReceiptInfo orderReceiptInfo) throws DAOException {
		StringBuilder sql = new StringBuilder();
		sql.append("update  ");
		sql.append(USER);
		sql.append("RE_MARKET_ORDER set t.F_STATE = ?,t.F_ERROR_RESULT = ?,t.F_ERROR_BZ = ? where t.f_order_id = ?");
		
		try
		{
			return this.getJdbcTemplate().update(sql.toString(),new Object[]{
				orderReceiptInfo.getOrderState(),
				orderReceiptInfo.getOrderErrorResult()==null?"0":orderReceiptInfo.getOrderErrorResult(),
				orderReceiptInfo.getOrderErrorBz()==null?"":orderReceiptInfo.getOrderErrorBz(),
				orderId});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("营销案订单回单失败");
			throw new DAOException(e);
		}
	}
}
