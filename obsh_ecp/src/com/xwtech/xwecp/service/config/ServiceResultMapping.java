package com.xwtech.xwecp.service.config;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class ServiceResultMapping
{
	private static final Logger logger = Logger.getLogger(ServiceResultMapping.class);
	
	private String name;
	
	private String expression;
	
	private String valueMapStr;
	
	private Map<String, String> valueMap;
	
	private List<ServiceResultMapping> childResultMapping = new ArrayList<ServiceResultMapping>();

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(String expression)
	{
		this.expression = expression;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<ServiceResultMapping> getChildResultMapping()
	{
		return childResultMapping;
	}

	public void setChildResultMapping(List<ServiceResultMapping> childResultMapping)
	{
		this.childResultMapping = childResultMapping;
	}

	public String getValueMapStr()
	{
		return valueMapStr;
	}

	public void setValueMapStr(String valueMapStr)
	{
		this.valueMapStr = valueMapStr;
	}
	
	public synchronized Map<String, String> getValueMap()
	{
		if(this.valueMap == null)
		{
			this.valueMap = new HashMap<String, String>();
			this.initValueMap();
		}
		return this.valueMap;
	}
	
	//format: 1:12;2:123,234,345,456;12:*;...   冒号前面的是XWECP的编码, 后面的是BOSS的编码 
	private void initValueMap()
	{
		if(this.valueMapStr != null)
		{
			String []pairs = this.valueMapStr.split(";");
			for(int i = 0;i<pairs.length;i++)
			{
				String part = pairs[i];
				String[] subPart = part.split(":");
				if(subPart.length == 2)
				{
					String xwecpCode = subPart[0];
					String bossCodes = subPart[1];
					if(bossCodes.equals("*"))
					{
						this.valueMap.put("*", xwecpCode);
					}
					else
					{
						String []bossCodeParts = bossCodes.split(",");
						for(int j = 0;j<bossCodeParts.length;j++)
						{
							this.valueMap.put(bossCodeParts[j], xwecpCode);
						}
					}
				}
				else
				{
					logger.error("忽悠值转换配置: ["+part+"]!");
				}
			}
		}
	}
}
