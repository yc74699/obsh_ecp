package com.xwtech.xwecp.dao;

/**
 * 接口订单返回接口
 * @author wang.huan
 *
 */
public class ChargingOrderFeedbackDAOImpl extends BaseDAO implements IChargingOrderFeedbackDAO {
//	private String ZXRW_USER = "obsh_2012.";
	private String ZXRW_USER = "bodata.";
	
	public int addChargingOrder(String[] args) throws DAOException {
		
		int result = 0;
		try {
			String sql = "INSERT INTO " +ZXRW_USER + "T_NET_PAY_ORDER"
							 +" (T_LOG_ID,"
							 +"  T_ORDER_ID,"
							 +"  T_PAY_MOBILE,"
							 +"  T_PAY_MONEY,"
							 +"   T_PAY_TYPE,"
							 +"   T_PAY_CHANNEL,"
							 +"   T_PAY_TIME,"
							 +"   T_RESERVED,"
							 +"   T_RESERVED1)"
						     +"	VALUES"
							 +"  (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			result = this.getJdbcTemplate().update(sql, args);
		} catch (Exception e) {
			throw new DAOException();
		}
		return result;
	}
}
