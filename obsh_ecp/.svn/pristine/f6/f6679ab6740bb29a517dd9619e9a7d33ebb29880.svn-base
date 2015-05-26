package com.xwtech.xwecp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.util.StringUtil;

public class TeletextDispatchDAO extends BaseDAO {
	
	private static final Logger logger = Logger.getLogger(TeletextDispatchDAO.class);
	
	/**
	 * 查询报文分发Map
	 * @param parameter
	 * @return
	 */
	public Map<String, String> getTeletextDispatchMap ()
	{
		StringBuffer str = null;
		Map<String, String> dispatchMap = null;
		
		try
		{
			Object obj = this.getCache().get("TELETEXT_DISPATCH_MAP");
			if(obj != null && obj instanceof Map)
			{
				dispatchMap = (Map<String, String>)obj;
			}
			else
			{
				str = new StringBuffer();
				str.append("SELECT T2.F_BOSS_INT_NUM,T.F_BOSS FROM T_TELETEXT_DISPATCH T LEFT JOIN T_BOSS_INT_DA T2 ON T.F_BOSS_INT_NUM = T2.F_BOSS_INT_NAME");
				
				logger.info(" ====== 查询报文分发Map sql ====== " + str.toString());
				List<Map> list = (List<Map>)this.getJdbcTemplate().queryForList(str.toString());
				dispatchMap = new HashMap<String, String>();
				
				for(Map m : list)
				{
					String id = String.valueOf(StringUtil.convertNull((String) m.get("F_BOSS_INT_NUM")));
					String boss = String.valueOf(StringUtil.convertNull((String) m.get("F_BOSS")));
					dispatchMap.put(id, boss);
				}
				this.getCache().add("TELETEXT_DISPATCH_MAP", dispatchMap);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return dispatchMap;
	}
	
	/**
	 * 获取渠道相关配置信息
	 * @param channelId			渠道编码
	 * @param processCode		
	 * @param sysfunId
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getChannelTeletextConfig(String channelId, String processCode, String sysfunId) throws DAOException {
		Map<String, String> resultMap = null;
		try {
			String key = "T_CHANNEL_TELETEXT_MODIFY"+"_"+channelId+"_"+processCode+"_"+sysfunId;
			Object obj = this.getCache().get(key);
			if(obj != null && obj instanceof Map)
			{
				resultMap = (Map<String, String>)obj;
			}
			else
			{
				String sql = "SELECT T.F_MODIFY_NAME, T.F_MODIFY_VALUE " +
							 "FROM T_CHANNEL_TELETEXT_MODIFY T " +
							 "WHERE T.F_CHANNEL_NUM = ? AND T.F_PROCESS_CODE = ? AND T.F_FUNCTION_ID = ? ";
				Object [] args = new Object[3];
				args[0] = channelId;
				args[1] = processCode;
				args[2] = sysfunId;

				List<Map> list = this.getJdbcTemplate().queryForList(sql, args);
				resultMap = new HashMap<String, String>();
				
				if (list != null && list.size() > 0) {
					String name = "";
					String value = "";
					for (Map m:list) {
						name = String.valueOf(StringUtil.convertNull((String) m.get("F_MODIFY_NAME")));
						value = String.valueOf(StringUtil.convertNull((String) m.get("F_MODIFY_VALUE")));
						
						resultMap.put(name, value);
					}
					this.getCache().add(key, resultMap);
				}
			}
			
		} catch (Exception e) {
			logger.error(e, e);
			throw new DAOException(e.getMessage());
		}
		return resultMap;
	}
	
	public Map<String, String> getUrgentDispatchMap(String region)
	{
		StringBuffer str = null;
		Map<String, String> dispatchMap = null;
		
		try
		{
			Object obj = this.getCache().get("T_URGENT_BOSS"+region);
			if(obj != null && obj instanceof Map)
			{
				dispatchMap = (Map<String, String>)obj;
			}
			else
			{
				str = new StringBuffer();
				str.append("SELECT T.F_BOSS_INT_NUM,T.F_REGION FROM T_URGENT_BOSS T WHERE T.F_FLAG = 1 AND T.F_REGION = ?");
				logger.info(" ====== 查询报文分发Map sql ====== " + str.toString());
				Object o[] = new Object[1];
				o[0] = region;
				List<Map> list = (List<Map>)this.getJdbcTemplate().queryForList(str.toString(),o);
				dispatchMap = new HashMap<String, String>();
				
				for(Map m : list)
				{
					String bossId = String.valueOf(StringUtil.convertNull((String) m.get("F_BOSS_INT_NUM")));
					String reg = String.valueOf(StringUtil.convertNull((String) m.get("F_REGION")));
					dispatchMap.put(bossId, reg);
				}
				this.getCache().add("T_URGENT_BOSS"+region, dispatchMap);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return dispatchMap;
	}
	
}
