package com.xwtech.xwecp.dao;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.pojo.InterfaceCTimeInfo;


/**
 * 将接口超时时间读取到redis中
 * @author wang.huan
 *
 */
public class QryInterfaceConnetionTimeDAOImpl extends BaseDAO implements IQryInterfaceConnetionTimeDAO {
	
	private static final Logger logger = Logger.getLogger(QryInterfaceConnetionTimeDAOImpl.class);
	
	public int qryInterfaceConnetionTime(String processCode) throws DAOException {
		Object obj = XWECPApp.redisCli.get(("REDIS_CONNECT_TIME_OBJECT").getBytes());
		
		String cTime = "";
		if(obj == null){
			InterfaceCTimeInfo iti = new InterfaceCTimeInfo();
			String sql = "SELECT t.F_BOSS_INT_NAME , t.CONNECTION_TIME FROM T_BOSS_CONNECTION_TIME t ";
			List<Map<String,String>> ret =  (List<Map<String,String>>)this.getJdbcTemplate().queryForList(sql);
			if(ret != null && ret.size() > 0){
				for(Map<String,String> m : ret) 
				{
					iti.getInterfaceCTMap().put(m.get("F_BOSS_INT_NAME"), m.get("CONNECTION_TIME"));
				}
			}
			obj = iti;
			XWECPApp.redisCli.set("REDIS_CONNECT_TIME_OBJECT", iti);
		}
		
		InterfaceCTimeInfo ictInfo = (InterfaceCTimeInfo) obj;
		cTime = ictInfo.getInterfaceCTMap().get(processCode);
		if(cTime == null || "".equals(cTime)){
			cTime="0";
		}
		return Integer.parseInt(cTime);
	}

}
