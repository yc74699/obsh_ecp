package com.xwtech.xwecp.memcached;

public interface IMemcachedManager
{
	Object get(String key);
	
	boolean add(String key, Object value);
	
	boolean add(String key, Object value, long expireInSeconds);
	
	boolean delete(String key);
	
	boolean replace(String key, Object value);
	
	boolean replace(String key, Object value, long expireInSeconds);
}
