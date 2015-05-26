package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IFlowToPayfeeService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.FlowToPayfeeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY610054Result;


/**
 * 流量和金额兑换查询
 * @author wang.h
 *
 */
public class QRY610054Test {
	private static final Logger logger = Logger.getLogger(QRY610054Test.class);
	
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
		 lic.setUserCity("12");
		 lic.setUserMobile("13585198722");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13585198722");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("request_seq", "0_111");
		ic.addContextParameter("request_time", "20110804022825");
		ic.addContextParameter("ddr_city", "12");
		
		ic.addContextParameter("user_id", "1208200000060545");
		
		lic.setContextParameter(ic);
	
		
		IFlowToPayfeeService co = new FlowToPayfeeServiceClientImpl();
		QRY610054Result re = co.flowToPayfee("levelone", "1024");
		
		if(re !=null){
			System.out.println("=============== re.getPayfee() ================" + re.getPayfee());
			System.out.println("=============== re.getRetCode() ===============" + re.getRetCode());
		}
	}
}
