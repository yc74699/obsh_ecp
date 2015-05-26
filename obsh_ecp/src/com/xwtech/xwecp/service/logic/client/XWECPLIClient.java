package com.xwtech.xwecp.service.logic.client;


import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.communication.HttpCommunicator;
import com.xwtech.xwecp.msg.MessageHelper;
import com.xwtech.xwecp.msg.ServiceMessage;


public class XWECPLIClient
{
	private static final Logger logger = Logger.getLogger(XWECPLIClient.class);
	
	private MessageHelper msgHelper;
	
	private HttpCommunicator platformConnection; 
	
	private static HashMap<String, XWECPLIClient> instanceMap = new HashMap<String, XWECPLIClient>();
	
	public static final String DEFAULT_INSTANCE_NAME = "DEFAULT";
	
	public static final String PROP_CHANNEL = "client.channel";
	
	public static final String PROP_USER = "platform.user";
	
	public static final String PROP_PWD = "platform.password";
	
	public static final String PROP_PLATFORM_URL = "platform.url";
	
	public static final String PROP_CONNECTION_TIMEOUT = "connection.timeout";
	
	public static final String PROP_CONNECTION_SO_TIMEOUT = "connection.soTimeout";
	
	public static final String PROP_CONNECTION_MAX_THREADS = "connection.maxThread";
	
	public static final String PROP_MAX_CONNECTIONS_PER_HOST = "connection.maxConnectionsPerHost";
	
	public static final String PROP_PLATFORM_ENCODING = "platform.encoding";
	
	public static final String PROP_LOGGER_CLASS = "client.logger";
	
	public static final String PROP_IS_LOG = "client.isLog";
	
	private static final int deafultTimeout = 10000;
	
	private static final int defaultSoTimeout = 20000;
	
	private static final int defaultMaxThreads = 100;
	
	private static final int defaultMaxConnectionsPerHost = 32;
	
	private static final String defaultEncoding = "UTF-8";
	
	private String loggerClass;
	
	private boolean log = true;
	
	private ILIInvocationLogger invokeLogger;
	
	protected XWECPLIClient(Properties props)
	{
		this(DEFAULT_INSTANCE_NAME, props);
	}
	
	protected XWECPLIClient(String name, Properties props)
	{
		String channel = props.getProperty(PROP_CHANNEL);
		String platformURL = props.getProperty(PROP_PLATFORM_URL);
		String timeout = props.getProperty(PROP_CONNECTION_TIMEOUT);
		String soTimeout = props.getProperty(PROP_CONNECTION_SO_TIMEOUT);
		String maxThreads = props.getProperty(PROP_CONNECTION_MAX_THREADS);
		String encoding = props.getProperty(PROP_PLATFORM_ENCODING);
		String maxConnectionsPerHost = props.getProperty(PROP_MAX_CONNECTIONS_PER_HOST);
		String loggerClass = props.getProperty(PROP_LOGGER_CLASS);
		String isLog = props.getProperty(PROP_IS_LOG);
		String user = props.getProperty(PROP_USER);
		String password = props.getProperty(PROP_PWD);
		
		this.loggerClass = loggerClass;
		this.log = "true".equalsIgnoreCase(isLog) ? true : false;
		
		if(channel == null || "".equals(channel.trim()))
		{
			throw new NullPointerException("必须指定客户端渠道编号!");
		}
		
		if(platformURL == null || "".equals(platformURL.trim()))
		{
			throw new NullPointerException("必须指定业务代理平台URL(http://xxx/xwecp.do)!");
		}
		
		timeout = timeout == null || "".equals(timeout.trim()) ? "" + deafultTimeout : timeout;
		soTimeout = soTimeout == null || "".equals(soTimeout.trim()) ? "" + defaultSoTimeout : soTimeout;
		maxThreads = maxThreads == null || "".equals(maxThreads.trim()) ? "" + defaultMaxThreads : maxThreads;
		encoding = encoding == null || "".equals(encoding.trim()) ? "" + defaultEncoding : encoding;
		maxConnectionsPerHost = maxConnectionsPerHost == null || "".equals(maxConnectionsPerHost.trim()) ? "" + defaultMaxConnectionsPerHost : maxConnectionsPerHost;
		this.msgHelper = new MessageHelper(channel);
		this.platformConnection = new HttpCommunicator();
		this.platformConnection.setRemoteURL(platformURL);
		this.platformConnection.setConnectionTimeout(Integer.parseInt(timeout));
		this.platformConnection.setSoTimeout(Integer.parseInt(soTimeout));
		this.platformConnection.setMaxConnectionsPerHost(Integer.parseInt(maxConnectionsPerHost));
		this.platformConnection.setMaxThreads(Integer.parseInt(maxThreads));
		this.platformConnection.setEncoding(encoding);
		this.platformConnection.initialize();
		LIInvocationContext.initialize(user, password);
		this.initInvokeLogger();
	}
	
	public static final XWECPLIClient createInstance(Properties props)
	{
		return createInstance(DEFAULT_INSTANCE_NAME, props);
	}
	
	public static final XWECPLIClient createInstance(String name, Properties props)
	{
		name = name == null || "".equals(name.trim()) ? DEFAULT_INSTANCE_NAME : name;
		StringBuffer sb = new StringBuffer();
		sb.append("==========逻辑接口配置支持的参数有==========\n");
		sb.append(PROP_CHANNEL+"[渠道编码,由XWECP平台统一分配]!\n");
		sb.append(PROP_USER+"[用户名]!\n");
		sb.append(PROP_PWD+"[用户密码(MD5加密)]!\n");
		sb.append(PROP_PLATFORM_URL+"[逻辑接口地址]!\n");
		sb.append(PROP_CONNECTION_TIMEOUT+"[响应超时时间,以毫秒为单位,默认1分钟]!\n");
		sb.append(PROP_CONNECTION_SO_TIMEOUT+"[SOCKET连接超时间,以毫秒为单位,默认1分钟]!\n");
		sb.append(PROP_CONNECTION_MAX_THREADS+"[HTTP通道最大线程数,默认100!]!\n");
		sb.append(PROP_MAX_CONNECTIONS_PER_HOST+"[HTTP通道每个主机最大连接数,默认32!]!\n");
		sb.append(PROP_PLATFORM_ENCODING+"[平台输入输出字符集,默认UTF-8!]!\n");
		sb.append(PROP_LOGGER_CLASS+"[业务系统逻辑接口调用日志实现类]!\n");
		sb.append(PROP_IS_LOG+"[是否记录业务系统逻辑接口调用日志,true/false,默认为false]!\n");
		sb.append("=======================================\n");
		XWECPLIClient client = new XWECPLIClient(name, props);
		instanceMap.put(name, client);
		return client;
	}
	
	public void log(String cmd, String url, String requestXML, String responseXML, ServiceMessage requestMsg, ServiceMessage responseMessage, long beginTime, long endTime, Throwable errorStack)
	{
		if(this.invokeLogger != null)
		{
			this.invokeLogger.log(cmd, url, requestXML, responseXML, requestMsg, responseMessage, beginTime, endTime, errorStack);
		}
	}
	
	protected void initInvokeLogger()
	{
		if(this.log && this.loggerClass != null && this.loggerClass.trim().length() > 0)
		{
			try
			{
				Class clazz = Class.forName(this.loggerClass);
				Object obj = clazz.newInstance();
				this.invokeLogger = (ILIInvocationLogger)(obj);
			}
			catch (Exception e)
			{
				logger.error("逻辑接口调用日志记录对象创建失败, 关闭逻辑接口调用日志["+e.getMessage()+"]!");
			}
		}
		else
		{
			logger.warn("逻辑接口调用日志被禁用或没有配置实现类!");
		}
	}
	
	public static final XWECPLIClient getInstance()
	{
		return getInstance(DEFAULT_INSTANCE_NAME);
	}
	
	public static final XWECPLIClient getInstance(String name)
	{
		name = name == null || "".equals(name.trim()) ? DEFAULT_INSTANCE_NAME : name;
		return instanceMap.get(name);
	}
	
	public MessageHelper getMsgHelper()
	{
		return msgHelper;
	}

	public HttpCommunicator getPlatformConnection()
	{
		return platformConnection;
	}

	public ILIInvocationLogger getInvokeLogger()
	{
		return invokeLogger;
	}
}
