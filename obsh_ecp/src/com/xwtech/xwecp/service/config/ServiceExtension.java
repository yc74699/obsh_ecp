package com.xwtech.xwecp.service.config;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class ServiceExtension
{
	private static final Logger logger = Logger.getLogger(ServiceExtension.class);
	
	private List<ExtensionClassInfo> extensions = new ArrayList<ExtensionClassInfo>();

	public List<ExtensionClassInfo> getExtensions()
	{
		return extensions;
	}

	public void setExtensions(List<ExtensionClassInfo> extensions)
	{
		this.extensions = extensions;
	}
}
