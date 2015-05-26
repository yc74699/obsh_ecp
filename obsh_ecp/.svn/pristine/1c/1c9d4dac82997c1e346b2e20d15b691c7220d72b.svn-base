/*
 * Copyright 2008 XWTECH INC. All Rights reserved
 * XWTECH INC.
 * 创建日期: 2008/1/19
 * 创建人：毛旭峰
 * 修改履历：    
 * 2008/1/19 毛旭峰 创建
 */
package com.xwtech.xwecp.util;

import java.util.Collection;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

/**
 * Controller向页面以JSON格式返回时写响应流的工具类
 * @version 1.0
 */
public class JsonResponseUtil
{
	public static final Logger logger = Logger.getLogger(JsonResponseUtil.class);
	
	/**
	 * 转化为JSON对象格式写入响应流
	 * @param response
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public void write(HttpServletResponse response, Object obj)
	{
		JSONObject jsonObj = JSONObject.fromObject(obj);
		doResponse(response, jsonObj.toString());
	}

	/**
	 * 转化为JSON对象格式写入响应流
	 * 
	 * @param response
	 * @param obj collection
	 */
	@SuppressWarnings("unchecked")
	public void write(HttpServletResponse response, Collection obj)
	{
		JSONArray arr = JSONArray.fromObject(obj);
		doResponse(response, arr.toString());
	}

	/**
	 * 转化为JSON对象格式写入响应流
	 * 
	 * @param response
	 * @param obj collection
	 */
	@SuppressWarnings("unchecked")
	public void write(HttpServletResponse response, Object[] obj)
	{
		JSONArray arr = JSONArray.fromObject(obj);
		doResponse(response, arr.toString());
	}

	/**
	 * 将JSON格式的字符串写入响应流
	 * 
	 * @param response
	 * @param jsonStr
	 * @throws IOException
	 */
	public void write(HttpServletResponse response, String jsonStr)
	{
		doResponse(response, jsonStr);
	}

	/**
	 * 将JSON格式的字符串写入响应流
	 * 
	 * @param response
	 * @param jsonStr
	 */
	private void doResponse(HttpServletResponse response, String jsonStr)
	{
		try
		{
			response.getWriter().print(jsonStr);
		}
		catch (Exception e)
		{
			logger.error(e,e);
		}
	}

	/**
	 * 将JSON格式转化对象类型（Object）
	 * @param content
	 * @return object
	 */
	public Object toBeanByJSONLib(String content)
	{
		JSONObject jsonObject = JSONObject.fromObject(content);
		Object bean = JSONObject.toBean(jsonObject);
		return bean;
	}
}
