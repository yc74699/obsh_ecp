package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactBusinessService;
import com.xwtech.xwecp.service.logic.client_impl.common.IUserApplyDiscountActService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransactBusinessServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.UserApplyDiscountActServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.DEL040046Result;

public class DEL040046Test
{
	private static final Logger logger = Logger.getLogger(DEL040046Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.230:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13901584903");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13901584903");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200012242069");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		
		IUserApplyDiscountActService ia = new UserApplyDiscountActServiceClientImpl();
		DEL040046Result re = ia.userApplyDiscountAct("13901584903", "113410", "20", "2", "2", "1");
		if(null !=re){
			System.out.println(re.getResultCode()+"------------------resultCode");
			System.out.println(re.getErrorMessage()+"--------------------errorMessage");
			
			
		} 
		
	}
}
