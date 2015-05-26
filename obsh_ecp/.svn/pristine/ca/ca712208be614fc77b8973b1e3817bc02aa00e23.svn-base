/*
 * Copyright 2007 XWTECH INC. All Rights reserved
 * XWTECH INC.
 * 创建日期: 2007/12/03
 * 创建人：  黄毅
 * 修改履历：
 * 	2008-1-8， 田承平， 增加两个14字符串格式的互转方法
 */
package com.xwtech.xwecp.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>
 * 对日期操作类.<BR>
 * </p>
 * </p>
 * 
 * @author huangyi
 * @version 1.0
 */
public class DateOperator {

	/** 14字符格式的日期 */
	private final static String FORMAT_CHAR14 = "yyyyMMddHHmmss";
	
	/** 8字符格式的日期 */
	private final static String FORMAT_CHAR8 = "yyyyMMdd";
	
	/** 6字符格式的日期 */
	private final static String FORMAT_CHAR6 = "yyyyMM";
	
	/**
	 * 将指定Date类型参数转换为指定的Oracle日期时间格式字符串
	 * 
	 * @param inputDate
	 *            传入Date类型参数
	 * @return String
	 */
	public static String getOracleDate(Date inputDate)
			throws NullPointerException {
		if (null == inputDate) {
			throw new NullPointerException("The input date is null.");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		return sdf.format(inputDate);
	}

	/**
	 * 获取系统当前时间的字符串
	 * 
	 * @return String
	 */
	public static String getSystemTime() {
		GregorianCalendar cal = new GregorianCalendar();

		return getOracleDate(cal.getTime());
	}

	/**
	 * 将给定的Date类型参数转换为指定的日期时间格式字符串
	 * 
	 * @param inputDate
	 *            传入的Date类型参数
	 * @param inputFormat
	 *            转换的日期时间格式
	 * @return String
	 */
	public static String formatDate(Date inputDate, String inputFormat)
			throws NullPointerException, IllegalArgumentException {
		if (null == inputDate) {
			throw new NullPointerException("The input date is null.");
		}

		if (null == inputFormat) {
			throw new NullPointerException("The input format is null.");
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);

			return sdf.format(inputDate);
		} catch (Exception ex) {
			throw new IllegalArgumentException("The input format is invalid.");
		}
	}

	/**
	 * 将给定的Timestamp类型参数转换为指定的日期时间格式字符串
	 * 
	 * @param inputDate
	 *            传入的Timestamp类型参数
	 * @param inputFormat
	 *            转换的日期时间格式
	 * @return String
	 */
//	public static String formatDate(Timestamp inputDate, String inputFormat) {
//		return formatDate(inputDate, inputFormat);
//	}

	/**
	 * 将给定的Timestamp类型参数转换为默认的日期时间格式(yyyy年MM月dd日hh时)字符串
	 * 
	 * @param inputDate
	 *            传入的Timestamp类型参数
	 * @return String
	 */
	public static String changeHourTo24(Timestamp inputDate) {
		if (null == inputDate) {
			throw new NullPointerException("The input Date is null.");
		}

		String dateStyle = "yyyy��MM��dd��hhʱ";

		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);

			if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
				cal.add(Calendar.DATE, -1);
			}

			SimpleDateFormat sdf = new SimpleDateFormat(dateStyle);

			return sdf.format(cal.getTime());
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 根据指定年、月、日转换为Date类型
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return Date
	 */
	public static Date changeYMDToDate(String year, String month, String day) {

		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = year + "/" + month + "/" + day;
		Date date = null;
		try {
			date = bartDateFormat.parse(strDate);
		} catch (Exception ex) {
			date = null;
		}
		return date;
	}

	/**
	 * 将指定的日期时间格式字符串转换为Calendar类型
	 * 
	 * @param inputDateTime
	 *            传入日期时间格式字符串参数
	 * @return Calendar
	 */
	public static Calendar getDateTime(String inputDateTime) throws Exception {

		SimpleDateFormat bartDateFormat = null;
		boolean dateFlag = false;
		Date date = null;
		try {
			bartDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			date = bartDateFormat.parse(inputDateTime);
			dateFlag = true;
		} catch (Exception ex) {
			dateFlag = false;
		}

		if (!dateFlag) {
			try {
				bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = bartDateFormat.parse(inputDateTime);
				dateFlag = true;
			} catch (Exception ex) {
				dateFlag = false;
			}
		}

		if (!dateFlag) {
			try {
				bartDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				date = bartDateFormat.parse(inputDateTime);
				dateFlag = true;
			} catch (Exception ex) {
				throw ex;
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}
	
	/**
	 * 普通字符串转换成日期形式的字符串
	 * 
	 * @param bYmd 传入普通字符串参数
	 * @return String
	 */
	public static String dateToString(String bYmd){  
		  
		  if (bYmd == null || bYmd.trim().length() == 0){   
			return   "您没有传入需要转换的字符串！";   
		  }else if (bYmd.length() == 8) {   
				   bYmd = bYmd.substring(0,4) + "-" + bYmd.substring(4,6) + "-" + bYmd.substring(6);      
			    }else if(bYmd.length() == 6){
			    		bYmd = bYmd.substring(0, 2) + ":" + bYmd.substring(2, 4)+":"+ bYmd.substring(4);
			          }else if(bYmd.length() == 14){
			                  bYmd = bYmd.substring(0,4) + "-" + bYmd.substring(4,6) + "-" + bYmd.substring(6,8)+
					   " " + bYmd.substring(8,10) + ":" +bYmd.substring(10,12) + ":" + bYmd.substring(12);        
			          }

		  return   bYmd;   
	 }
	
	/**
	 * 比如：125秒 转换成  00:02:05
	 * 
	 * @param bYmd 传入长整型参数
	 * @return String(格式：HH:MM:SS)
	 */
	public static String returnTime(Long bYmd){
		
		String result = "";
		int hour = (int)(bYmd / 3600);
		int minute = (int)(bYmd%3600/60);
		int second = (int)(bYmd%3600%60);
		result = (hour<10?("0"+hour):hour)+":" + (minute<10?("0"+minute):minute) + ":" 
		  + (second<10?("0"+second):(second+""));
		return result;
	}
	
	/**
	 * 把日期转换成14字符的格式类型字符串，如20080108111225
	 * @param date 日期
	 * @return String
	 */
	public static String getChar14(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_CHAR14);
		return df.format(date);
	}
	
	/**
	 * 把日期转换成8字符的格式类型字符串，如20080108
	 * @param date 日期
	 * @return String
	 */
	public static String getChar8(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_CHAR8);
		return df.format(date);
	}
	
	/**
	 * 把日期转换成6字符的格式类型字符串，如200801
	 * @param date 日期
	 * @return String
	 */
	public static String getChar6(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_CHAR6);
		return df.format(date);
	}
	
	/**
	 * 把14字符的格式日期字符串转换成日期
	 * @param char14  14字符的格式日期字符串，如20080108111225
	 * @return Date
	 */
	public static Date fromChar14(String char14)
	{
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_CHAR14);
		try 
		{
			return df.parse(char14);
		} 
		catch (ParseException e) 
		{
			return new Date();
		}
	}
	
	/**
	 * 把14字符的格式日期字符串转换成日期
	 * @param char6  14字符的格式日期字符串，如200801
	 * @return Date
	 */
	public static Date fromChar6(String char6)
	{
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_CHAR6);
		try 
		{
			return df.parse(char6);
		} 
		catch (ParseException e) 
		{
			return new Date();
		}
	}
	
	/**
	 * 把14字符的格式日期字符串转换成日期
	 * @param char14  14字符的格式日期字符串，如2008-01-08
	 * @return Date
	 */
	public static String fromChar14toStandard(String charDateTime)
	{
		
		String  strResult = "";
		if(null != charDateTime && !"".equals(charDateTime) && charDateTime.length() >= 8){
			strResult=charDateTime.substring(0, 4) + "-"
			+ charDateTime.substring(4, 6) + "-"
		    + charDateTime.substring(6, 8);
		}
		return strResult;
	}
	
	/**
     * 检验输入是否为正确的日期格式,严格要求日期正确性,格式:yyyyMM
     * @param sourceDate
     * @return
     */
	public static boolean checkDate(String sourceDate){
		if(sourceDate==null){
			return false;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
			dateFormat.setLenient(false);
			dateFormat.parse(sourceDate);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	public static String getFullTimes(int theSecond)   {   
		int h = 0, m = 0, s = 0;   

		h = (int)Math.floor(theSecond/3600);   
		if(h > 0) {   
			m = (int)Math.floor((theSecond-h*3600)/60);   
			if(m > 0) {   
				s = theSecond-h*3600-m*60;   
			}else {   
				s = theSecond-h*3600;     //原来这里写错了,写成了s   =   theSecond;   
			} 
		}else { 
			m = (int)Math.floor(theSecond/60);   
			if(m > 0) {   
				s = theSecond-m*60;   
			}else {   
				s = theSecond;   
			}   
		}   
		return   h + ":" + m + ":" + s;   
	}
	
}
