//package com.xwtech.xwecp.test;
//
//import java.util.Properties;
//
//import org.apache.log4j.Logger;
//
//import com.xwtech.xwecp.msg.InvocationContext;
//import com.xwtech.xwecp.service.logic.client_impl.common.IChgProFamailyMsisdnService;
//import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
//import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
//import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChgProFamailyMsisdnServiceClientImpl;
//import com.xwtech.xwecp.service.logic.pojo.DEL040017Result;
//
//public class DEL040017Test
//{
//	private static final Logger logger = Logger.getLogger(DEL040017Test.class);
//	
//	public static void main(String[] args) throws Exception
//	{
//		//初始化ecp客户端片段
//		Properties props = new Properties();
//		props.put("client.channel", "test_channel");
//		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
//		props.put("platform.user", "zlbbq");
//		props.put("platform.password", "zlbbq99");
//		
//		XWECPLIClient client = XWECPLIClient.createInstance(props);
//		//逻辑接口调用片段
//		LIInvocationContext lic = LIInvocationContext.getContext();
//		lic.setBizCode("biz_code_19234");
//		lic.setOpType("开通/关闭/查询/变更");
//		lic.setUserBrand("动感地带");
//		lic.setUserCity("用户县市");
//		lic.setUserMobile("13601400067");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13952030863");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "14");
//		ic.addContextParameter("ddr_city", "14");
//		ic.addContextParameter("selfplatuserreg_user_id", "userid");
//		
//		ic.addContextParameter("user_id", "");
//		
//		lic.setContextParameter(ic);
//		
//		IChgProFamailyMsisdnService co = new ChgProFamailyMsisdnServiceClientImpl();
//
//		
//		DEL040017Result re = co.chgProFamailyMsisdn("13952030863", "", 4, 3, "364777F91461D668", "13866901806", "111");
//	
//		logger.info(" ====== 开始返回参数 ======");
//		if (re != null)
//		{
//			logger.info(" ====== getResultCode ======" + re.getResultCode());
//			}
//		}
//	
//}
