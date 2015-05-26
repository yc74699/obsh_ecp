package com.xwtech.xwecp.test;


import org.apache.log4j.Logger;

import com.xwtech.xwecp.memcached.IMemcachedManager;
import com.xwtech.xwecp.memcached.MemcachedManagerImpl;


public class CacheTest
{
	private static final Logger logger = Logger.getLogger(CacheTest.class);
	
	public static void main(String args[]) throws Exception
	{
		MemcachedManagerImpl cache = new MemcachedManagerImpl();
		cache.setServers("127.0.0.1:6001,127.0.0.1:6002,127.0.0.1:6003,127.0.0.1:6004");
		cache.setWeights("1,1,1,1");
		cache.setCompressEnable("true");
		cache.setInitConn("5");
		cache.setMaintSleep("30");
		cache.setNagle("false");
		cache.setCompressThreshold("65530");
		cache.setSocketTo("3000");
		cache.setSocketConnectTo("0");
		cache.setMaxConn("250");
		cache.setMaxIdle("21600000");
		cache.initialize();
		
		String key = "JJJJJ";
		
		String curr = null;
		
		while(true)
		{
			String s = (String)(cache.get(key));
			if(curr == null)
			{
				curr = s;
			}
			if(curr != null)
			{
				if(!curr.equals(s))
				{
					logger.error("not equals!");
					throw new Exception();
				}
			}
			String generate = GenerateCode();
			if(s != null)
			{
				if(cache.delete(key))
				{
					cache.add(key, generate);
				}
			}
			curr = generate;
			Thread.sleep(100);			
		}
	}
	
	private static String GenerateCode()
	{
		return "" + Math.random() * 1000;
	}
}
