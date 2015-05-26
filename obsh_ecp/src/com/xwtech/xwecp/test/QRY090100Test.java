package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QuerySnQqhmzhClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY090100Result;

/**
 * 判断是否是国内亲情号码测试类
 * @author yangli
 *
 */
public class QRY090100Test {
private static final Logger logger = Logger.getLogger(QRY090100Test.class);
	
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
		 lic.setUserCity("12");
		 lic.setUserMobile("13585198722");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13585198722");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "23");
		ic.addContextParameter("request_seq", "0_111");
		ic.addContextParameter("request_time", "20110804022825");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1208200000060545");
		
		lic.setContextParameter(ic);
		
		QuerySnQqhmzhClientImpl co = new QuerySnQqhmzhClientImpl();
		QRY090100Result re = co.querySnqqhmzh("13585198722",  "2000001840");
		
		System.out.println("22222222"+re);
	}
	
}
