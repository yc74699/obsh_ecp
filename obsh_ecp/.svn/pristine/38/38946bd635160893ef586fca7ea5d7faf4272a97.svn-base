package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAgentBalanceService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAgentBalanceServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040015Result;

public class QRY040015Test {
private static final Logger logger = Logger.getLogger(QRY040014TestApp.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		props.put("platform.url", "http://localhost/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("");
		lic.setUserMobile("13913986186");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913986186");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913986186");
		ic.addContextParameter("ddr_city", "14");
		
		//代理商agentCustomId(需要设置此附加参数)  
		ic.addContextParameter("agentCustomId", "");
		lic.setContextParameter(ic);
		
		IQueryAgentBalanceService service = new QueryAgentBalanceServiceClientImpl();
		QRY040015Result result = service.queryAgentBalance("13913986186", "1419200008842422", "14");
	
	}
	
	
}
