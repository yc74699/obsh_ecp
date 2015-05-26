package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IUnionPayMallService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.UnionPayMallServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040111Result;

public class DEL040111Test {
	private static final Logger logger = Logger.getLogger(DEL040111Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("17");
		lic.setUserMobile("13921078544");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13921078544");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "17");
		ic.addContextParameter("ddr_city", "17");
		

		lic.setContextParameter(ic);

		IUnionPayMallService service = new UnionPayMallServiceClientImpl();
		
		
		DEL040111Result re = service.unionPayMall("13921078544",2, "2000");
//		logger.info(" ====== 开始返回参数 ======");
//		if (re != null)
//		{
//			System.out.println(" === re.getResultCode() === " + re.getResultCode());
//			System.out.println(" === re.getErrorCode() === " + re.getErrorCode());
//			System.out.println(" === re.getErrorMessage() === " + re.getErrorMessage());
//			
//			
//		}
	}
}
