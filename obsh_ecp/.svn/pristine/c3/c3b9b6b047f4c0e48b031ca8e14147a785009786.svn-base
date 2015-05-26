package com.xwtech.xwecp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.lob.LobHandler;

import com.xwtech.xwecp.service.logic.pojo.UserOdcInfo;
import com.xwtech.xwecp.util.ConfigurationRead;

/**
 * 一维码处理类
 * @author 吴宗德
 *
 */
public class OdcDao extends BaseDAO {
	private static final Logger logger = Logger.getLogger(OdcDao.class);
	
	private static final String ODCUSER = ConfigurationRead.getInstance().getValue("odcuser") + ".";
	
	private LobHandler lobHandler;

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}
	
	/**
	 * 根据用户的手机号及商户编号查询用户的一维码获奖信息
	 * @param mobile		手机号
	 * @param merNum		商户编号
	 * @param odcNum		一维码
	 * @return
	 * @throws DAOException
	 */
	public List<UserOdcInfo> queryUserOdcInfo(String mobile, String mrcNum, String odcNum) throws DAOException {
		List<UserOdcInfo> retList = null;
		UserOdcInfo odcInfo = null;
		List list = null;
		try {
			String sql = "SELECT t.F_USER_NUM," +
					"       c.F_AREA_NAME," +
					"       t.F_ACT_NUM," +
					"       a.F_ACT_NAME," +
					"       t.F_AWARD_NUM," +
					"       b.F_AWARD_NAME," +
					"       b.F_AWARD_LEVEL," +
					"       t.F_ODC_EXG_ENDTIME," +
					"       CASE WHEN TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS') > t.F_ODC_EXG_ENDTIME THEN 2 ELSE t.F_IS_EXCHANGE END F_IS_EXCHANGE" +
					"  FROM "+ODCUSER+"T_ODC_AWARD_CODE_DISP t, "+ODCUSER+"T_ODC_ACT_DA a, "+ODCUSER+"T_ODC_AWARD_DA b, "+ODCUSER+"T_ODC_AREA_DA c" +
					" WHERE t.F_ACT_NUM = a.F_ACT_NUM" +
					"   AND t.F_AWARD_NUM = b.F_AWARD_NUM" +
					"	AND t.F_AREA_NUM = c.F_AREA_NUM" +
					"   AND t.F_USER_NUM = ?" +
					"   AND t.F_ODC_NUM IN" +
					"       (SELECT F_ODC_NUM FROM "+ODCUSER+"T_ODC_CODE_MER_LIST WHERE F_MRC_NUM = ?)";
			
			Object [] args = new Object[2];
			args[0] = mobile;
			args[1] = mrcNum;
			
			if (StringUtils.isNotBlank(odcNum)) {
				sql += "AND T.F_ODC_NUM = ?";
				
				args = new Object[3];
				args[0] = mobile;
				args[1] = mrcNum;
				args[2] = odcNum;
			}
			list = this.getJdbcTemplate().queryForList(sql, args);
			if (list != null && list.size() > 0) {
				retList = new ArrayList<UserOdcInfo>(list.size());
				for (int i = 0; i < list.size(); i ++) {
					Map map = (Map)list.get(i);
					odcInfo = new UserOdcInfo();
					
					odcInfo.setMobile((String)map.get("F_USER_NUM"));
					odcInfo.setArea((String)map.get("F_AREA_NAME"));
					odcInfo.setActNum((String)map.get("F_ACT_NUM"));
					odcInfo.setActName((String)map.get("F_ACT_NAME"));
					odcInfo.setAwardNum((String)map.get("F_AWARD_NUM"));
					odcInfo.setAwardName((String)map.get("F_AWARD_NAME"));
					odcInfo.setAwardLevel(String.valueOf(((BigDecimal)map.get("F_AWARD_LEVEL")).intValue()));
					odcInfo.setAwardEndtime((String)map.get("F_ODC_EXG_ENDTIME"));
					odcInfo.setIsExchange(Integer.parseInt(map.get("F_IS_EXCHANGE").toString()));
					
					retList.add(odcInfo);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return retList;
	}
	
	/**
	 * 查询一维码锁定状态
	 * @param odcNum
	 * @return
	 * @throws DAOException
	 */
	public int queryOdcLockInfo(String odcNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "DELETE FROM "+ODCUSER+"T_ODC_CODE_LOCK WHERE F_ODC_NUM = ? AND F_LOCK_TIME <= TO_CHAR(SYSDATE-1/24, 'YYYYMMDDHH24MISS')";
			Object [] args = new Object[1];
			args[0] = odcNum;
			
			this.getJdbcTemplate().update(sql, args);
			
			sql = "SELECT COUNT(*) M_COUNT FROM "+ODCUSER+"T_ODC_CODE_LOCK WHERE F_ODC_NUM = ?";
			
			intRet = this.getJdbcTemplate().queryForInt(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 查询活动兑奖截止日期
	 * @param actNum
	 * @return
	 * @throws DAOException
	 */
	public int queryActEndTime(String actNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "SELECT COUNT(*) AS NUM" +
					"  FROM "+ODCUSER+"T_ODC_ACT_DA " +
					" WHERE F_ACT_NUM = ?" +
					"   AND F_ACT_PAY_AWARD_ENDTIME >= TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS')";
			Object [] args = new Object[1];
			args[0] = actNum;
			
			intRet = this.getJdbcTemplate().queryForInt(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 根据一维码查询用户一维码奖品信息
	 * @param odcNum		一维码
	 * @return
	 * @throws DAOException
	 */
	public UserOdcInfo queryUserOdcInfoByOdcNum(String odcNum) throws DAOException {
		UserOdcInfo odcInfo = null;
		try {
			String sql = "SELECT COUNT(*) AS NUM FROM "+ODCUSER+"T_ODC_AWARD_CODE_DISP T WHERE T.F_ODC_NUM = ?";
			Object [] args = new Object[1];
			args[0] = odcNum;
			int sum = this.getJdbcTemplate().queryForInt(sql, args);
			
			//一维码信息存在
			if (sum > 0) {
				sql = "SELECT F_USER_NUM, F_AREA_NUM, F_ACT_NUM, F_AWARD_NUM, F_ODC_NUM, F_ACT_PUBLIC_KEY, F_IS_EXCHANGE " +
						"FROM "+ODCUSER+"T_ODC_AWARD_CODE_DISP WHERE F_ODC_NUM = ?";
				Map map = this.getJdbcTemplate().queryForMap(sql, args);
				if (map != null) {
					odcInfo = new UserOdcInfo();
					
					odcInfo.setMobile((String)map.get("F_USER_NUM"));
					odcInfo.setArea((String)map.get("F_AREA_NUM"));
					odcInfo.setActNum((String)map.get("F_ACT_NUM"));
					odcInfo.setAwardNum((String)map.get("F_AWARD_NUM"));
					odcInfo.setActPublicKey((String)map.get("F_ACT_PUBLIC_KEY"));
					odcInfo.setIsExchange(Integer.parseInt(map.get("F_IS_EXCHANGE").toString()));
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return odcInfo;
	}
	
	/**
	 * 增加一维码兑换明细
	 * @param mobile		手机号
	 * @param areaNum		地市编码
	 * @param odcNum		一维码编码
	 * @param actNum		活动编码
	 * @param awardNum		奖项编码
	 * @param merNum		商户登录账号
	 * @param fromIp		来源IP
	 * @param merAwardNum	用户在商户兑换的奖品编号
	 * @return
	 * @throws DAOException
	 */
	public int addOdcExchangeDetail(String mobile, String areaNum, String odcNum, 
			String actNum, String awardNum, String merNum, String fromIp, String merAwardNum, int state) throws DAOException {
		int intRet = -1;
		try {
			int seq = this.getJdbcTemplate().queryForInt("SELECT "+ODCUSER+"SEQ_ODC_CODE_EXCHANGE_MX_XH.NEXTVAL FROM DUAL");
			
			String sql = "INSERT INTO "+ODCUSER+"T_ODC_CODE_EXCHANGE_MX" +
					"  (F_XH," +
					"   F_USER_NUM," +
					"   F_AREA_NUM," +
					"   F_ODC_NUM," +
					"   F_ACT_NUM," +
					"   F_AWARD_NUM," +
					"   F_MER_ACCOUNT_NUM," +
					"   F_EXCHANGE_TIME," +
					"   F_FROM_IP," +
					"   F_MER_AWARD_NUM," +
					"	F_STATE," +
					"   F_BZ) " +
					"VALUES" +
					"  (?, ?, ?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), ?, ?, ?, '')";
			Object [] args = new Object[10];
			args[0] = seq;
			args[1] = mobile;
			args[2] = areaNum;
			args[3] = odcNum;
			args[4] = actNum;
			args[5] = awardNum;
			args[6] = merNum;
			args[7] = fromIp;
			args[8] = merAwardNum;
			args[9] = state;
			
			int uRet = this.getJdbcTemplate().update(sql, args);
			
			if (uRet > 0) {
				intRet = seq;
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 用户一维码兑换锁定
	 * @param odcNum
	 * @param merNum
	 * @return
	 * @throws DAOException
	 */
	public int lockOdcNum (String odcNum, String merNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "INSERT INTO "+ODCUSER+"T_ODC_CODE_LOCK" +
					"    (F_LOCK_NUMBER, F_ODC_NUM, F_MER_ACCOUNT_NUM, F_LOCK_TIME)" +
					"  VALUES" +
					"    ("+ODCUSER+"SEQ_ODC_LOCK_NUMBER.NEXTVAL," +
					"     ?, ?," +
					"     TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'))";
			Object [] args = new Object[2];
			args[0] = odcNum;
			args[1] = merNum;
			
			intRet = this.getJdbcTemplate().update(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 根据确认码查找一维码
	 * @param xh
	 * @return
	 * @throws DAOException
	 */
	public String queryOdcNumByXh (String xh) throws DAOException {
		String strRet = "";
		try {
			String sql = "SELECT F_ODC_NUM FROM "+ODCUSER+"T_ODC_CODE_EXCHANGE_MX WHERE F_XH = ?";
			Map map = this.getJdbcTemplate().queryForMap(sql, new Object[]{xh});
			if (map != null) {
				strRet = (String)map.get("F_ODC_NUM");
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return strRet;
	}
	
	/**
	 * 根据一维码查找锁定时间
	 * @param odcNum
	 * @return
	 * @throws DAOException
	 */
	public String queryLockTimeByOdcNum (String odcNum) throws DAOException {
		String strRet = "";
		try {
			String sql = "SELECT F_LOCK_TIME FROM "+ODCUSER+"T_ODC_CODE_LOCK WHERE F_ODC_NUM = ?";
			Map map = this.getJdbcTemplate().queryForMap(sql, new Object[]{odcNum});
			if (map != null) {
				strRet = (String)map.get("F_LOCK_TIME");
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return strRet;
	}
	
	/**
	 * 删除一维码锁定信息
	 * @param odcNum
	 * @return
	 * @throws DAOException
	 */
	public int delLockOdcNum(String odcNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "DELETE FROM "+ODCUSER+"T_ODC_CODE_LOCK WHERE F_ODC_NUM = ?";
			intRet = this.getJdbcTemplate().update(sql, new Object[]{odcNum});
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 更新一维码兑换明细
	 * @param xh
	 * @return
	 * @throws DAOException
	 */
	public int updateOdcExchangeDetail(String xh, int state) throws DAOException {
		int intRet = -1;
		try {
			String sql = "UPDATE "+ODCUSER+"T_ODC_CODE_EXCHANGE_MX SET F_STATE = ? WHERE F_XH = ?";
			
			Object [] args = new Object[2];
			args[0] = state;
			args[1] = xh;
			
			intRet = this.getJdbcTemplate().update(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 一维码兑换确认
	 * @param odcNum
	 * @return
	 * @throws DAOException
	 */
	public int odcExchangeConfirm(String odcNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "UPDATE "+ODCUSER+"T_ODC_AWARD_CODE_DISP SET F_IS_EXCHANGE = 1 WHERE F_ODC_NUM = ?";
			Object [] args = new Object[1];
			args[0] = odcNum;
			intRet = this.getJdbcTemplate().update(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 校验商户信息
	 * @param num		商户号码
	 * @param pwd		商户密码
	 * @return
	 */
	public boolean checkMerAccount(String num, String pwd) {
		boolean blRet = false;
		try {
			String sql = "SELECT F_MER_ACCOUNT_NUM, F_MER_ACCOUNT_PWD, " +
					"F_MER_ACCOUNT_TYPE, F_MRC_NUM, F_ACCOUNT_STATE " +
					"FROM "+ODCUSER+"T_ODC_MER_ACCOUNT_INFO " +
					"WHERE F_MER_ACCOUNT_NUM = ?";
			Object [] args = new Object[1];
			args[0] = num;
			Map map = this.getJdbcTemplate().queryForMap(sql, args);

			if (map != null) {
				String password = (String)map.get("F_MER_ACCOUNT_PWD");
				int state = Integer.parseInt(map.get("F_ACCOUNT_STATE").toString());

				if (state == 1 && pwd.equals(password)) {
					blRet = true;
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			blRet = false;
		}

		return blRet;
	}
	
	/**
	 * 生成一维码获取序列值
	 * @return
	 * @throws DAOException
	 */
	public long getNextOdcSeq() throws DAOException {
		long longRet = -1;
		try {
			longRet = this.getJdbcTemplate().queryForLong("SELECT "+ODCUSER+"SEQ_ODC_ID.NEXTVAL FROM DUAL");
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return longRet;
	}
	
	/**
	 * 生成当前一维码获取序列值
	 * @return
	 * @throws DAOException
	 */
	public long getCurrOdcSeq() throws DAOException {
		long longRet = -1;
		
		String driverClassName = ConfigurationRead.getInstance().getValue("driverClassName");
		String url = ConfigurationRead.getInstance().getValue("url");
		String username = ConfigurationRead.getInstance().getValue("username");
		String password = ConfigurationRead.getInstance().getValue("password");
		
		
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        
        try
        {
            Class.forName(driverClassName);
            con = DriverManager.getConnection(url, username, password);
            st = con.createStatement();
//            System.out.println(st.getFetchSize());
            String sql = "SELECT LAST_NUMBER FROM USER_SEQUENCES WHERE SEQUENCE_NAME = 'SEQ_ODC_ID'";
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                longRet = rs.getLong(1);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
            throw new DAOException(e.getMessage());
            
        }catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new DAOException("can not found class:oracle.jdbc.driver.OracleDriver");
            
        }finally
        {
            try
            {
                rs.close();
            }catch(Exception e)
            {
            }
            
            try
            {
                st.close();
            }catch(Exception e)
            {
            }
            
            try
            {
                con.close();
            }catch(Exception e)
            {
            }
        }
        return longRet;
	}
	
	/**
	 * 匹配一维码四位后缀
	 * @param odcNum
	 * @return
	 * @throws DAOException
	 */
	public int queryOdcNum(String odcNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "SELECT COUNT(*) AS NUM FROM "+ODCUSER+"T_ODC_AWARD_CODE_DISP T WHERE T.F_ODC_NUM LIKE '%"+odcNum+"'";
			intRet = this.getJdbcTemplate().queryForInt(sql);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 获取活动加密私钥
	 * @param actNum
	 * @return
	 * @throws DAOException
	 */
	public String getActPrivateKey(String actNum) throws DAOException {
		String strRet = null;
		try {
			String sql = "SELECT F_ACT_PRIVATE_KEY FROM "+ODCUSER+"T_ODC_ACT_DA WHERE F_ACT_NUM = ?";
			
			Map map = this.getJdbcTemplate().queryForMap(sql, new Object [] {actNum});
			
			if (map != null) {
				strRet = (String)map.get("F_ACT_PRIVATE_KEY");
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return strRet;
	}
	
	/**
	 * 根据奖项编码获取奖项等级
	 * @param awardNum
	 * @return
	 * @throws DAOException
	 */
	public int getAwardLevel(String awardNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "SELECT F_AWARD_LEVEL FROM "+ODCUSER+"T_ODC_AWARD_DA WHERE F_AWARD_NUM = ?";
			
			Map map = this.getJdbcTemplate().queryForMap(sql, new Object [] {awardNum});
			
			if (map != null) {
				intRet = Integer.parseInt(map.get("F_AWARD_LEVEL").toString());
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 更新奖项档案表
	 * @param actNum
	 * @param awardNum
	 * @return
	 * @throws DAOException
	 */
	public int updateOdcAwardDa(String actNum, String awardNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "UPDATE "+ODCUSER+"T_ODC_AWARD_DA " +
					"SET F_AWARD_AMOUNT_YFP = F_AWARD_AMOUNT_YFP + 1" +
					", F_AWARD_AMOUNT_WFP = F_AWARD_AMOUNT_WFP - 1 " +
					"WHERE F_ACT_NUM = ? AND F_AWARD_NUM = ? AND F_AWARD_AMOUNT_WFP >= 1";
			Object [] args = new Object[2];
			args[0] = actNum;
			args[1] = awardNum;
			intRet = this.getJdbcTemplate().update(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 查询一维码分配情况
	 * @param mobile
	 * @param actNum
	 * @param awardNum
	 * @return
	 * @throws DAOException
	 */
	public int queryOdcAwardCodeDisp (String mobile, String actNum, String awardNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "SELECT COUNT(*) AS NUM FROM "+ODCUSER+"T_ODC_AWARD_CODE_DISP " +
					"WHERE F_USER_NUM = ? AND F_ACT_NUM = ? AND F_AWARD_NUM = ?";
			
			Object [] args = new Object [3];
			args[0] = mobile;
			args[1] = actNum;
			args[2] = awardNum;
			
			intRet = this.getJdbcTemplate().queryForInt(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 添加一维码奖品分配信息
	 * @param mobile
	 * @param actNum
	 * @param awardNum
	 * @param odcNum
	 * @param key
	 * @return
	 * @throws DAOException
	 */
	public int addOdcAwardCodeDisp (String mobile, String areaNum, String actNum, String awardNum, String odcNum, String key) throws DAOException {
		int intRet = -1;
		try {
			String sql = "INSERT INTO "+ODCUSER+"T_ODC_AWARD_CODE_DISP" +
					"  (F_USER_NUM," +
					"   F_XH," +
					"   F_AREA_NUM," +
					"   F_ACT_NUM," +
					"   F_AWARD_NUM," +
					"   F_ODC_NUM," +
					"   F_ODC_EXG_ENDTIME," +
					"   F_ACT_PUBLIC_KEY," +
					"   F_IS_EXCHANGE," +
					"	F_ODC_DISP_TIME," +
					"   F_BZ)" +
					" VALUES" +
					"  (?," +
					"   (SELECT MAX(F_XH) + 1 FROM (SELECT F_XH" +
					"      FROM "+ODCUSER+"T_ODC_AWARD_CODE_DISP" +
					"     WHERE F_USER_NUM = ?" +
					"    UNION" +
					"    SELECT 0 FROM DUAL))," +
					"   ?, ?, ?, ?, " +
					"   (SELECT F_ACT_PAY_AWARD_ENDTIME FROM "+ODCUSER+"T_ODC_ACT_DA WHERE F_ACT_NUM = ?)," +
					"   ?, 0, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), NULL)";
			Object [] args = new Object [8];
			args[0] = mobile;
			args[1] = mobile;
			args[2] = areaNum;
			args[3] = actNum;
			args[4] = awardNum;
			args[5] = odcNum;
			args[6] = actNum;
			args[7] = key;
			
			intRet = this.getJdbcTemplate().update(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 添加用户一维码可兑换商户列表信息
	 * @param odcNum
	 * @param merNum
	 * @return
	 * @throws DAOException
	 */
	public int addOdcCodeMerList(String odcNum, String merNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = " INSERT INTO "+ODCUSER+"T_ODC_CODE_MER_LIST (F_ODC_NUM, F_MRC_NUM) VALUES (?, ?)";
			Object [] args = new Object [2];
			args[0] = odcNum;
			args[1] = merNum;
			
			intRet = this.getJdbcTemplate().update(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 校验奖品编码信息
	 * @param merAwardNum
	 * @param merAccountNum
	 * @return
	 * @throws DAOException
	 */
	public int checkMerAwardNum (String merAwardNum, String merAccountNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "SELECT COUNT(*) AS NUM" +
					"  FROM "+ODCUSER+"T_ODC_MER_AWARD_LIST T" +
					" WHERE T.F_MER_AWARD_NUM = ?" +
					"   AND T.F_MRC_NUM = (SELECT F_MRC_NUM" +
					"                        FROM "+ODCUSER+"T_ODC_MER_ACCOUNT_INFO" +
					"                       WHERE F_MER_ACCOUNT_NUM = ?)" +
					"   and t.f_state = 0";
			
			Object [] args = new Object [2];
			args[0] = merAwardNum;
			args[1] = merAccountNum;
			
			intRet = this.getJdbcTemplate().queryForInt(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 校验一维码在该商户下是否存在
	 * @param odcNum
	 * @param merAccountNum
	 * @return
	 * @throws DAOException
	 */
	public int checkOdcNum (String odcNum, String merAccountNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "SELECT COUNT(*) AS NUM" +
					"  FROM "+ODCUSER+"T_ODC_CODE_MER_LIST" +
					" WHERE F_ODC_NUM = ?" +
					"   AND F_MRC_NUM = (SELECT F_MRC_NUM" +
					"                      FROM "+ODCUSER+"T_ODC_MER_ACCOUNT_INFO" +
					"                     WHERE F_MER_ACCOUNT_NUM = ?)";
			
			Object [] args = new Object [2];
			args[0] = odcNum;
			args[1] = merAccountNum;
			
			intRet = this.getJdbcTemplate().queryForInt(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	
	/**
	 * 校验活动试用地区信息
	 * @param actNum
	 * @param areaNum
	 * @return
	 * @throws DAOException
	 */
	public int checkOdcArea (String actNum, String areaNum) throws DAOException {
		int intRet = -1;
		try {
			String sql = "SELECT COUNT(*) AS NUM FROM (" +
					"SELECT A.F_AREA_NUM, A.F_JB_NUM, A.F_JB" +
					"  FROM "+ODCUSER+"T_ODC_AREA_DA A," +
					"       (SELECT F_AREA_NUM, F_JB_NUM, F_JB" +
					"          FROM "+ODCUSER+"T_ODC_AREA_DA AA" +
					"         WHERE EXISTS (SELECT F_AREA_NUM" +
					"                                FROM "+ODCUSER+"T_ODC_ACT_AREA_DZ BB" +
					"                               WHERE AA.F_AREA_NUM = BB.F_AREA_NUM AND F_ACT_NUM = ?)) B" +
					" WHERE SUBSTR(A.F_JB_NUM, 1, 2 * B.F_JB) = B.F_JB_NUM" +
					"   AND A.F_JB = 2) WHERE F_AREA_NUM = ?";
			
			Object [] args = new Object[2];
			args[0] = actNum;
			args[1] = areaNum;
			
			intRet = this.getJdbcTemplate().queryForInt(sql, args);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return intRet;
	}
	 
	/**
	 * 校验活动、奖项、商户帐号是否对应
	 * @param actNum
	 * @param awardNum
	 * @param areaNum
	 * @param mrcNum
	 * @return
	 * @throws DAOException
	 */
	public Map checkOdcInfo(String actNum, String awardNum, String mrcNum) throws DAOException {
		Map map = null;
		try {
			String sql = "SELECT A.F_ACT_NUM, A.F_ACT_NAME, B.F_AWARD_NUM, B.F_AWARD_NAME" +
					"  FROM "+ODCUSER+"T_ODC_ACT_DA      A," +
					"       "+ODCUSER+"T_ODC_AWARD_DA    B," +
					"       "+ODCUSER+"T_ODC_MERCHANT_DA C" +
					" WHERE A.F_ACT_NUM = B.F_ACT_NUM" +
					"   AND A.F_ACT_NUM = ?" +
					"   AND B.F_AWARD_NUM = ?" +
					"   AND C.F_MRC_NUM = ?";
			
			String qSql = "SELECT COUNT(*) AS NUM FROM ( " + sql + " )";
			
			Object [] args = new Object[3];
			args[0] = actNum;
			args[1] = awardNum;
			args[2] = mrcNum;
			
			int num = this.getJdbcTemplate().queryForInt(qSql, args);
			if (num == 1) {
				map = this.getJdbcTemplate().queryForMap(sql, args);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return map;
	}
	
}
