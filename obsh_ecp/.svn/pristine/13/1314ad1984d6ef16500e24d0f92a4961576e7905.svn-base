package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.INXSPayService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.NXSPayServiceClientImpl;

import com.xwtech.xwecp.service.logic.pojo.DEL040100Result;
import com.xwtech.xwecp.service.logic.pojo.TransferTribe;

public class DEL040100Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://10.32.229.70:10006/wap_forward/xwecp.do");
		props.put("platform.url", "http://localhost:8080/wap_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8182/obsh_forward/obsh_js");
//		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");
		
		props.put("platform.user", "xl");
		props.put("platform.password", "xl");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13813382424");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200008195116");
		
		lic.setContextParameter(ic);
		
		INXSPayService service = new NXSPayServiceClientImpl();
//		DEL040100Result result = service.nXSPayService("13813382424", "100", "14","","","");
		DEL040100Result result = service.nXSPayService("13813382424", "100", "14","","");
		if(null != result)
		{
			System.out.println(result.getPayUrl());
			System.out.println(result.getPlain());
			System.out.println(result.getSignature());
			System.out.println(result.getCltSrl());
		}
		
	}
	
}
