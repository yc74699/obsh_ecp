package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IUserNumByVnumService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.UserNumByVnumServiceClientService;
import com.xwtech.xwecp.service.logic.pojo.QRY040053Result;

public class QRY040053Test {

	private static final Logger logger = Logger.getLogger(QRY040053Test.class);
	
	public static void main(String[] args) throws LIException {
		
//		初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13912986834");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18251968869");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		
		IUserNumByVnumService is = new UserNumByVnumServiceClientService();
		QRY040053Result re =is.getUserNumByVnum("15061260551");
		if("0".equals(re.getResultCode())){
			System.out.println("---vnumCity---"+re.getVnumCity());
			System.out.println("---userSid----"+re.getUserSid());
			System.out.println("---userCity---"+re.getUserCity());
		}else{
			System.out.println("---errorMessage--"+re.getErrorMessage());
		}
		
	}

}
