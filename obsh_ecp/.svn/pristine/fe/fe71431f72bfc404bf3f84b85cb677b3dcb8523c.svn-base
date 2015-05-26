package com.xwtech.xwecp;


import org.apache.log4j.Logger;

import com.xwtech.xwecp.util.PropertiesConfiguration;


public class AppConfig
{
	private static final Logger logger = Logger.getLogger(AppConfig.class);
	
	public static PropertiesConfiguration getConfig(String name)
	{
		if(XWECPApp.SPRING_CONTEXT != null)
		{
			Object obj = XWECPApp.SPRING_CONTEXT.getBean(name);
			if(obj instanceof PropertiesConfiguration)
			{
				return (PropertiesConfiguration)obj;
			}
		}
		return null;
	}
	
	public static String getConfigValue(String name, String key)
	{
		PropertiesConfiguration p = getConfig(name);
		if(p != null)
		{
			return p.get(key);
		}
		return null;
	}
	
	public static String getConfigValue(String name, String key, String def)
	{
		PropertiesConfiguration p = getConfig(name);
		if(p != null)
		{
			return p.get(key, def);
		}
		return null;
	}
	
	public static int getIntConfigValue(String name, String key)
	{
		PropertiesConfiguration p = getConfig(name);
		if(p != null)
		{
			return p.getIntConfig(key);
		}
		return 0;
	}
	
	public static int getIntConfigValue(String name, String key, int def)
	{
		PropertiesConfiguration p = getConfig(name);
		if(p != null)
		{
			return p.getIntConfig(key, def);
		}
		return def;
	}
	
	public static long getLongConfigValue(String name, String key)
	{
		PropertiesConfiguration p = getConfig(name);
		if(p != null)
		{
			return p.getLongConfig(key);
		}
		return 0;
	}
	
	public static long getLongConfigValue(String name, String key, long def)
	{
		PropertiesConfiguration p = getConfig(name);
		if(p != null)
		{
			return p.getLongConfig(key, def);
		}
		return def;
	}
	
	public static double getDoubleConfigValue(String name, String key)
	{
		PropertiesConfiguration p = getConfig(name);
		if(p != null)
		{
			return p.getDoubleConfig(key);
		}
		return 0;
	}
	
	public static double getDoubleConfigValue(String name, String key, double def)
	{
		PropertiesConfiguration p = getConfig(name);
		if(p != null)
		{
			return p.getDoubleConfig(key, def);
		}
		return def;
	}
}
