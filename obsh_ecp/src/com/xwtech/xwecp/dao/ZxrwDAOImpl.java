package com.xwtech.xwecp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xwtech.xwecp.service.logic.pojo.LanOrderInfo;
import com.xwtech.xwecp.service.logic.pojo.ZxrwOrderInfo;
import com.xwtech.xwecp.service.logic.pojo.ZxrwSiteOfficeInfo;

public class ZxrwDAOImpl extends BaseDAO implements IZxrwDAO {
//    private String ZXRW_USER = "obsh_2012.";
	private String ZXRW_USER = "bodata.";
	/**
	 * 根据号码和流水号查询锁号状态(商城调用)
	 * @param mobile - 号码
	 * @param orderId - 流水号
	 */
	@SuppressWarnings("unchecked")
	public String queryMobileLockState(String mobile, String orderId) throws DAOException {
		String stateFlag = "1";
		try {
			String qryMobileStateSQL = " SELECT * FROM "+ ZXRW_USER +"T_ZXRW_MSISDN_LOCK T, (select * from "+ ZXRW_USER +"T_ZXRW_MOBILE UNION select * from "+ ZXRW_USER +"T_ZXRW_MOBILE_MARKET) TT ";
			qryMobileStateSQL += " WHERE T.MOBILE = TT.SITE_MOBILE";
			qryMobileStateSQL += " AND TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') >= T.CREATE_TIME ";
			qryMobileStateSQL += " AND TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') < T.UNLOCKED_TIME AND T.MOBILE = ? ";
			int state1 = 0;
			int state2 = 0;
			List retList = this.getJdbcTemplate().queryForList(qryMobileStateSQL, new Object[]{mobile});
			if(retList != null && retList.size() > 0) {
				for (Object obj : retList) {
					Map map = (Map)obj;
					String tmpSID = String.valueOf(map.get("SID"));
					// 是自己的
					if(tmpSID.equals(orderId)){
						state1++;
					}
					// 是别人的
					else {
						state2++;
					}
				}
				
				// 如果未被别人占有
				if(state2 == 0) {
					stateFlag = "0"; 
				}
			}
			else {
				// 锁号记录不存在
				stateFlag = "2";
			}
		} catch (Exception e) {
			throw new DAOException();
		}
		
		return stateFlag;
	}
	
	/**
	 * 根据号码和流水号查询锁号状态(商城调用)
	 * @param mobile - 号码
	 * @param orderId - 流水号
	 */
	@SuppressWarnings("unchecked")
	public String queryMobileState(String mobile, String orderId) throws DAOException {
		try {
			String qryMobileStateSQL = " SELECT T.MOBILE MOBILE FROM "+ ZXRW_USER +"T_ZXRW_MSISDN_LOCK T " +
			"	WHERE TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') >= T.CREATE_TIME " +
			"   AND TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') < T.UNLOCKED_TIME ";
			int i = 0;
			HashMap<Integer, String> params = new HashMap<Integer, String>();
			if(!mobile.equals("")){
				qryMobileStateSQL += " AND T.MOBILE = ? ";
				params.put(i++, mobile);
			}
			
			if(!orderId.equals("")){}
			
			Object [] args = new Object[params.size()];
			for(int j=0; j<params.size(); j++){
				args[j] = String.valueOf(params.get(j));
			}
			
			List retList = this.getJdbcTemplate().queryForList(qryMobileStateSQL, args);
			if(retList != null && retList.size() > 0) {
				return "1";
			}
		} catch (Exception e) {
			throw new DAOException();
		}
		
		return "0";
	}
	
	/**
	 * 锁号操作(商城调用)
	 * @param mobile - 号码
	 * @param lockedType - 锁号类型(商城使用 - 3:60分钟, 4:24小时, 5:永久锁定)
	 * @param orderIds - UUID流水号集
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> lockedMobile(String lockedType, List<String> orderIds)  throws DAOException{
		Map<String, String> m = new HashMap<String, String>();
		String errOrderIds = "";
		int errOrderIdCount = 0;
		String orderState = "1";
		try{
			String lockSQL = "";
			if(lockedType.equals("6") || lockedType.equals("7")){
				for (String orderId : orderIds) {
					if(lockedType.equals("6")){
						lockSQL = "INSERT INTO "+ ZXRW_USER +"T_ZXRW_MSISDN_LOCK(ID, MOBILE, CREATE_TIME, UNLOCKED_TIME, STATE, SID) " +
						 " VALUES ("+ ZXRW_USER +"SEQ_LOCKMOBILE.nextval,(SELECT T.MOBILE FROM "+ ZXRW_USER +"T_ZXRW_ORDERINFO T WHERE T.UU_ID = ?),TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), TO_CHAR((SYSDATE + 1), 'YYYYMMDDHH24MISS'),2,?)";
						Object [] args = new Object[2];
						args[0] = orderId;
						args[1] = orderId;
						this.getJdbcTemplate().update(lockSQL, args);
						orderState = "0";
					}
					
					if(lockedType.equals("7")){
						lockSQL = "INSERT INTO "+ ZXRW_USER +"T_ZXRW_MSISDN_LOCK(ID, MOBILE, CREATE_TIME, UNLOCKED_TIME, STATE, SID) " +
						 " VALUES ("+ ZXRW_USER +"SEQ_LOCKMOBILE.nextval,(SELECT T.MOBILE FROM "+ ZXRW_USER +"T_ZXRW_ORDERINFO T WHERE T.UU_ID = ?),TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'),'99999999999999',2,?)";
						Object [] args = new Object[2];
						args[0] = orderId;
						args[1] = orderId;
						this.getJdbcTemplate().update(lockSQL, args);
						orderState = "0";
					}
				}
				
				m.put("state", orderState);
				m.put("orderIds", errOrderIds);
			}
			else{
				for (String orderId : orderIds) {
					/** 如果有记录，就在原记录上修改被锁时间 */
					String sql = " SELECT T.MOBILE MOBILE FROM "+ ZXRW_USER +"T_ZXRW_MSISDN_LOCK T " +
					"	WHERE TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') >= T.CREATE_TIME " +
					"   AND TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') < T.UNLOCKED_TIME " +
					"	AND T.SID = ?";
					List<Map<String, Object>> lockedMobiles = (List<Map<String, Object>>) this.getJdbcTemplate().queryForList(sql, new Object[]{orderId});
					if(lockedMobiles != null && lockedMobiles.size() > 0){
						lockSQL = "";
						if(lockedType.equals("3")){
							lockSQL = " UPDATE "+ ZXRW_USER +"T_ZXRW_MSISDN_LOCK t SET t.UNLOCKED_TIME = TO_CHAR((SYSDATE + 60 /(24*60)), 'YYYYMMDDHH24MISS') WHERE t.SID = ?";
							Object [] args = new Object[1];
							args[0] = orderId;
							this.getJdbcTemplate().update(lockSQL, args);
							orderState = "0";
						}
						
						if(lockedType.equals("4")){
							lockSQL = " UPDATE "+ ZXRW_USER +"T_ZXRW_MSISDN_LOCK t SET t.UNLOCKED_TIME = TO_CHAR((SYSDATE + 1), 'YYYYMMDDHH24MISS') WHERE t.SID = ?";
							Object [] args = new Object[1];
							args[0] = orderId;
							this.getJdbcTemplate().update(lockSQL, args);
							orderState = "0";
						}
						
						if(lockedType.equals("5")){
							lockSQL = " UPDATE "+ ZXRW_USER +"T_ZXRW_MSISDN_LOCK t SET t.UNLOCKED_TIME = '99999999999999' WHERE t.SID = ?";
							Object [] args = new Object[1];
							args[0] = orderId;
							this.getJdbcTemplate().update(lockSQL, args);
							orderState = "0";
						}
					}
					else {
						errOrderIds += orderId + ",";
						errOrderIdCount++;
						orderState = "1";
					}
				}
				
				m.put("state", orderState);
				m.put("orderIds", errOrderIds);
			}
		} catch(Exception e){
			m.put("state", "-9999");
			m.put("orderIds", "");
			throw new DAOException();
		}
		
		return m;
	}
	 
	/**
	 * 根据流水号列表查询在线入网资料(商城使用)
	 * @param orderIds - 流水号列表
	 */
	@SuppressWarnings("unchecked")
	public List<ZxrwOrderInfo> queryZxrwSubmits(String orderIds)  throws DAOException{
		List<ZxrwOrderInfo> zxrwOrderInfos = new ArrayList<ZxrwOrderInfo>();
		String orderInfoSQL = "SELECT * FROM "+ ZXRW_USER +"T_ZXRW_ORDERINFO t WHERE t.UU_ID IN(?)";
		try{
			List orderInfos = this.getJdbcTemplate().queryForList(orderInfoSQL, new Object[]{orderIds});
			if(orderInfos != null && orderInfos.size() != 0) {
				for(Object obj : orderInfos) {
					Map map = (Map)obj;
					ZxrwOrderInfo orderInfo = new ZxrwOrderInfo();
					orderInfo.setOrderId(String.valueOf(map.get("UU_ID")));
					orderInfo.setMobile(String.valueOf(map.get("MOBILE")));
					orderInfo.setMoney(String.valueOf(map.get("MONEY")));
					orderInfo.setUserName(String.valueOf(map.get("USERNAME")));
					orderInfo.setPersonalNum(String.valueOf(map.get("PERSONALNUM")));
					orderInfo.setPersonalAdd(String.valueOf(map.get("PERSONALADD")));
					orderInfo.setBrandCode(String.valueOf(map.get("BRAND_CODE")));
					orderInfo.setProductChanPing(String.valueOf(map.get("PRODUCT_CHANPING")));
					orderInfo.setProductCode(String.valueOf(map.get("PRODUCT_CODE")));
					orderInfo.setServiceCode(String.valueOf(map.get("SERVICE_CODE")));
					orderInfo.setIncrementCode(String.valueOf(map.get("INCREMENT_CODE")));
					orderInfo.setMarketId(String.valueOf(map.get("MARKET_ID")));
					orderInfo.setMarketLevel(String.valueOf(map.get("MARKET_LEVEL")));
					orderInfo.setMarketFee(String.valueOf(map.get("MARKET_FEE")));
					orderInfo.setMarketName(String.valueOf(map.get("MARKET_NAME")));
					orderInfo.setCityId(String.valueOf(map.get("CITY_ID")));
					orderInfo.setCityCode(String.valueOf(map.get("CITY_CODE")));
					orderInfo.setCityName(String.valueOf(map.get("CITY_NAME")));
					orderInfo.setCityCounty(String.valueOf(map.get("CITY_COUNTY")));
					orderInfo.setCityCountyName(String.valueOf(map.get("CITY_COUNTYNAME")));
					orderInfo.setCreateDate(String.valueOf(map.get("CREATE_DATE")));
				    // sunwei add start
				    orderInfo.setWlanAddress(String.valueOf(map.get("WLANADDRESS")));
				    orderInfo.setfCode(String.valueOf(map.get("FCODE")));
				    orderInfo.setWlanMoney(String.valueOf(map.get("WLANMONKEY")));
				    // sunwei add end
					zxrwOrderInfos.add(orderInfo);
				}
			}
		} catch(Exception e){
			zxrwOrderInfos = null;
			throw new DAOException();
		}
		
		return zxrwOrderInfos;
	}
	
	/**
	 * 根据地市和县市获取营业厅信息
	 * @param cityCode - 地市编码
	 * @param countyCode - 县市编码
	 */
	@SuppressWarnings("unchecked")
	public List<ZxrwSiteOfficeInfo> querySaleOfficeInfo(String dq, String xs) throws DAOException{
		List<ZxrwSiteOfficeInfo> siteOfficeInfoList = new ArrayList<ZxrwSiteOfficeInfo>();
		try {
			String sql = " SELECT T.SITE_ID,T.SITE_NAME,T.SITE_ADDRESS,T.SITE_WORKTIME,T.SITE_REGION,T.SITE_CONTACTPHONE,T.SITE_TRAFFIC " +
			"FROM "+ ZXRW_USER +"T_ZXRW_SITE T WHERE T.SITE_AREA = ? AND T.SITE_BOROUGH = ?";

			List<Map<String, Object>> saleOfficeList = this.getJdbcTemplate().queryForList(sql, new Object[]{dq, xs});
			if(saleOfficeList != null && saleOfficeList.size() > 0){
				for (Map m:saleOfficeList) {
					ZxrwSiteOfficeInfo siteOfficeInfo = new ZxrwSiteOfficeInfo();
					siteOfficeInfo.setSiteId(String.valueOf(m.get("SITE_ID")));
					siteOfficeInfo.setSiteName(String.valueOf(m.get("SITE_NAME")));
					siteOfficeInfo.setSiteAddress(String.valueOf(m.get("SITE_ADDRESS")));
					siteOfficeInfo.setSiteRegion(String.valueOf(m.get("SITE_REGION")));
					siteOfficeInfo.setSiteWordTime(String.valueOf(m.get("SITE_WORKTIME")));
					siteOfficeInfo.setSiteContactPhone(String.valueOf(m.get("SITE_CONTACTPHONE")));
					siteOfficeInfo.setSiteTraffic(String.valueOf(m.get("SITE_TRAFFIC")));

					siteOfficeInfoList.add(siteOfficeInfo);
				}
			}
		} catch (Exception e) {
			siteOfficeInfoList = null;
			throw new DAOException();
		}
		
		return siteOfficeInfoList;
	}

	/**
	 * 根据流水号查询宽带预约订单信息
	 * @param orderId - 编码
	 * @return 订单实体类
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public LanOrderInfo queryLanOrderInfo(String orderId) throws DAOException {
		LanOrderInfo lanInfo = new LanOrderInfo();
		try{
			String sql = "SELECT * FROM "+ ZXRW_USER +"T_LAN_ORDER_NEW T WHERE T.F_ORDER_NUM = ?";
			List listCount = this.getJdbcTemplate().queryForList(sql,new Object[]{orderId});
			if(listCount == null || listCount.size() == 0){
				return lanInfo;
			}
			
			Map<String,Object> lanMap = (Map<String, Object>) listCount.get(0);
			lanInfo.setFOrderNum(String.valueOf(lanMap.get("F_ORDER_NUM")));
			lanInfo.setFCommNum(String.valueOf(lanMap.get("F_COMM_NUM")));
			lanInfo.setFTelName(String.valueOf(lanMap.get("F_ORDER_NAME")));
			lanInfo.setFAddress(String.valueOf(lanMap.get("F_ADDRESS")));
			lanInfo.setFPhone(String.valueOf(lanMap.get("F_PHONE")));
			lanInfo.setFTime(String.valueOf(lanMap.get("F_ORDER_TIME")));
			lanInfo.setFResult(String.valueOf(lanMap.get("F_RESULT")));
			lanInfo.setFState(String.valueOf(lanMap.get("F_STATE")));
			lanInfo.setFFixDate(String.valueOf(lanMap.get("F_ORDER_FIX_DATE")));
			lanInfo.setFReferName(String.valueOf(lanMap.get("F_REFER_NAME")));
			lanInfo.setFReferTel(String.valueOf(lanMap.get("F_REFER_TEL")));
			lanInfo.setFReferChannel(String.valueOf(lanMap.get("F_REFER_CHANNEL")));
			lanInfo.setFRemark(String.valueOf(lanMap.get("F_LEAVING_WORDS")));
			lanInfo.setFArea(String.valueOf(lanMap.get("F_ORDER_AREA")));
			lanInfo.setFMarketId(String.valueOf(lanMap.get("F_MARKETID")));
			lanInfo.setFMarketLevelId(String.valueOf(lanMap.get("F_MARKETLEVELID")));
			lanInfo.setFMarketName(String.valueOf(lanMap.get("F_MARKETNAME")));
			lanInfo.setFMarketFee(String.valueOf(lanMap.get("F_MARKETFEE")));
			lanInfo.setFMarketRemark(String.valueOf(lanMap.get("F_MARKETREMARK")));
			lanInfo.setFDeposit(String.valueOf(lanMap.get("F_DEPOSIT")));
			lanInfo.setFCreateTime(String.valueOf(lanMap.get("F_CREATETIME")));
			lanInfo.setFMobile(String.valueOf(lanMap.get("F_MOBILE")));
			lanInfo.setFMarketBind(String.valueOf(lanMap.get("F_MARKETBIND")));
			lanInfo.setFMarketType(String.valueOf(lanMap.get("F_MARKETTYPE")));
			lanInfo.setFWlanPwd(String.valueOf(lanMap.get("F_WLANPWD")));
			lanInfo.setFProductCode(String.valueOf(lanMap.get("F_PRODUCTCODE")));
			lanInfo.setFProductName(String.valueOf(lanMap.get("F_PRODUCTNAME")));
			lanInfo.setFPackageZip(String.valueOf(lanMap.get("F_PACKAGEZIP")));
			lanInfo.setFChannel(String.valueOf(lanMap.get("F_CHANNEL")));
		} catch (Exception e) {
			throw new DAOException();
		}
		
		return lanInfo;
	}
}
