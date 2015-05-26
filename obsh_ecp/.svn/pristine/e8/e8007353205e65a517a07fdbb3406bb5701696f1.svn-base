package com.xwtech.xwecp.test;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IModifyAgentPwdService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAgentBalanceService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBillDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGamePointService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryOperDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserEPackageService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ModifyAgentPwdServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAgentBalanceServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBillDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGamePointServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryOperDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserEPackageServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040016Result;
import com.xwtech.xwecp.service.logic.pojo.EPkgDetail;
import com.xwtech.xwecp.service.logic.pojo.GamePoint;
import com.xwtech.xwecp.service.logic.pojo.GsmBillDetail;
import com.xwtech.xwecp.service.logic.pojo.OperDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040004Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040014Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040015Result;
import com.xwtech.xwecp.service.logic.pojo.QRY050003Result;

public class DEL040016Test {
private static final Logger logger = Logger.getLogger(QRY040014TestApp.class);
	
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
		lic.setUserCity("用户县市");
		lic.setUserMobile("13813844797");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813844797");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		//代理商agentUserId(需要设置此附加参数) 
		ic.addContextParameter("agent_user_id", "1419200015393873");
		
		lic.setContextParameter(ic);
		
		IModifyAgentPwdService service = new ModifyAgentPwdServiceClientImpl();
		DEL040016Result result = service.modifyAgentPwd("13813844797", "844797");
		
	
	}
	
	
}
