package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICheckUserTransReg;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CheckUserTransRegClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050040Result;

public class QRY050040Test {
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13952395959");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13952395959");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13952395959");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		ICheckUserTransReg queryMktAct = new CheckUserTransRegClientImpl();
		
		QRY050040Result result = queryMktAct.checkUserTransReg("13952395959", "12", "13", "1208200000060545");
		
		if(result != null){
			System.out.println(" ====== getResultCode ======" + result.getChkresult());
			System.out.println(" ====== getErrorMessage ======" + result.getErrorMessage());
		}
	}
}