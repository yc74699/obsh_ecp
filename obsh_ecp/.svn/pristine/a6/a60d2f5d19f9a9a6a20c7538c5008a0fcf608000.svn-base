package com.xwtech.xwecp.service.config;


import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;


public class ServiceConfig
{
	private static final Logger logger = Logger.getLogger(ServiceConfig.class);
	
	protected String namespace;
	
	protected String name;
	
	protected String cmd;
	
	protected String chineseName;
	
	protected String description;
	
	protected ServiceInput input;
	
	protected ServiceOutput output;
	
	protected ServiceImplementation impl;
	
	protected ServiceExtension extension;
	
	private Map<String, BeanFieldInfo> fieldInfoMap = new HashMap<String, BeanFieldInfo>();

	public String getChineseName()
	{
		return chineseName;
	}

	public void setChineseName(String chineseName)
	{
		this.chineseName = chineseName;
	}

	public String getCmd()
	{
		return cmd;
	}

	public void setCmd(String cmd)
	{
		this.cmd = cmd;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public ServiceExtension getExtension()
	{
		return extension;
	}

	public void setExtension(ServiceExtension extension)
	{
		this.extension = extension;
	}

	public ServiceImplementation getImpl()
	{
		return impl;
	}

	public void setImpl(ServiceImplementation impl)
	{
		this.impl = impl;
	}

	public ServiceInput getInput()
	{
		return input;
	}

	public void setInput(ServiceInput input)
	{
		this.input = input;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	public ServiceOutput getOutput()
	{
		return output;
	}

	public void setOutput(ServiceOutput output)
	{
		this.output = output;
	}	
	
	public Map<String, BeanFieldInfo> getFieldInfoMap()
	{
		return fieldInfoMap;
	}

	public void setFieldInfoMap(Map<String, BeanFieldInfo> fieldInfoMap)
	{
		this.fieldInfoMap = fieldInfoMap;
	}
	
	public void initOutputFieldInfoMap()
	{
		fieldInfoMap = new HashMap<String, BeanFieldInfo>();
		if(this.getOutput().getOutputFields() != null)
		{
			for(int i = 0;i<this.getOutput().getOutputFields().size();i++)
			{
				BeanFieldInfo bfi = this.getOutput().getOutputFields().get(i);
				Stack stack = new Stack();
				fieldInfoMap.put(bfi.getName(), bfi);
				if(DataTypeConstants.LIST.equalsIgnoreCase(bfi.getDataType()) || DataTypeConstants.CLASS.equalsIgnoreCase(bfi.getDataType()))
				{
					stack.push(bfi.getName());
					recusiveInitBeanFieldMap(bfi, stack);
					stack.pop();
				}
			}
		}
		//logger.debug(this.fieldInfoMap);
	}
	
	private void recusiveInitBeanFieldMap(BeanFieldInfo bfi, Stack stack)
	{
		String className = bfi.getClassName();
		if(className == null || "".equals(className.trim()))
		{
			return;
		}
		ExtensionClassInfo eci = this.getExtensionClass(className);
		if(eci.getFields() != null)
		{
			for(int i = 0;i<eci.getFields().size();i++)
			{
				BeanFieldInfo b = eci.getFields().get(i);
				String name = b.getName();
				String key = getBeanFieldExpression(stack) + "." + name;
				this.fieldInfoMap.put(key, b);
				if(DataTypeConstants.LIST.equalsIgnoreCase(b.getDataType()) || DataTypeConstants.CLASS.equalsIgnoreCase(b.getDataType()))
				{
					stack.push(name);
					recusiveInitBeanFieldMap(b, stack);
					stack.pop();
				}
			}
		}
	}
	
	private String getBeanFieldExpression(Stack objStack)
	{
		StringBuffer expression = new StringBuffer();
		for(int i = 0;i<objStack.size();i++)
		{
			String name = (String)objStack.get(i);
			expression.append(name).append(".");
		}		
		if(expression.length() > 0)
		{
			return expression.substring(0, expression.length() - 1);
		}
		else
		{
			return expression.toString();
		}
	}
	
	private ExtensionClassInfo getExtensionClass(String name)
	{
		if(this.getExtension() == null || this.getExtension().getExtensions() == null || name == null)
		{
			return null;
		}
		for(int i = 0;i<this.getExtension().getExtensions().size();i++)
		{
			ExtensionClassInfo eci = this.getExtension().getExtensions().get(i);
			if(name.equals(eci.getClassName()))
			{
				return eci;
			}
		}
		return null;
	}
}
