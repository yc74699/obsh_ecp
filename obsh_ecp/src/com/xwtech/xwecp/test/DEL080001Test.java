package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangeUserShortNumService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChangeUserShortNumServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL080001Result;

public class DEL080001Test
{
	private static final Logger logger = Logger.getLogger(DEL080001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("全球通");
		lic.setUserCity("12");
		lic.setUserMobile("15952301080");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15952301080");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("context_loginiplock_login_ip", "127.0.0.1");
		
		ic.addContextParameter("user_id", "1214200004298532");  //2056200011182291
		
		
		lic.setContextParameter(ic);

		
		IChangeUserShortNumService co = new ChangeUserShortNumServiceClientImpl();
		DEL080001Result re = co.changeUserShortNum("1299300002467001", "15052670834", "765");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
