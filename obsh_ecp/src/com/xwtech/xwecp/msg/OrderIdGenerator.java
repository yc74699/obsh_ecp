package com.xwtech.xwecp.msg;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;


public class OrderIdGenerator
{
	private static final Logger logger = Logger.getLogger(OrderIdGenerator.class);
	
	private String prefix = "";
	
//	private long min = 100000000L;
	
//	private long max = 999999999L;
	
//	private long counter = 100000000L;
	
	private String postfix = "";
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static final String DATE_EXP = "#{date}";
	
	public OrderIdGenerator()
	{
		this(DATE_EXP, 100000000L, 999999999L);
	}
	
	public OrderIdGenerator(String prefix, long min, long max)
	{
		this.prefix = prefix;
//		this.min = min;
//		this.counter = min;
	}
	
	public synchronized String next()
	{
		String pre = this.prefix;
		if(this.prefix.contains(DATE_EXP))
		{
			String dateStr = this.format.format(new Date());
			pre = pre.replaceFirst("\\#\\{date\\}", dateStr);
		}
		this.postfix = (int)(10+Math.random()*90) +"";
		return pre + (postfix);
	}
}
