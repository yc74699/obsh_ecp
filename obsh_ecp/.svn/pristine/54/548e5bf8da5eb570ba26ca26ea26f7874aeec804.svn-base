package com.xwtech.xwecp.util;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * XML工具类
 * @param 
 * @return
 */
public class XMLUtil {
	
	private static final Logger logger = Logger.getLogger(XMLUtil.class);
	
	private static Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
	
	/**
	 * 获取根据节点
	 * @param respXML
	 * @return
	 */
	public static Element getRootElementEx(String respXML) throws Exception
	{
		return getElement(respXML.getBytes("UTF-8"));
	}
	
	/**
	 * 获取根据节点
	 * @param respXML
	 * @return
	 */
	public static Element getRootElement(String respXML)
	{
		return getElement(respXML.getBytes());
	}
	
	public static Element getElement(byte[] tmp)
	{
		Element root = null;
		try
		{
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return root;
	}
	
	/**
	 * 获取具体节点值
	 * @param root
	 * @param keys
	 * @return
	 */
	public static String getChildText(Element element, String ...keys )
	{
		try
		{
			for (int i = 0; i < keys.length-1; i++) 
			{
				element = element.getChild(keys[i]);
			}
			return pattern.matcher(element.getChildText(keys[keys.length-1])).replaceAll("");
		}catch (Exception e) 
		{
			return null;
		}
	}
	
	/**
	 * 根据请求报文,获取具体节点值
	 * 如<response><resp_code>0000<resp_code/></response>
	 * @param respXML
	 * @param keys
	 * @return
	 */
	public static String getChildText(String respXML, String ...keys )
	{
		Element root = getRootElement(respXML);
		try
		{
			for (int i = 0; i < keys.length-1; i++) 
			{
				root=root.getChild(keys[i]);
			}
			return pattern.matcher(root.getChildText(keys[keys.length-1])).replaceAll("");
		}catch (Exception e) 
		{
			return null;
		}
	}
	
	/**
	 * 根据请求报文,获取具体节点值
	 * 如<response><resp_code>0000<resp_code/></response>
	 * @param respXML
	 * @param keys
	 * @return
	 */
	public static String getChildTextEx(String respXML, String ...keys )
	{
		Element root = null;
		try {
			root = getRootElementEx(respXML);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			for (int i = 0; i < keys.length-1; i++) 
			{
				root=root.getChild(keys[i]);
			}
			return pattern.matcher(root.getChildText(keys[keys.length-1])).replaceAll("");
		}catch (Exception e) 
		{
			return null;
		}
	}
	
	/**
	 * 获取list节点类型
	 * @param respXML
	 * @param keys
	 * @return
	 */
	public static List getChildList(String respXML, String ...keys )
	{
		Element root = getRootElement(respXML);
		try
		{
			for (int i = 0; i < keys.length-1; i++) 
			{
				root=root.getChild(keys[i]);
			}
			return root.getChildren(keys[keys.length-1]);
		}catch (Exception e) 
		{
			return null;
		}
	}
	
	
	public static Element getChild(Element element, String keys )
	{
		try
		{
			return element.getChild(keys);
		}catch (Exception e) 
		{
			return null;
		}
	}
}
