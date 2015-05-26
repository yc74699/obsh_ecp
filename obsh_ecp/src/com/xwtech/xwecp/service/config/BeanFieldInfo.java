package com.xwtech.xwecp.service.config;


import org.apache.log4j.Logger;


public class BeanFieldInfo
{
	private static final Logger logger = Logger.getLogger(BeanFieldInfo.class);
	
	protected String name;
	
	protected String dataType;
	
	protected String className;
	
	protected String defaultValue;

	

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}
}
