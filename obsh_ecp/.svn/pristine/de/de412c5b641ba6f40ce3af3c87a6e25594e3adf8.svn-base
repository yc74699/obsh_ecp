package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransMarketPlanBroadBandService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransMarketPlanBroadBandServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL100008Result;

public class DEL100008Test
{
	private static final Logger logger = Logger.getLogger(DEL100008Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "market_channel");
		//props.put("platform.url", "http://10.32.122.166:10000/js_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:80/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "ytf");
		props.put("platform.password", "ytf");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("18");
		lic.setUserMobile("15952899092");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15952899092");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "18");
		ic.addContextParameter("ddr_city", "18");
		

		lic.setContextParameter(ic);

		ITransMarketPlanBroadBandService service = new TransMarketPlanBroadBandServiceClientImpl();
		
		DEL100008Result re = service.transMarketPlan("15952899092", "2518103907", "300001906030", "88015032759210", "300002112030", "1", "1877704","1877704","");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" === re.getResultCode() === " + re.getResultCode());
			System.out.println(" === re.getErrorCode() === " + re.getErrorCode());
			System.out.println(" === re.getErrorMessage() === " + re.getErrorMessage());
			System.out.println(" === CRM办理流水号 === " + re.getRecoid());
		}else{
			System.out.println(" ====== re 为null");
		}
	}
}
