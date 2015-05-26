package com.xwtech.xwecp.log;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.xwtech.xwecp.dao.BaseDAO;
import com.xwtech.xwecp.pojo.ChannelInfo;


public class LogDAOImpl extends BaseDAO implements ILogDAO
{
	private static final Logger logger = Logger.getLogger(LogDAOImpl.class);
	
	public boolean insertLInterfaceAccessLog(final LInterfaceAccessLogBean lb)
	{
		try
		{
			String sql = "INSERT INTO T_LI_ACCESS_LOG(F_ACCESS_ID, F_LOGIC_NUMBER, "
				+ "F_ACCESS_TIME, F_RESPONSE_TIME, F_CLIENT_IP, F_CHANNEL_NUM, "
				+ "F_CHANNEL_USER, F_IS_ERROR, F_RESULT_CODE, F_ERROR_CODE, "
				+ "F_ERROR_MSG, F_OP_TYPE, F_USER_MOBILE, F_USER_BRAND, "
				+ "F_USER_CITY, F_BIZ_CODE, F_REQ_TEXT, F_RES_TEXT, F_ERROR_STACK, F_CLIENT_ACCESS_ID) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,EMPTY_CLOB(),EMPTY_CLOB(),EMPTY_CLOB(),?)";
			int n = this.getJdbcTemplate().update(sql, new PreparedStatementSetter(){
				public void setValues(PreparedStatement ps) throws SQLException
				{
					int index = 1;
					ps.setString(index++, lb.f_access_id);
					ps.setString(index++, lb.f_logic_number);
					ps.setString(index++, lb.f_access_time);
					ps.setString(index++, lb.f_response_time);
					ps.setString(index++, lb.f_client_ip);
					ps.setString(index++, lb.f_channel_num);
					ps.setString(index++, lb.f_channel_user.toString());
					ps.setString(index++, lb.f_is_error);
					ps.setString(index++, lb.f_result_code);
					ps.setString(index++, lb.f_error_code);
					ps.setString(index++, lb.f_error_msg);
					ps.setString(index++, lb.f_op_type);
					ps.setString(index++, lb.f_user_mobile);
					ps.setString(index++, lb.f_user_brand);
					ps.setString(index++, lb.f_user_city);
					ps.setString(index++, lb.f_biz_code);
					ps.setString(index++, lb.f_client_access_id);
				}			
			});
			/*
			if(n == 1)
			{
				String updateSQL = "UPDATE T_LI_ACCESS_LOG SET F_REQ_TEXT = ?, F_RES_TEXT = ?, F_ERROR_STACK = ? WHERE F_ACCESS_ID = '" + lb.f_access_id + "'";
			    SqlLobValue[] values = new SqlLobValue[3];
			    values[0] = new SqlLobValue(lb.f_req_text);
			    values[1] = new SqlLobValue(lb.f_res_text);
			    values[2] = new SqlLobValue(lb.f_error_stack);
			    updateForClob(updateSQL, values);
			    return true;
			}
			*/
			return n == 1;
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return false;
	}
	
	public boolean insertBossRequestAccessLog(final BossRequestLogBean lb)
	{
		try
		{
			String sql = "INSERT INTO T_BOSS_ACCESS_LOG(F_BOSS_REQ_ID, F_ACCESS_ID, F_BOSS_ID, " +
					"F_ACCESS_TIME, F_RESPONSE_TIME, F_IS_ERROR, F_RESULT_CODE, " +
					"F_ERROR_CODE, F_ERROR_MSG, F_REQ_TEXT, F_RES_TEXT, " +
					"F_ERROR_STACK) VALUES (?,?,?,?,?,?,?,?,?,EMPTY_CLOB(),EMPTY_CLOB(),EMPTY_CLOB())";
			int n = this.getJdbcTemplate().update(sql,new PreparedStatementSetter(){
				public void setValues(PreparedStatement ps) throws SQLException
				{
					int index = 1;
					ps.setString(index++, lb.f_boss_req_id);
					ps.setString(index++, lb.f_access_id);
					ps.setString(index++, lb.f_boss_id);
					ps.setString(index++, lb.f_access_time);
					ps.setString(index++, lb.f_response_time);
					ps.setString(index++, lb.f_is_error);
					ps.setString(index++, lb.f_result_code);
					ps.setString(index++, lb.f_error_code);
					ps.setString(index++, lb.f_error_msg);
				}			
			});
			/*
			if(n == 1)
			{
				String updateSQL = "UPDATE T_BOSS_ACCESS_LOG SET F_REQ_TEXT = ?, F_RES_TEXT = ?, F_ERROR_STACK = ? WHERE F_BOSS_REQ_ID = '" + lb.f_boss_req_id + "'";
			    SqlLobValue[] values = new SqlLobValue[3];
			    values[0] = new SqlLobValue(lb.f_req_text);
			    values[1] = new SqlLobValue(lb.f_res_text);
			    values[2] = new SqlLobValue(lb.f_error_stack);
			    updateForClob(updateSQL, values);
			    return true;
			}
			*/
			return n == 1;
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return false;
	}

	public Map<String, String> getBossLogRecord() {

		Map bossLogRecordMap = (Map) (this.getCache().get("T_BOSSLOG_RECORD_FLAG"));

		if ( bossLogRecordMap == null )
		{
			List tempLst = this.getJdbcTemplate().queryForList(
					"SELECT T.f_boss_id, T.flag FROM T_BOSSLOG_RECORD_FLAG T");

			if ( tempLst == null || tempLst.size() <= 0 )
			{
				return bossLogRecordMap;
			}
			else
			{
				bossLogRecordMap = new HashMap();
				Map temp = new HashMap();
				String bossId = "";
				String flag = "";
				for(int i=0; i < tempLst.size();i++)
				{
					temp = (Map) tempLst.get(i);
					bossId = temp.get("F_BOSS_ID")== null ? "" : temp.get("F_BOSS_ID").toString();
					flag = temp.get("FLAG")== null ? "" : temp.get("FLAG").toString();
					bossLogRecordMap.put(bossId, flag);
				}
				this.getCache().add("T_BOSSLOG_RECORD_FLAG" ,bossLogRecordMap);
			}
		}
		return bossLogRecordMap;
	}
}
