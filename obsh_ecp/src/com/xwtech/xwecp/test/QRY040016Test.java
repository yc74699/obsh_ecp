package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAgentInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAgentInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.AgentUserInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040016Result;

public class QRY040016Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
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
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13921225799");
		ic.addContextParameter("ddr_city", "17");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		//代理商密码  226662
		//AgentName--AgentUserId--CustomerId--GroupId
		//石磊--1738200010603804--1738002028628038--1738200004637140
		IQueryAgentInfoService service = new QueryAgentInfoServiceClientImpl();
		long start = System.currentTimeMillis();
		QRY040016Result res = service.queryAgentInfo("13921225799","508925");
		System.out.println("用了" + (System.currentTimeMillis() - start));
		List<AgentUserInfo> agentUserInfoList  =  res.getAgentUserInfo();
		if(agentUserInfoList != null && agentUserInfoList.size() > 0)
		{
			AgentUserInfo agentUserInfo = agentUserInfoList.get(0);
		    System.out.println(agentUserInfo.getAgentName() + "--" + agentUserInfo.getAgentUserId() + "--" + agentUserInfo.getAgentCustomerId() + "--" + agentUserInfo.getGroupId());
		}
		
		System.out.println(res.getLevel2Number());
		System.out.println(res.getLevel3Number());
		System.out.println("------------END----------------------");
		
	}
	
	
	
}
