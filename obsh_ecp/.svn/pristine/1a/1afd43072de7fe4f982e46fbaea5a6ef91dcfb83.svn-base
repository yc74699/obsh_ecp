package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICfMyOrderQueryService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CfMyOrderQueryServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010036Result;

public class QRY010036Test {
	private static final Logger logger = Logger.getLogger(QRY010036Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://10.32.65.67:8080/obsh_ecp/xwecp.do");
//    	props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10000/obsh_ecp/xwecp.do");

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
		lic.setUserCity("14");
		lic.setUserMobile("15861721274");
		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15861721274");
		ic.addContextParameter("fixed_oper_id", "55555");
		
		
		lic.setContextParameter(ic);
		
		ICfMyOrderQueryService co = new CfMyOrderQueryServiceClientImpl();
		QRY010036Result re = co.cfmyorderquery("12140527816526993","15861721274");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			logger.info(" ====== getErrorMessage ======" + re.getOrderstatus());
			logger.info(" ====== getErrorMessage ======" + re.getOrderstatusname());
			logger.info(" ====== getErrorMessage ======" + re.getOrderid());
			logger.info(" ====== getErrorMessage ======" + re.getListOrderlineinfo().size());
			
		}
	}
}
