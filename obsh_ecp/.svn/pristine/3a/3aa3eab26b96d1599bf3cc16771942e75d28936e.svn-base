package com.xwtech.xwecp.service.config;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class ServiceImplementation
{
	private static final Logger logger = Logger.getLogger(ServiceImplementation.class);
	
	private boolean directImpl;
	
	private String implClass;
	
	private Map<String, List<ServiceResultMapping>> resultMapping = new HashMap<String, List<ServiceResultMapping>>();
	
	//private List<ServiceResultMapping> resultMapping = new ArrayList<ServiceResultMapping>();
	
	private List<TeletextMapping> teletextMapping = new ArrayList<TeletextMapping>();

	public boolean isDirectImpl()
	{
		return directImpl;
	}

	public void setDirectImpl(boolean directImpl)
	{
		this.directImpl = directImpl;
	}

	public String getImplClass()
	{
		return implClass;
	}

	public void setImplClass(String implClass)
	{
		this.implClass = implClass;
	}
	
	public List<ServiceResultMapping> getResultMapping(String name)
	{
		return resultMapping.get(name);
	}

	public Map<String, List<ServiceResultMapping>> getResultMapping()
	{
		return resultMapping;
	}

	public void setResultMapping(Map<String, List<ServiceResultMapping>> resultMapping)
	{
		this.resultMapping = resultMapping;
	}

	public List<TeletextMapping> getTeletextMapping()
	{
		return teletextMapping;
	}

	public void setTeletextMapping(List<TeletextMapping> teletextMapping)
	{
		this.teletextMapping = teletextMapping;
	}
}
