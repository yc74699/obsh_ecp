package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBelongGroupNetService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBelongGroupNetServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY060073Result;




public class QRY060073Test
{
	
	public static void main(String[] args) throws Exception
	{
		

		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		//props.put("platform.url", "http://10.32.122.166:10000/js_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		 
		@SuppressWarnings("unused")
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("");
		lic.setUserBrand("");
		lic.setUserCity("12");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		IQueryBelongGroupNetService service = new QueryBelongGroupNetServiceClientImpl();
		
		QRY060073Result res = service.queryBelongGroupNet("泰州市高港区寺巷镇石桥村", "21");
		if (res != null)
		{
			System.out.println("用户是否为乡情网："+ res.getFlag()); //flag:1-是乡情网； 0-是集团v网
		}
	}
}
