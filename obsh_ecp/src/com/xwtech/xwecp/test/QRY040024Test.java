package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPackService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPackServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040024Result;

public class QRY040024Test
{
	private static final Logger logger = Logger.getLogger(DEL040023Test.class);
	
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
		lic.setUserCity("14");
		lic.setUserMobile("13912986834");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13912986834");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		lic.setContextParameter(ic);
		
		IQueryPackService service = new QueryPackServiceClientImpl();
		List<String> codes = new ArrayList<String>();
		codes.add("2266");
		QRY040024Result result = service.queryPack(codes);
		System.out.println();
	}
	
}
