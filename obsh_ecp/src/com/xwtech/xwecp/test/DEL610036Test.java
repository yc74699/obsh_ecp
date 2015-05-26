package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICfMyOrderCancelService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CfMyOrderCancelServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL610036Result;

public class DEL610036Test {
	private static final Logger logger = Logger.getLogger(DEL610036Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://10.32.65.67:8080/obsh_ecp/xwecp.do");
//    	props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10003/obsh_ecp/xwecp.do");

		//props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("14");
		lic.setUserCity("12");
		lic.setUserMobile("18762021100");
		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "18762021100");
		ic.addContextParameter("fixed_oper_id", "55555");
		
		
		lic.setContextParameter(ic);
		
		ICfMyOrderCancelService co = new CfMyOrderCancelServiceClientImpl();
		DEL610036Result re = co.cfmyordercancel("212201406042450277498","12");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			
		}
	}
}
