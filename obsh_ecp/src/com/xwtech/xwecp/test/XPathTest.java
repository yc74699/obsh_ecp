package com.xwtech.xwecp.test;


import java.io.StringBufferInputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;


public class XPathTest
{
	private static final Logger logger = Logger.getLogger(XPathTest.class);
	
	public static void main(String args[]) throws Exception
	{
		String xml = "<test><t1>hello, xpath!</t1><t2><t>1</t><t>2</t><t>3</t></t2></test>";
		StringBufferInputStream sbi = new StringBufferInputStream(xml);
		InputSource in = new InputSource(sbi);
		//SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		//SAXParser parser = saxFactory.newSAXParser();
		//parser.parse(sbi, new DefaultHandler());
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expression = xpath.compile("/");
		Object root = expression.evaluate(in, XPathConstants.NODE);
		String s = xpath.evaluate("./test/t1/text()", root);
		//Object node = xpath.evaluate("./t2", root, XPathConstants.NODE);
		String s1 = xpath.evaluate("./test/t2/t[3]/text()", root);
		logger.info(s);
		logger.info(s1);
	}
}
