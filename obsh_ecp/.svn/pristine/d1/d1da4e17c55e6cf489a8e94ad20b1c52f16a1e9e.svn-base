package com.xwtech.xwecp.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.pojo.ChannelLimitInfo;
import com.xwtech.xwecp.pojo.EmerBusiInfo;
import com.xwtech.xwecp.pojo.IterfaceCapacity;
import com.xwtech.xwecp.util.IMemcachedKeys;
import com.xwtech.xwecp.util.StringUtil;

public class ServiceCallerPrivilegeDAOImpl extends BaseDAO implements IServiceCallerPrivilegeDAO
{
	private static final Logger logger = Logger.getLogger(ServiceCallerPrivilegeDAOImpl.class);

	private String ISOPENPOWER = "0"; 
	/**
	 * 获取客户端渠道的可信IP列表
	 * 
	 * @param channel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCallerAcceptAddress(String channel)
	{
		List<String> addresses = (List<String>) (this.getCache().get("CALLER_ACCEPT_IP_" + channel));
		if ( addresses == null )
		{
			addresses = new ArrayList<String>();
			List<Map> ret = (List<Map>) this.getJdbcTemplate().queryForList("SELECT T.F_CLIENT_IP FROM T_CHANNEL_CLIENTS_MX T WHERE T.F_CHANNEL_NUM= ? ", new String[] { channel });
			for (Map m : ret)
			{
				addresses.add(m.get("F_CLIENT_IP").toString());
			}

			this.getCache().add("CALLER_ACCEPT_IP_" + channel, addresses);
		}
		return addresses;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xwtech.xwecp.dao.IServiceCallerPrivilegeDAO#getCallerAcceptLiLst(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCallerAcceptLiLst(String channel)
	{
		List<String> liLst = (List<String>) (this.getCache().get("CALLER_ACCEPT_LI_" + channel));
		if ( liLst == null )
		{
			liLst = new ArrayList<String>();
			List<Map> ret = (List<Map>) this.getJdbcTemplate().queryForList("SELECT T.F_LINTERFACE_NUM FROM T_CHANNEL_LINTERFACE_DZ T WHERE T.F_CHANNEL_NUM= ? ", new String[] { channel });
			for (Map m : ret)
			{
				liLst.add(m.get("F_LINTERFACE_NUM").toString());
			}

			this.getCache().add("CALLER_ACCEPT_LI_" + channel, liLst);
		}
		return liLst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.xwtech.xwecp.dao.IServiceCallerPrivilegeDAO#getChannelInfo(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public ChannelInfo getChannelInfo(String channel) throws DAOException
	{
		Map channelMap = (Map) (this.getCache().get("CALLER_CHANNEL_INFO_" + channel));

		if ( channelMap == null )
		{
			List tempLst = this.getJdbcTemplate().queryForList(
					"SELECT T.F_AUTH_IP, T.F_AUTH_PWD, T.F_AUTH_LI, T.F_USER, T.F_PASSWORD, F_FLOW_CONTRL, F_MAX_CONNNUMS,APP_ID,ACCESS_TOKEN,SIGN FROM T_CHANNEL_INFO T WHERE T.F_CHANNEL_NUM = ? ", new String[] { channel });

			if ( tempLst == null || tempLst.size() <= 0 )
			{
				logger.error("获取渠道信息时出错，没有查询到符合条件[渠道编码为：" + channel + "]的渠道信息！");
				throw new DAOException();
			}
			else
			{
				channelMap = (Map) tempLst.get(0);
				this.getCache().add("CALLER_CHANNEL_INFO_" + channel, channelMap);
			}
		}

		ChannelInfo channelInfo = new ChannelInfo();
		channelInfo.setChannelId(channel);
		channelInfo.setNeedAuthIp(channelMap.get("F_AUTH_IP") == null ? "" : channelMap.get("F_AUTH_IP").toString());
		channelInfo.setNeedAuthPwd(channelMap.get("F_AUTH_PWD") == null ? "" : channelMap.get("F_AUTH_PWD").toString());
		channelInfo.setNeedAuthLi(channelMap.get("F_AUTH_LI") == null ? "" : channelMap.get("F_AUTH_LI").toString());
		channelInfo.setUserNum(channelMap.get("F_USER") == null ? "" : channelMap.get("F_USER").toString());
		channelInfo.setPassword(channelMap.get("F_PASSWORD") == null ? "" : channelMap.get("F_PASSWORD").toString());
		channelInfo.setNeedFlowContrl(channelMap.get("F_FLOW_CONTRL") == null ? "" : channelMap.get("F_FLOW_CONTRL").toString());
		channelInfo.setMaxConnNum(channelMap.get("F_MAX_CONNNUMS") == null ? "" : channelMap.get("F_MAX_CONNNUMS").toString());
		channelInfo.setAppId(channelMap.get("APP_ID") == null ? "" : channelMap.get("APP_ID").toString());
		channelInfo.setAccessToken(channelMap.get("ACCESS_TOKEN") == null ? "" : channelMap.get("ACCESS_TOKEN").toString());
		channelInfo.setSign(channelMap.get("SIGN") == null ? "" : channelMap.get("SIGN").toString());

		return channelInfo;
	}

	/**
	 * 获取渠道詳情信息
	 * 
	 * @param channel
	 *            渠道编码
	 * @param date
	 *            日期
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<ChannelLimitInfo> getChannelLimitInfoList(String channel, String date)
	{
		List<ChannelLimitInfo> resList = null;
		Map channelMap = (Map) (this.getCache().get(IMemcachedKeys.IChannelKeys.CHANNEL_LIMIT_KEY_PRE + channel));

		if ( channelMap == null )
		{
			channelMap = new HashMap<String, Object>();
			this.getCache().add(IMemcachedKeys.IChannelKeys.CHANNEL_LIMIT_KEY_PRE + channel, channelMap);
		}
		List<Map<String, Object>> tempLst = null;

		// 如果渠道map中尚未含有date日期的详情配置信息 则需要查询数据库获得List 并放入memCached
		if ( !channelMap.containsKey(date) )
		{
			tempLst = this.getJdbcTemplate().queryForList("SELECT * FROM T_CHANNEL_LIMIT WHERE F_CHANNEL_NUM = ? AND F_DATE = ? AND F_STATE = '0' ORDER BY F_START_TIME",
					new String[] { channel, date });
			// if ( tempLst != null && tempLst.size() > 0 )
			// {
			channelMap.put(date, tempLst);
			this.getCache().replace(IMemcachedKeys.IChannelKeys.CHANNEL_LIMIT_KEY_PRE + channel, channelMap);
			// }
		}
		else
		{
			tempLst = (List) channelMap.get(date);
		}

		if ( tempLst != null && tempLst.size() > 0 )
		{
			resList = new ArrayList<ChannelLimitInfo>();
			for (Map<String, Object> m : tempLst)
			{
				if ( m == null || m.isEmpty() )
				{
					continue;
				}
				ChannelLimitInfo channelLimitInfo = new ChannelLimitInfo();
				channelLimitInfo.setChannelId(channel);
				channelLimitInfo.setDate(date);
				channelLimitInfo.setStartTime((String) m.get("F_START_TIME"));
				channelLimitInfo.setEndTime((String) m.get("F_END_TIME"));
				channelLimitInfo.setMaxConnums(m.get("F_MAX_CONNNUMS") == null ? 0 : ((BigDecimal) m.get("F_MAX_CONNNUMS")).intValue());
				channelLimitInfo.setState((String) m.get("F_STATE"));

				resList.add(channelLimitInfo);
			}
		}

		return resList;
	}

	/**
	 * 获得接口连接数
	 * 
	 * @param itfId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IterfaceCapacity getIterfaceCapacity(String itfId)
	{
		IterfaceCapacity iterfaceCapacity = null;
		if ( !StringUtil.isNull(itfId) )
		{
			Map<String, Integer> map = (Map<String, Integer>) this.getCache().get(IMemcachedKeys.ILinterfaceKeys.LINTERFACE_LIMIT_CAPACITY_KEY);
			if ( map == null )
			{
				map = new HashMap<String, Integer>();
				// 查询数据库
				List<Map<String, Object>> list = this.getJdbcTemplate().queryForList("SELECT * FROM T_LINTERFACE_LIMIT_CAPACITY");
				if ( list != null && list.size() > 0 )
				{
					// 存入缓存当中
					for (Map<String, Object> m : list)
					{
						map.put((String) m.get("F_LI_NUMBER"), (m.get("F_NUMS") == null ? 1 : ((BigDecimal) m.get("F_NUMS")).intValue()));
					}
				}

				// 存入缓存
				this.getCache().add(IMemcachedKeys.ILinterfaceKeys.LINTERFACE_LIMIT_CAPACITY_KEY, map);
			}

			// 设置对象
			iterfaceCapacity = new IterfaceCapacity();
			iterfaceCapacity.setItfId(itfId);
			iterfaceCapacity.setNums(map.get(itfId) == null ? 1 : map.get(itfId));
		}
		return iterfaceCapacity;
	}

	/**
	 * 获得渠道额外参数配置
	 * 
	 * @param channel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getChannelExtParams(String channel)
	{
		Map<String, String> map = null;
		if ( StringUtil.isNull(channel) ) { return map; }

		String key = IMemcachedKeys.IChannelKeys.CHANNEL_EXT_PARAMS_PRE + channel;
		// 从缓存中获得map
		map = (Map<String, String>) this.getCache().get(key);
		if ( map == null )
		{
			map = new HashMap<String, String>();
			List<Map<String, Object>> list = this.getJdbcTemplate().queryForList("select * from T_CHANNEL_EXT_PARAMS where F_CHANNEL_NUM = ?", new Object[] { channel });
			if ( list != null && list.size() > 0 )
			{
				Iterator<Map<String, Object>> iterator = list.iterator();
				Map<String, Object> tmpMap = null;
				while (iterator.hasNext())
				{
					tmpMap = iterator.next();
					map.put((String) tmpMap.get("F_PARAMS_KEY"), (String) tmpMap.get("F_PARAMS_VALUE"));
				}
			}

			this.getCache().add(key, map);

		}
		return map;
	}
	
	
	/**
	 * 根据渠道号、地市 判断ECP服务是否对该渠道的地市开放
	 */
	public Map<String,String> getChannelLimitCityInfos() throws DAOException 
	{
		// redis 存取 渠道、地市 标识
		
		Map<String,String> obsh_city_maps = (Map<String,String>)XWECPApp.redisCli.get(("CHANNEL_TO_CITYS_FLAG").getBytes());
		
		if(null == obsh_city_maps || obsh_city_maps.size() < 0)
		{
			obsh_city_maps =  new HashMap<String,String>();
			String channel_num = "";
			String city_num = "";
			String flag = "";
			String sql = "select t.F_CHANNEL_NUM,t.F_CITY_NUM,t.F_OPEN_FLAG from T_CITY_CURRENT_LIMIT t ";
			Object[] objects = new Object[]{};
			List<Map<String,String>> ret = (List<Map<String,String>>)this.getJdbcTemplate().queryForList(sql,objects);
			if(ret != null && ret.size() > 0)
			{
				
				for(Map<String,String> m : ret) 
				{
					channel_num = StringUtil.convertNull((String)m.get("F_CHANNEL_NUM"));
					city_num = StringUtil.convertNull((String)m.get("F_CITY_NUM"));
					flag = StringUtil.convertNull((String)m.get("F_OPEN_FLAG"));
					obsh_city_maps.put(channel_num+"_"+city_num, flag);
				}
			}
			XWECPApp.redisCli.set("CHANNEL_TO_CITYS_FLAG", obsh_city_maps);
		}
		
		
		return obsh_city_maps;
	}
}
