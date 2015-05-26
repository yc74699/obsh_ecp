package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IABCPayService;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangprodNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ABCPayServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChangprodNewServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040035Result;

public class DEL040035Test
{
	private static final Logger logger = Logger.getLogger(DEL040035Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
//		props.put("platform.url", "http://127.0.0.1/obsh_ecp_test/xwecp.do");
		props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("15061411447");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15061411447");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15061411447");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1210300002578253");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		
		String prodId = "1000100217";
        String addProductSet = ",2100000003,;,2100000002,;2010121031,,;";
        String delProductSet = ",2000002942,;";
		IChangprodNewService ins = new ChangprodNewServiceClientImpl();
		DEL040035Result res = ins.changeProdNew("12", "ChangeProduct", prodId, addProductSet, delProductSet, "0", "15061411447", "");
		
		System.out.println("getErrorCode  ========"+res.getErrorCode());
		System.out.println("getBossCode  ========"+res.getBossCode());
		System.out.println("getErrorMessage  ========"+res.getErrorMessage());
		System.out.println("getResultCode  ========"+res.getResultCode());
	}
}
