package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryConductService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryConductServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY020018Result;

public class QRY020018Test {

	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("12");
		lic.setUserCity("14");
		lic.setUserMobile("18862771667");

		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18862771667");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "2054300023463848");
		
		lic.setContextParameter(ic);
		


		
		IQueryConductService co=new QueryConductServiceClientImpl();
		QRY020018Result re=co.qryConduct("18862771667");
		
	
		if (re != null)
		{
			System.out.println("getState:" + re.getState());
		} else {
			System.out.println("无数据返回");
		}
	}
}
