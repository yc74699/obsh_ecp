package com.xwtech.xwecp.msg;


import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class ServiceMessage
{
	private static final Logger logger = Logger.getLogger(ServiceMessage.class);
	
	private MessageHead head;
	
	private MessageData data;
	
	private static XStream xstream = new XStream(new DomDriver());

	public MessageData getData()
	{
		return data;
	}

	public void setData(MessageData data)
	{
		this.data = data;
	}

	public MessageHead getHead()
	{
		return head;
	}

	public void setHead(MessageHead head)
	{
		this.head = head;
	}
	
	public static ServiceMessage fromXML(String xml)
	{
		Object obj = xstream.fromXML(xml);
		if(obj instanceof ServiceMessage)
		{
			return (ServiceMessage)(obj);
		}
		return null;
	}
	
	public String toString()
	{
		String xml = xstream.toXML(this); // serialize to XML
		return xml;		
	}
}
