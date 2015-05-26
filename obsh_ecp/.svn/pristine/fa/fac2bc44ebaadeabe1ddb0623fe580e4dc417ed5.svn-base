package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IAgentUserLoginService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.AgentUserLoginServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040018Result;

public class QRY040018Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://localhost/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("15951285799");
		InvocationContext ic = new InvocationContext();
		
		ic.addContextParameter("login_msisdn", "15951285799");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15951285799");
		ic.addContextParameter("ddr_city", "18");
		ic.addContextParameter("$context_loginiplock_login_ip", "127.0.0.1");
		
		
		
		lic.setContextParameter(ic);
		
	
		
		IAgentUserLoginService service = new AgentUserLoginServiceClientImpl();
		QRY040018Result res = service.agentUserLogin("15951285799","5089251");
		
		System.out.println(res.getAgentCustomerId() + "--" + res.getGroupId());
		System.out.println("------------END----------------------");
		
	}
	
	
	
}
