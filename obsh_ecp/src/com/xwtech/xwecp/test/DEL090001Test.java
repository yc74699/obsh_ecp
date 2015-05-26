package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactYxfaNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransactYxfaNewServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL090001Result;

public class DEL090001Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		 lic.setBizCode("biz_code_19234");
		 lic.setOpType("开通/关闭/查询/变更");
		 lic.setUserBrand("动感地带");
		 lic.setUserCity("用户县市");
		 lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("request_seq", "1_1");
		ic.addContextParameter("request_time", "20090728024911");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		ITransactYxfaNewService co = new TransactYxfaNewServiceClientImpl();
		try {
			DEL090001Result result = co.transactYxfaNew("功能编码", "12", "1210300002055004", 123, "1234567", "业务包id", "礼品包id", "500", "方案id", "受理类型");
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
