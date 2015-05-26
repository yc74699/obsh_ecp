package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransMarketPlanService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransMarketPlanServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL100005Result;

public class DEL100005Test
{
	private static final Logger logger = Logger.getLogger(DEL100005Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("client.channel", "jsmcc_channel");
//		props.put("platform.url", "http://10.32.122.166:10000/js_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.71:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.229.103:10004/obsh_ecp/xwecp.do");
//		props.put("platform.user", "jhr");
//		props.put("platform.password", "jhr");
		props.put("platform.user", "mfw");
		props.put("platform.password", "mfw");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
//		lic.setUserCity("14");
//		lic.setUserMobile("13813382424");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13813382424");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "14");
//		ic.addContextParameter("ddr_city", "14");
		
		

		lic.setUserCity("17");
		lic.setUserMobile("13914342784");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13914342784");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "17");
		ic.addContextParameter("ddr_city", "17");

		lic.setContextParameter(ic);

		ITransMarketPlanService service = new TransMarketPlanServiceClientImpl();
		
		//办理
		//DEL100005Result re = service.transMarketPlan("2", "15952379514", "3000382001", "300000316000", "", "", "3", "11061000125262");
		//校验
		//DEL100005Result re = service.transMarketPlan("1", "13952339472", "3000382001", "300000316000", "88009898575662", "", "", "");
		
		
		//DEL100005Result re = service.transMarketPlan("1", "13913933685", "3001100034", "300001552077", "", "", "2", "");
		//DEL100005Result re = service.transMarketPlan("1", "13813382424", "3001100034", "300001354066", "11", "88011485097490", "2", "11","1","99402488","");
		DEL100005Result re = service.transMarketPlan("1", "13914342784", "3000292473", "300003180379", "", "", "", "","4","","88017823465350|88017823077982");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" === re.getResultCode() === " + re.getResultCode());
			System.out.println(" === re.getErrorCode() === " + re.getErrorCode());
			System.out.println(" === re.getErrorMessage() === " + re.getErrorMessage());
			System.out.println(" === CRM办理流水号 === " + re.getRecoid());
			System.out.println("end_date ==== "+re.getNewPrivEndDate());
			System.out.println("begin_date ==== "+re.getNewPrivStartDate());
			System.out.println("privId ===== " + re.getNewPrivId());
		}
		else
		{
			System.out.println(" ====== re 为null");
		}
	}
}
