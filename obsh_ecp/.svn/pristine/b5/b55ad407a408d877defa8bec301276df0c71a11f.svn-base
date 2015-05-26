package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryValidatePrepareNumService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryValidatePrepareNumServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY020014Result;

public class QRY020014Test {

	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		String url = "http://10.32.122.166:10009/js_ecp/xwecp.do";
		String host = "http://127.0.0.1/js_ecp/xwecp.do";
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", host);
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		@SuppressWarnings("unused")
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("");
		lic.setUserBrand("");
		lic.setUserCity("14");
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		IQueryValidatePrepareNumService service = new QueryValidatePrepareNumServiceClientImpl();
		QRY020014Result result = service.queryValidatePrepareNum("141900111", "15062256927");
		
		System.err.println(result.getResultCode());
		System.err.println(result.getErrorMessage());
	}

}
