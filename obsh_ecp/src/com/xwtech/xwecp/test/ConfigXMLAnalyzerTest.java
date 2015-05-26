package com.xwtech.xwecp.test;


import java.io.File;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.config.ServiceConfigAnalyzer;


public class ConfigXMLAnalyzerTest
{
	private static final Logger logger = Logger.getLogger(ConfigXMLAnalyzerTest.class);
	
	public static void main(String args[]) throws Exception
	{
		ServiceConfigAnalyzer sca = new ServiceConfigAnalyzer();
		ServiceConfig sc = sca.readFromFile(new File("D:\\MyProjects\\网营产品化\\meta.xml"));
		logger.info(sc.toString());
	}
	
	private static String[] getEvaluateExpression(String expression)
	{
		if(expression.toLowerCase().startsWith("xpath("))
		{
			expression = expression.substring(6, expression.length() - 1);
			logger.debug("parsed xpath expression: " + expression);
			return new String[]{"xpath", expression};
		}
		return new String[]{"UNKNOWN", ""};
	}
}
