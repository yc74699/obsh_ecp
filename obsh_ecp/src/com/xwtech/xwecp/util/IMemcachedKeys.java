package com.xwtech.xwecp.util;

/**
 * MemCached 参数
 * 
 * @author maofw
 * 
 */
public interface IMemcachedKeys
{
	// 渠道memcached key
	public interface IChannelKeys
	{
		// 渠道详情memcached key前缀
		public String CHANNEL_LIMIT_KEY_PRE = "CALLER_CHANNEL_LIMIT_";
		//渠道额外参数配置memcached key前缀
		public String CHANNEL_EXT_PARAMS_PRE = "CHANNEL_EXT_PARAMS_";
	}

	// 接口相关memcached key参数
	public interface ILinterfaceKeys
	{
		//接口连接数memcached key
		public String LINTERFACE_LIMIT_CAPACITY_KEY = "LINTERFACE_LIMIT_CAPACITY";
	}
}
