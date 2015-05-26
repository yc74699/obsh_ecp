package com.xwtech.xwecp.web.listener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.xwtech.xwecp.AppConfig;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.Jedis.RedisClient;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.flow.works.job.ReleaseInvalidConnnectJob;
import com.xwtech.xwecp.pojo.NumberSegment;
import com.xwtech.xwecp.util.JobDispatchUtil;
import com.xwtech.xwecp.web.XWECPWebApp;

public class ApplicationContextListener implements ServletContextListener
{
	private static final Logger logger = Logger.getLogger(ApplicationContextListener.class);
	
	public void contextDestroyed(ServletContextEvent arg0)
	{
		logger.info("XWECP关闭!");
	}

	public void contextInitialized(ServletContextEvent arg0)
	{
		String realPath = arg0.getServletContext().getRealPath("");
		if(!realPath.endsWith("/"))
		{
			realPath += "/";
		}

		XWECPApp.APP_PATH = realPath;
		File log4jConfig = new File(realPath, "WEB-INF/classes/log4j_ecp.properties");
		/* redis的地址配置文件*/
		File redisConfig = new File(realPath,"WEB-INF/cfg/server.xml");
		XWECPApp.redisCli = new RedisClient();
		XWECPApp.redisCli.init(redisConfig);
		
		URL url = null;
		try{
			url = log4jConfig.toURI().toURL();
			PropertyConfigurator.configure(url);
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		
		XWECPWebApp.CONTEXT_NAME = arg0.getServletContext().getServletContextName();
		logger.info("XWECPWebApp.CONTEXT_NAME = " + XWECPWebApp.CONTEXT_NAME);
		XWECPWebApp.SPRING_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
		//根据配置确定是否启动日志持久化线程
		XWECPWebApp.CDR_DIRECT2DB = AppConfig.getConfigValue("logConfig", "cdrDirect2DB");
		XWECPWebApp.IS_RECORD_RIGHT = AppConfig.getConfigValue("logConfig", "isRecordRight");
		//获取号段表数据，接口用户登录、查询用户资料、查询归属地根据地市来请求报文
		XWECPWebApp.NUMBERSEGMENTS = getNumberSegment();
		logger.info("号段记录数："+XWECPWebApp.NUMBERSEGMENTS.size());
		
		try {
			//每1分钟执行一次
			JobDispatchUtil.addJob("释放超时的连接请求", new ReleaseInvalidConnnectJob(), "0 0/1 * * * ?");
			//每1分钟执行一次
			//JobDispatchUtil.addJob("清空渠道流量统计数据", new RemoveChannelFlowJob(), "0 0/1 * * * ?");
			//每2秒钟执行一次
			//JobDispatchUtil.addJob("汇总渠道流量统计数据", new SaveChannelFlowJob(), "0/1 * * * * ?");
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Map<String,List<NumberSegment>> getNumberSegment()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		WellFormedDAO wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));		
		return wellFormedDAO.getNumberSegment();
	}
}