package com.xwtech.xwecp;

import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.Jedis.RedisClient;


public class XWECPApp
{
	public static String APP_PATH = "";
	
	public static ApplicationContext SPRING_CONTEXT = null;
	
	public static RedisClient redisCli = null;
}