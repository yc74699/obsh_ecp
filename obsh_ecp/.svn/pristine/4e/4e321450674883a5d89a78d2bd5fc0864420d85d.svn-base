package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IdoMobilePayOperService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.DoMobilePayOperServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040052Result;




public class DEL040052Test
{
	private static final Logger logger = Logger.getLogger(DEL040052Test.class);
	
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
		lic.setUserMobile("14751812083");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "");
		
		lic.setContextParameter(ic);
		
		
		IdoMobilePayOperService co = new DoMobilePayOperServiceClientImpl();
		DEL040052Result re = co.domobilePayOper("13813382424", "0", "", "", "0");
		
		
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 错误信息 ======" + re.getErrorMessage());
			logger.info(" ====== getSigncode ======" + re.getSigncode());
			logger.info(" ====== getPrepaytype ======" + re.getPrepaytype());
			logger.info(" ====== getTrigamt ======" + re.getTrigamt());
			logger.info(" ====== getDrawamt ======" + re.getDrawamt());
			logger.info(" ====== getPrepaytype ======" + re.getPrepaytype());

		}
	}
}
