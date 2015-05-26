package com.xwtech.xwecp.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.util.StringUtil;

public class YingXingCardDaoImpl extends BaseDAO implements IYingXingCardDao {
	private static final Logger logger = Logger.getLogger(YingXingCardDaoImpl.class);
	/**
	 * 取缓存数据库
	 * 有数据那么就是迎接卡
	 */


	public List<String> getYingXingNum(String phoneNum, String cityNum)
			throws DAOException {
		List cards = (List<String>)XWECPApp.redisCli.get(("FIND_YINGXING_CARD"+cityNum).getBytes());
		String sql = "select phone_num from t_yingxing_card t where t.city_num = ? order by t.phone_num ";
		if(null == cards || 0 >= cards.size())
		{
			cards = new ArrayList<String>(); 
			Object[] objects = new Object[]{cityNum};
			
			List<Map<String,String>> ret = (List<Map<String,String>>)this.getJdbcTemplate().queryForList(sql,objects);
			
			if(ret != null && ret.size() > 0)
			{
				for(Map<String,String> m : ret) 
				{
					String cardNumber = StringUtil.convertNull((String)m.get("PHONE_NUM"));
					cards.add(cardNumber);
				}
			}
			XWECPApp.redisCli.set("FIND_YINGXING_CARD"+cityNum, cards);
		}
		return cards;
	}
	/**
	 * 查询流量封顶的目标库
	 * @return
	 */
	public  List<String> getFluxFD(String city)throws DAOException
	{
		List cards = (List<String>)XWECPApp.redisCli.get(("T_MAXFLOW_NUM"+city).getBytes());
		
		String sql = "select phone from T_MAXFLOW_NUM t where t.city = ? order by t.phone ";
		
		if(null == cards || 0 >= cards.size())
		{
			cards = new ArrayList<String>(); 
			Object[] objects = new Object[]{city};
			List<Map<String,Object>> ret = null;
			
			ret = (List<Map<String,Object>>)this.getJdbcTemplate().queryForList(sql,objects);
		
			if(ret != null && ret.size() > 0)
			{
				for(Map<String,Object> m : ret) 
				{
					String cardNumber = StringUtil.convertNull((String)m.get("phone"));
					cards.add(cardNumber);
				}
			}
			XWECPApp.redisCli.set("T_MAXFLOW_NUM"+city, cards);
		}
		return cards;
	}
}