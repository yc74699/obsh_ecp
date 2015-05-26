package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IVerifyAgentUserPwdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.VerifyAgentUserPwdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050046Result;

public class QRY050046Test {
	private static final Logger logger = Logger.getLogger(QRY050046Test.class);
	
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
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		//lic.setUserMobile("13912986834");
		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13952395931");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		
		IVerifyAgentUserPwdService co = new VerifyAgentUserPwdServiceClientImpl();
//		QRY050046Result re = co.verifyAgentUserPwd("13914627480", "7480");
		QRY050046Result re = co.verifyAgentUserPwd("13852342534", "123123");
		System.out.println(re.getResultCode());
	}
}
