package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IChkUserCountryService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChkUserCountryServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050052Result;

public class QRY050052Test {
	private static final Logger logger = Logger.getLogger(QRY050052Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8089/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		
		IChkUserCountryService co = new ChkUserCountryServiceClientImpl();
		QRY050052Result re = co.chkUserCountry("18752345109", "12515692");
		System.out.println(re.getResultCode());
		System.out.println(re.getRetcode());
		System.out.println(re.getErrorMessage());
	}
}
