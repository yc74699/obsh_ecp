package com.xwtech.xwecp.test;


import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;


public class JDOMXPathTest
{
	private static final Logger logger = Logger.getLogger(JDOMXPathTest.class);
	
	public static void main(String args[]) throws Exception
	{
		File f = new File("D:\\MyProjects\\网营产品化\\boss-simulate\\ret-B10011.xml");
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(f);
		XPath xpath = new JDOMXPath("/");
		Object root = xpath.evaluate(doc);
		XPath xpath2 = new JDOMXPath("/result/user-id/text()");
		Object obj = xpath2.evaluate(doc);
		logger.info(((java.util.List)(obj)).get(0));
		logger.info(obj.getClass().getName());
		logger.info(((java.util.List)(obj)).get(0).getClass().getName());
		logger.info("=>obj: " + obj);
		
		XPath xpath3 = new JDOMXPath("/result/user-history");
		Object obj1 = xpath3.evaluate(doc);
		xpath = new JDOMXPath("./history");		
		List list = xpath.selectNodes(obj1);
		for(int i = 0;i<list.size();i++)
		{
			//logger.info(list.get(i).getClass().getName());
		}
		
		XPath xpath4 = new JDOMXPath("/result/user-name/@pp");
		Object obj4 = xpath4.evaluate(doc);
		logger.info(obj4.getClass().getName());
		logger.info(((java.util.List)(obj4)).get(0).getClass().getName());
	}
}
