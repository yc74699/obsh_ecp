package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.ILoginGetInformationService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.LoginGetInformationServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010028Result;

public class QRY010028Test {
	private static final Logger logger = Logger.getLogger(QRY010028Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13815886655");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13815886655");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		
		lic.setContextParameter(ic);
		
		ILoginGetInformationService co = new LoginGetInformationServiceClientImpl();
		QRY010028Result re = co.loginGetInformation("13815886655", (long)1, "10000001", "201307", "201308", (Integer)2, (Integer)3);
		System.out.println(re.getResultCode());
	}
}
