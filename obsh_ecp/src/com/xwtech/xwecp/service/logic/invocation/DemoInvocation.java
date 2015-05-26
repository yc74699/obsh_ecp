package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

public class DemoInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(DemoInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	public DemoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		BaseServiceInvocationResult res = null;
		try
		{
			String id = "";
			/*if (null != params && params.size() > 0)
			{
				for (int i = 0; i < params.size(); i++)
				{
					RequestParameter par = (RequestParameter)params.get(i);
				}
			}*/
			
			/*DefaultServiceImpl impl = new DefaultServiceImpl();
			impl.setServiceConfig(config);
			impl.setParams(params);*/
			//TeletextMapping tm = (TeletextMapping)impl.getBossTeletextMapping();
			//logger.info(" ====== TeletextMapping ====== " + tm.getParamName());
			String rsp = (String)this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("QDCX_YYHD", params), accessId, "QDCX_YYHD", this.generateCity(params)));
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		
		return res;
	}

	public BossTeletextUtil getBossTeletextUtil() {
		return bossTeletextUtil;
	}

	public void setBossTeletextUtil(BossTeletextUtil bossTeletextUtil) {
		this.bossTeletextUtil = bossTeletextUtil;
	}

	public IRemote getRemote() {
		return remote;
	}

	public void setRemote(IRemote remote) {
		this.remote = remote;
	}
}
