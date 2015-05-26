package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryqStatusByCardIdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryqStatusByCardIdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.ActivityBean;
import com.xwtech.xwecp.service.logic.pojo.QRY050038Result;

public class QRY050038Test {
	private static final Logger logger = Logger.getLogger(QRY050038Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "terminal_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.80:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.153:10006/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.86:8080/openapi_ecp/xwecp.do");
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		//ic.addContextParameter("user_id", "2055200007046734");
		
		lic.setContextParameter(ic);
		IQueryqStatusByCardIdService  co= new QueryqStatusByCardIdServiceClientImpl();
		
		QRY050038Result result = co.queryStatusByCardId("1","321084198408021921");
		
		if(result != null){

				System.out.println(result.getIfvalid());
				System.out.println(result.getErrorMessage());
				System.out.println(result.getErrorCode());
		}
	}
}