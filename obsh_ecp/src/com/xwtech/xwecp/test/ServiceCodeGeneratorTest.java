package com.xwtech.xwecp.test;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.config.ServiceConfigAnalyzer;

public class ServiceCodeGeneratorTest
{
	private static final Logger logger = Logger.getLogger(ConfigXMLAnalyzerTest.class);
	public static void main(String args[]) throws Exception
	{
		ServiceConfigAnalyzer sca = new ServiceConfigAnalyzer();
		List<ServiceConfig> list = sca.readFromFolder(new File("D:\\MyProjects\\网营产品化\\meta"));
		//new ServiceImplGenerator_Client().generateCodes(list, "D:\\SVN_CHECKOUT\\XWECP\\开发类\\源代码\\ecp\\src");
		//new ServiceInterfaceGenerator().generateCodes(list, "D:\\SVN_CHECKOUT\\XWECP\\开发类\\源代码\\ecp\\src");
	}
}
