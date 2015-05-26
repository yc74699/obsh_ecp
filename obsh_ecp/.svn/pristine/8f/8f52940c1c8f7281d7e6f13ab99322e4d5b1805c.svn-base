package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBillDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryOperDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.IScoreExchangeService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBillDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryOperDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ScoreExchangeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL020001Result;
import com.xwtech.xwecp.service.logic.pojo.GsmBillDetail;
import com.xwtech.xwecp.service.logic.pojo.OperDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040004Result;

public class DEL020001Test {
private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.81:10000/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("13606132424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "1");
		ic.addContextParameter("route_type", "11");
		ic.addContextParameter("route_value", "13606132424");
		ic.addContextParameter("ddr_city", "11");
		
		ic.addContextParameter("user_id", "1101200007185585");
		
		lic.setContextParameter(ic);
		
		IScoreExchangeService service = new ScoreExchangeServiceClientImpl();
		//DEL020001Result res = service.scoreExchange("13921909348", "2", "2405", 1, "1", "5000","");
		DEL020001Result res = service.scoreExchange("13606132424", "1", "2400", 1, "1", "900","");
		if (res != null)
		{
			logger.info(" ====== getResultCode ======" + res.getResultCode());
			logger.info(" ====== getErrorMessage ======" + res.getErrorMessage());
			logger.info(" ====== liushui ======" + res.getOperating_srl());
		}
	}
	
	
	
}
