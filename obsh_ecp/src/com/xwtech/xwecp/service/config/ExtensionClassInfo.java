package com.xwtech.xwecp.service.config;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class ExtensionClassInfo
{
	private static final Logger logger = Logger.getLogger(ExtensionClassInfo.class);
	
	private String className;
	
	private List<BeanFieldInfo> fields = new ArrayList<BeanFieldInfo>();

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public List<BeanFieldInfo> getFields()
	{
		return fields;
	}

	public void setFields(List<BeanFieldInfo> fields)
	{
		this.fields = fields;
	}
}
