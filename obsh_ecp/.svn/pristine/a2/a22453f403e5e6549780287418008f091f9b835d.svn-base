package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICheckVnetXQWMemberService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CheckVnetXQWMemberServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040029Result;
import com.xwtech.xwecp.service.logic.pojo.VnetGroupProduct;

public class QRY040029Test {

	
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
		props.put("platform.url", url);
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		@SuppressWarnings("unused")
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("");
		lic.setUserBrand("");
		lic.setUserCity("12");
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		ICheckVnetXQWMemberService service = new CheckVnetXQWMemberServiceClientImpl();
		// 15061411447 13770472424 13615142424
		QRY040029Result result = service.checkVnetXQWMemberInfo("13770472424");
		
		List<VnetGroupProduct> list = result.getVnetGroupProduct();
		System.err.println(result.getGrpqry());
		for(VnetGroupProduct p : list)
		{
			System.err.println("============================");
			System.err.println(p.getCustomerName());
			System.err.println(p.getGrpsubsId());
			System.err.println(p.getStartDate());
			System.err.println(p.getEndDate());
			System.err.println(p.getStatus());
			System.err.println("============================");
		}
		System.err.println(result.getResultCode());
		System.err.println(result.getErrorMessage());
	}

}
