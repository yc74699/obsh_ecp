package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * XML解析类
 * @author yuantao
 *
 */
public class ParseXmlConfig implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4673796023084436158L;

	public static final Logger logger = Logger.getLogger(ParseXmlConfig.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	
	public ParseXmlConfig ()
	{
		
	}
	
	/**
	 * 获取子节点信息
	 * @param e
	 * @param childName
	 * @return
	 */
	public String getChildText(Element e, String childName)
	{
		String str = "";
		
		try
		{
			if (null != e && null != childName && !"".equals(childName))
			{
				str = e.getChildText(childName)==null?"":e.getChildText(childName).trim();
			}
		}
		catch(Exception ex)
		{
			str = "";
		}
		
		return str;
	}
	
	/**
	 * 获取content下父节点信息
	 * @param root
	 * @param name
	 * @return
	 */
	public List getContentList(Element root, String name)
	{
		List list = null;
		try
		{
			if (null != root && null != name && !"".equals(name))
			{
				list = root.getChild("content").getChildren(name);
			}
		}
		catch (Exception e)
		{
			list = null;
		}
		return list;
	}
	
	/**
	 * 获取子节点
	 * @param e
	 * @param name
	 * @return
	 */
	public Element getElement(Element e, String name)
	{
		Element dt = null;
		try
		{
			if (null !=  e && null != name && !"".equals(name))
			{
				dt = e.getChild(name);
			}
		}
		catch (Exception ex)
		{
			dt = null;
		}
		return dt;
	}
	
	/**
	 * 解析报文
	 * @param xml
	 * @return
	 */
	public Element getElement(String xml)
	{
		Element root = null;
		try
		{
			if (null != xml && !"".equals(xml))
			{
				ByteArrayInputStream ins = new ByteArrayInputStream(xml.getBytes());
				SAXBuilder sax = new SAXBuilder();
				Document doc = sax.build(ins);
				root = doc.getRootElement();
			}
			return root;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return root;
	}
	
	/**
	 * 返回年月
	 * @return yyyyMM
	 */
	public static String getTodayChar6(){
		return DateFormatUtils.format(new Date(), "yyyyMM");
	}
	
	/**
	 * 比对两个时间间隔
	 * @param startDateTime 开始时间
	 * @param endDateTime 结束时间
	 * @param distanceType 计算间隔类型 天d 小时h 分钟m 秒s
	 * @return
	 */
    public static String getDistanceDT(String startDateTime,String endDateTime,String distanceType){
        String strResult="";
        long lngDistancVal=0;
        try{
            SimpleDateFormat tempDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date startDate=tempDateFormat.parse(startDateTime);
            Date endDate=tempDateFormat.parse(endDateTime);
            if(distanceType.equals("d")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            }else if(distanceType.equals("h")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
            }else if(distanceType.equals("m")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60);
            }else if(distanceType.equals("s")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000);
            }
            strResult=String.valueOf(lngDistancVal);
        }catch(Exception e){
        	strResult="0";
        }
        return strResult;
    }
    
    /**
	 * 返回 年月日小时分秒
	 * @return
	 */
	public static String getTodayChar14(){
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
	}
	
	/**
	 * 获取今日年份  
	 * @return yyyy
	 */
	public static String getTodayYear(){
		return DateFormatUtils.format(new Date(), "yyyy");	
	}
	
	/**
	 * 将字符串按指定格式转换成日期型
	 * @param str
	 * @param format
	 * @return
	 */
	public Date stringToDate(String str, String format)
	{
		try
		{
			if (null != str && !"".equals(str) && null != format && !"".equals(format))
			{
				sdf.applyPattern(format);
				return sdf.parse(str);
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return null;
	}
	
	/**
	 * 将字符串转换成日期型
	 * @param str
	 * @return
	 */
	public Date stringToDate(String str)
	{
		String format = "";
		try
		{
			if (null != str && !"".equals(str))
			{
				if (14 == str.length())
				{
					format = "yyyyMMddHHmmss";
				}
				else if (8 == str.length())
				{
					format = "yyyyMMdd";
				}
				else if (6 == str.length())
				{
					format = "yyyyMM";
				}
				if (!"".equals(format))
				{
					sdf.applyPattern(format);
					return sdf.parse(str);
				}
			}
		}
		catch (Exception e)
		{
			return null;
		}
		return null;
	}
	
	/**
	 * 将日期转换成指定格式
	 * @param date
	 * @param format
	 * @return
	 */
	public String dateToString(Date date, String format)
	{
		try
		{
			if (null != date && null != format && !"".equals(format))
			{
				sdf.applyPattern(format);
				return sdf.format(date);
			}
		}
		catch (Exception e)
		{
			return "";
		}
		return "";
	}
}
