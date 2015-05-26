package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryNewMobileStateService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryNewMobileStateServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY090017Result;

import com.xwtech.xwecp.service.logic.client.XWECPLIClient;

public class QRY090017Test
{
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "terminal_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14"); 
		lic.setUserMobile("13651542424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13651542424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "");
		lic.setContextParameter(ic);
		
		try{
			IQueryNewMobileStateService co = new QueryNewMobileStateServiceClientImpl();
			QRY090017Result re = co.queryNewMobileState("15150576706", "1111","0");
			System.out.println(re.getState());
		}catch(Exception e){	
			e.printStackTrace();
		}
	}
}
