package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAgentSerialNumberService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAgentSerialNumberServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.AgentSerialNumber;
import com.xwtech.xwecp.service.logic.pojo.QRY040017Result;

public class QRY040017Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		props.put("platform.url", "http://localhost/xwecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13813844797");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13776535783");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		//ic.addContextParameter("ddr_city", "14");
		
		//ic.addContextParameter("user_id", "1419200015393873");
		
		//代理商agentUserId(需要设置此附加参数) 
		ic.addContextParameter("agentUserId", "1426200004607636");
		
		lic.setContextParameter(ic);
		
		//1:转帐流水查询
		//2:充值流水查询
		//3:预存流水查询

		
		IQueryAgentSerialNumberService service = new QueryAgentSerialNumberServiceClientImpl();
		QRY040017Result res = service.queryAgentSerialNumber("13776535783", "0", "20100301", "20100331");
		
		
		List<AgentSerialNumber> agentSerialNumberList  =  res.getAgentSerialNumber();
		
		
		if(agentSerialNumberList != null && agentSerialNumberList.size() > 0)
		{
			for(AgentSerialNumber bean : agentSerialNumberList) {
				System.out.println(bean.getMsisdn());
				System.out.println(bean.getDetailBillMonth());
				System.out.println(bean.getAdMsisdn());
				System.out.println(bean.getWfseqType());
				System.out.println(bean.getCardAreaCode());
				System.out.println(bean.getStartDate());
			}
		}
		
//		System.out.println(res.getCzAmount());
//		System.out.println(res.getYcAmount());
//		System.out.println(res.getZzAmount());
		System.out.println("------------END----------------------");
		
	}
	
	
	
}
