package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAllFamilyMembersService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAllFamilyMembersServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.FamilyMemberBean;
import com.xwtech.xwecp.service.logic.pojo.QRY010017Result;

public class QRY010017Test {
private static final Logger logger = Logger.getLogger(QRY010017Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
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
		lic.setUserCity("14");
		lic.setUserMobile("13851664447");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13851664447");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13851664447");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("context_loginiplock_login_ip", "127.0.0.1");
		
		
		
		ic.addContextParameter("user_id", "1419200000755198");
		
		lic.setContextParameter(ic);
		
		
		IQueryAllFamilyMembersService  co = new QueryAllFamilyMembersServiceClientImpl();
		
		QRY010017Result result = co.queryAllFamilyMembers("13851664447","201308");
		
		List<FamilyMemberBean> operDetail = result.getFamilyMember();
		
		for(int i = 0 ; i < operDetail.size() ; i ++)
		{
			System.out.println(operDetail.get(i).getMemberName() );
		}
		
	}
	
}
