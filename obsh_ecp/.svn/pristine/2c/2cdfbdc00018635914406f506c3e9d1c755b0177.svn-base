package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.SMSRechargeServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.ISMSRechargeService;
import com.xwtech.xwecp.service.logic.pojo.DEL040049Result;




public class DEL040049Test
{
	private static final Logger logger = Logger.getLogger(DEL040049Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813366200");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813366200");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "");
		
		lic.setContextParameter(ic);
		
		
		ISMSRechargeService co = new SMSRechargeServiceClientImpl();
		DEL040049Result re = co.smsRecharge("13813366200", "","1","");
//		DEL040049Result re = co.smsRecharge("13813366200", "10000","2","银行");
//		DEL040049Result re = co.smsRecharge("13813366200", "10000","3","流水");
		
		
		
		logger.info(" ====== 开始返回参数 ======");
//		if (re != null)
//		{
//			logger.info(" ====== 错误信息 ======" + re.getCardId());
//			logger.info(" ====== 结果码 ======" + re.getEasyPayType());
//			logger.info(" ====== 错误信息 ======" + re.getErrorMessage());
//		
//
//		}
	}
}
