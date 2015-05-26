package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IQuitOutXQWService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QuitOutXQWServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010011Result;
    
public class DEL010011Test
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
		IQuitOutXQWService service = new QuitOutXQWServiceClientImpl();
		DEL010011Result result = service.quitOutXQWOperation("1211200007792998", "m1", "13770472424");
		
		System.err.println(result.getCustomerName());
		System.err.println(result.getDelDate());
		System.err.println(result.getSubsName());
		System.err.println(result.getStatus());
		System.err.println(result.getGrpsubsId());
		System.err.println(result.getResultCode());
		System.err.println(result.getErrorMessage());
	}
}
