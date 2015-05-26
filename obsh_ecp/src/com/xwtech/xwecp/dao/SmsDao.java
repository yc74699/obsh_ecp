package com.xwtech.xwecp.dao;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.lob.LobHandler;

import com.xwtech.xwecp.util.ConfigurationRead;

public class SmsDao extends BaseDAO {
	private Logger logger = Logger.getLogger(SmsDao.class);
	
	private LobHandler lobHandler;

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public int sendSms(String mobile, String content, String spCode,
			String serviceId) throws DAOException {
		int intRet = 0;
		try {
			String user = ConfigurationRead.getInstance().getValue("smsuser") + ".";
			if (content != null && !"".equals(content)) {
				String [] contArr = this.organSmsArr(content, 70);

				if (contArr != null && contArr.length > 0) {
					for (int i = 0; i < contArr.length; i ++) {
						String cot = contArr[i];

						String sql = "INSERT INTO "+user+"SMS_MT_SWAP(ID, INF_TYPE_ID, MSG_TYPE, MSG_CONTENT, FEE_TERMINAL_ID, "+
						"FEE_TERMINAL_TYPE, FEE_USERTYPE, DEST_TERMINAL_ID, DEST_TERMINAL_TYPE, SP_CODE, SERVICE_ID, "+
						"FEE_TYPE, FEE_CODE, RECEIVE_REPORT, MSG_FORMAT, MSG_PRIORITY, TRY_TIMES, VALID_TIME, AT_TIME,"+
						"SERVICE_CODE, REGION_CODE, LINKID, REQUEST_TIME, BATCH_ID"+
						") VALUES("+user+"SEQ_SMS_MT_SWAP.NEXTVAL, NULL, 1, ?, NULL, 0, 0, ?, 0, ?, ?, "+
						"1, 0, 0, 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";

						Object [] args = new Object[4];
						args[0] = cot;
						args[1] = mobile;
						args[2] = spCode;
						args[3] = serviceId;

						intRet = this.getJdbcTemplate().update(sql, args);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			intRet = 0;
			throw new DAOException();
		}
		return intRet;
	}

	/**
	 * 组织短信内容分发
	 * @param content
	 * @param size
	 * @return
	 */
	private String [] organSmsArr(String content, int size) {
		String [] retArr;
		
		if (size <= 0) {
			return null;
		}
		
		int z = content.length()/size;
		int y = content.length()%size;

		int len = y == 0 ? z : z + 1;

		retArr = new String [len];

		for (int i = 0; i < len; i++) {
			String s = "";
			if (y != 0 && i == len -1) {
				s = content.substring(i * size, i * size + y);
			} else {
				s = content.substring(i * size, i * size + size);
			}
			retArr[i] = s;
		}
		return retArr;
	}
}
