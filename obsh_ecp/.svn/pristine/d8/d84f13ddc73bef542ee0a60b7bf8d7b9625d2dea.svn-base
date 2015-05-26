package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.IZxrwDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040051Result;

/**
 * 锁号操作
 * @author 丁亮
 * 开发日期:Nov 12, 2012 7:26:37 PM
 */
public class DoLockedMobileNumInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(DoLockedMobileNumInvocation.class);
	private IZxrwDAO zxrwDAO;
	public DoLockedMobileNumInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.zxrwDAO = (IZxrwDAO) (springCtx.getBean("zxrwDAO"));
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL040051Result res = new DEL040051Result();
		try
		{
			String lockedType = (String) getParameters(params,"lockType");
			List<String> orderIds = (List<String>)getParameters(params, "orderId");
			Map<String, String> m = zxrwDAO.lockedMobile(lockedType, orderIds); //此处操作数据库查询状态
			res.setResultCode(LOGIC_SUCESS);
			res.setState(m);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
}