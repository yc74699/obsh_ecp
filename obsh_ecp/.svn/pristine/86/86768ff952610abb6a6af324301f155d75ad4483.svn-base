package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IAddToXQWService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryUserActByImeiService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPkgUsedInfoService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.AddToXQWServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryUserActByImeiServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPkgUsedInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010010Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040020Result;
import com.xwtech.xwecp.service.logic.pojo.QRY090101Result;
    
public class QRY090101Test
{
	
	public static void main(String[] args) throws Exception
	{
		
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
//		props.put("platform.url", "http://10.32.229.70:10006/wap_forward/xwecp.do");
		props.put("platform.url", "http://10.32.65.71:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8080/wap_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10006/obsh_ecp/xwecp.do");
		props.put("platform.user", "xl");
		props.put("platform.password", "xl");
//		props.put("platform.user", "jhr");
//		props.put("platform.password", "jhr"); 
		@SuppressWarnings("unused")
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("");
		lic.setUserBrand("");
		lic.setUserCity("14");
		lic.setUserMobile("13951590912");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13951590912");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("oper_id", "");
		
//		lic.setUserCity("14");
//		lic.setUserMobile("13813382424");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13813382424");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "14");
//		ic.addContextParameter("ddr_city", "14");
//		ic.addContextParameter("oper_id", "");
//		ic.addContextParameter("user_id", "1419200008195116");
		lic.setContextParameter(ic);
		
		IQryUserActByImeiService service = new QryUserActByImeiServiceClientImpl();
		//359850051484082
		QRY090101Result result = service.qryUserActByImei("359627054539892");
		
		if(null != result)
		{
			System.out.println("result..."+result);
		}
	}
}
