package com.xwtech.xwecp.test;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.service.logic.client.XWECPLIClient;

public class PerformanceTest
{
	private static final Logger	logger	= Logger.getLogger(PerformanceTest.class);
	
	public static void main(String args[]) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8080/xwecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		
	}
}
