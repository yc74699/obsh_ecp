package com.xwtech.xwecp.dao;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.lob.LobHandler;


/**
 * @author        :  xmchen
 * @Create Date   :  2013-7-16
 */
public class InterfaceDAO extends BaseDAO {
	
	private static final Logger logger = Logger.getLogger(InterfaceDAO.class);
	
	private LobHandler lobHandler;

	
	public int updateState(String channelNum ,String status,String region,String[] interfaceArray)
	{
//		String str = null;
//		try{
//			Object obj = this.getCache().get("T_URGENT_BOSS");
//			if (obj != null) {
//				str = obj.toString();
//			} else {
//				final String sql = "SELECT T.F_FLAG FROM T_URGENT_BOSS T";
//
//				List<Map> list = (List<Map>) this.getJdbcTemplate().queryForList(sql, new Object[]{});
//				if(list.size() > 0){
//					this.getCache().add("T_URGENT_BOSS", list.get(0).get("F_FLAG"));
//				}
//			}
//		}
//		catch (Exception e) {
//			logger.error(e, e);
//		}
//		logger.info("数据库T_URGENT_BOSS.f_flag缓存值： "+str);

		int result = 0;

		StringBuilder sql = new StringBuilder();
		sql.append("update  ");
		sql.append("T_URGENT_BOSS t set t.F_FLAG = ? where T.F_REGION = ? AND T.F_CHANNEL_NUM = ? AND T.F_BOSS_NUM IN("+buildStrMethod(interfaceArray)+")");
		try
		{
			result =  this.getJdbcTemplate().update(sql.toString(),new Object[]{
						status,	region,channelNum});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("更新表失败");
		}
		return result;
	}
	
	public int updatesSysState(String status,String region,String channelNum)
	{
		int result = 0;
		StringBuilder sql = new StringBuilder();
		sql.append("update  ");
		sql.append("T_URGENT_BOSS_SYS t set t.f_flag = ? where t.f_region = ?  AND T.f_channel_num = ?");
		try
		{
			result =  this.getJdbcTemplate().update(sql.toString(),new Object[]{
						status,	region,channelNum});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("更新表失败");
		}
		return result;
	}
	/**
	 * 取消  数据库中to_order_net 订单
	 * @param orderId
	 * @return
	 */
	public int cancelOrderNetState(String orderId,String telnum)
	{
		int result = 0;
		StringBuilder sql = new StringBuilder();
//		sql.append("update  openapi.unip_addorder t set t.ORDERSTATUS = 2 where T.ORDERID = ?");//测试环境
		//现网环境
		
		
		sql.append("update xwmall.to_order_net  t set t.f_result = 1 where T.f_order_num = ? and t.f_phone=?");
		
		
		
		try
		{
			result =  this.getJdbcTemplate().update(sql.toString(),new Object[]{orderId,telnum});
		}catch (Exception e)
		{
			e.printStackTrace();
			logger.error("取消失败");
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	public int updateBusiState(String status,String region,String channelNum)
	{
		int result = 0;
		StringBuilder sql = new StringBuilder();
		sql.append("update  ");
		sql.append("T_URGENT_BOSS t set t.F_FLAG = ? where T.F_REGION = ? AND T.F_CHANNEL_NUM = ?");
		try
		{
			result =  this.getJdbcTemplate().update(sql.toString(),new Object[]{
						status,	region,channelNum});
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("更新表失败");
		}
		return result;
	}
	
	/**
	 * 更改数据库中订单的状态
	 * @param orderId
	 * @return
	 */
	public int updateOrderState(String orderId)
	{
		int result = 0;
		StringBuilder sql = new StringBuilder();
//		sql.append("update  openapi.unip_addorder t set t.ORDERSTATUS = 2 where T.ORDERID = ?");//测试环境
		//现网环境
		sql.append("update  ECOS4.unip_addorder t set t.ORDERSTATUS = 2 where T.ORDERID = ?");
		try
		{
			result =  this.getJdbcTemplate().update(sql.toString(),new Object[]{orderId});
		}catch (Exception e)
		{
			e.printStackTrace();
			logger.error("更新表失败");
		}
		return result;
	}
	
	/**
	 * 更改数据库中to_order_net 订单的状态
	 * @param orderId
	 * @return
	 */
	public int updateOrderNetState(String orderId,String orderstatus)
	{
		int result = 0;
		StringBuilder sql = new StringBuilder();
//		sql.append("update  openapi.unip_addorder t set t.ORDERSTATUS = 2 where T.ORDERID = ?");//测试环境
		//现网环境
		sql.append("update xwmall.to_order_net_new  t set t.f_crm_status = ? where t.f_order_num = ?");
		try
		{
			result =  this.getJdbcTemplate().update(sql.toString(),new Object[]{orderstatus,orderId});
		}catch (Exception e)
		{
			e.printStackTrace();
			logger.error("更新表失败");
		}
		return result;
	}
	
	private String buildStrMethod(String[] interfaceArray) {
		StringBuffer strBossId = new StringBuffer();
		for(int i = 0; i < interfaceArray.length; i++)
		{
			strBossId.append("'");
			strBossId.append(interfaceArray[i]);
			strBossId.append("'");
			strBossId.append(",");
		}
		return strBossId.toString().substring(0,strBossId.toString().lastIndexOf(","));
	}
	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}
}
