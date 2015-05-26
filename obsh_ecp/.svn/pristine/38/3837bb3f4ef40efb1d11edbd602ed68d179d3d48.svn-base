package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMmsDealNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMmsDealNewServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY090012Result;

public class QRY090012Test {
private static final Logger logger = Logger.getLogger(QRY090012Test.class);
	
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
		 lic.setUserMobile("13952395959");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13952395959");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("request_seq", "1_1");
		ic.addContextParameter("request_time", "20090728024911");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1208200000060545");
		
		lic.setContextParameter(ic);
		
		IQueryMmsDealNewService co = new QueryMmsDealNewServiceClientImpl();
		QRY090012Result re = co.queryMmsDealNew("13952395959", 123, "1");
	}
	
}
