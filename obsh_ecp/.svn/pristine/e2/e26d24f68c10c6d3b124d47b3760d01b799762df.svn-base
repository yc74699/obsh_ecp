package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IModifyCZZXRWUserInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ModifyCZZXRWUserInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040028Result;
    
public class DEL040028Test
{
	
	public static void main(String[] args) throws Exception
	{
		
		String url = "http://10.32.122.166:10009/js_ecp/xwecp.do";
		String host = "http://127.0.0.1/js_ecp/xwecp.do";
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", url);
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		 
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
		IModifyCZZXRWUserInfoService service = new ModifyCZZXRWUserInfoServiceClientImpl();
		DEL040028Result re = service.modifyCZZXRWUserInfo("15251247154", "", "guoxiang", "guoxiang@sina.com", "210012", "12", "123456789012300987","nanjing", "", "", "", "0265890909", "", "141900731", "", "", "");
		System.err.println(re.getResultCode());
		System.err.println(re.getErrorMessage());
	}
}
