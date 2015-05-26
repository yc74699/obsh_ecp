package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryVnetProductsService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryVnetProductsServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040030Result;
import com.xwtech.xwecp.service.logic.pojo.VnetGroupBean;

public class QRY040030Test {

	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		String url = "http://10.32.122.166:10009/js_ecp/xwecp.do";
		String host = "http://127.0.0.1:8080/obsh_ecp/xwecp.do";
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", host);
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
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
		IQueryVnetProductsService service = new QueryVnetProductsServiceClientImpl();
		QRY040030Result result = service.queryVnetProducts("13815890413");
		
		List<VnetGroupBean> list = result.getVnetGroupBean();
		System.err.println(list.size());
		for(VnetGroupBean p : list)
		{
			System.err.println("============================");
			System.err.println(p.getCustomerName());
			System.err.println(p.getActiveFlag());
			System.err.println(p.getGrpsubsId());
			System.err.println(p.getBeginDate());
			System.err.println(p.getEndDate());
			System.err.println(p.getDepartType());
			System.err.println("============================");
		}
		System.err.println(result.getResultCode());
		System.err.println(result.getErrorMessage());
	}
}
