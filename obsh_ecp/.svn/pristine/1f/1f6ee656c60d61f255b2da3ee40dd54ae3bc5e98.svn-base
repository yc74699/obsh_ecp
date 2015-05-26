package com.xwtech.xwecp.util;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xwtech.xwecp.memcached.IMemcachedManager;

public class ClearItBossMemScheduler extends QuartzJobBean {

	private static final String APPLICATION_CONTEXT_KEY = "applicationContext";   
	private ApplicationContext getApplicationContext(JobExecutionContext context) throws Exception {   
	        ApplicationContext appCtx = null;   
	        appCtx = (ApplicationContext) context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);   
	        if (appCtx == null) {   
	            throw new JobExecutionException("No application context available in scheduler context for key \"" + APPLICATION_CONTEXT_KEY + "\"");   
	        }   
	        return appCtx;   
	}  
	private IMemcachedManager cache;
	
	@Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		try {
			cache = (IMemcachedManager)this.getApplicationContext(ctx).getBean("cache");
			List lsKeys = (List)cache.get("ITYXA_KEY_INFO");
			if (null != lsKeys && lsKeys.size() > 0) {
				for (int i = 0;i < lsKeys.size(); i++) {
					cache.delete(String.valueOf(lsKeys.get(i)));
				}
				cache.delete("ITYXA_KEY_INFO");
			}
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
