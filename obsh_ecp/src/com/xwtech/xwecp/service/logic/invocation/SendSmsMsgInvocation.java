package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.SmsDao;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY080001Result;

/**
 * 短消息发送
 * @author 吴宗德
 * 
 */
public class SendSmsMsgInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(SendSmsMsgInvocation.class);
	
	private SmsDao smsDao;
	
	public SendSmsMsgInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.smsDao = (SmsDao)(springCtx.getBean("smsDao"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		QRY080001Result res = new QRY080001Result();
		res.setResultCode(LOGIC_ERROR);
		res.setErrorMessage("");
		try
		{
			String mobile = (String)this.getParameters(params, "mobile");
			String content = (String)this.getParameters(params, "content");
			String spCode = (String)this.getParameters(params, "spCode");
			String serviceId = (String)this.getParameters(params, "serviceId");
			
			int ret = smsDao.sendSms(mobile, content, spCode, serviceId);
			
			if (ret > 0) {
				res.setResultCode(LOGIC_SUCESS);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			res.setErrorCode(LOGIC_EXCEPTION);
		}
		
		return res;
	}
	
}
