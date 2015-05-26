package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICancelSmsUserspNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CancelSmsUserspNewServiceClient;
import com.xwtech.xwecp.service.logic.pojo.DEL040068Result;

/**
 * 新统一退订
 * @author YXQ
 *
 */
public class DEL040068Test
{
	private static final Logger logger = Logger.getLogger(DEL040068Test.class);
	
	public static void main(String[] args) throws Exception
	{
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do"); 
		props.put("platform.url", "http://10.32.65.238:8080/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		
		
		String mobile="13913889201";
		lic.setUserMobile(mobile);
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", mobile);	
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value",mobile);
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "");   		
		lic.setContextParameter(ic);

		ICancelSmsUserspNewService co = new CancelSmsUserspNewServiceClient();
		DEL040068Result re = co.cancelSmsUserspNew(mobile,"1"); 
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode()   === " + re.getResultCode());
			logger.info(" === re.getErrorCode()    === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage()); 
		}
		
	}
}
