package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;


import com.xwtech.xwecp.service.logic.client_impl.common.IScbQueryNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ScbQueryNewServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040063Result;
import com.xwtech.xwecp.service.logic.pojo.ScbHisDetailNew;


public class QRY040063Test
{
	private static final Logger logger = Logger.getLogger(QRY040063Test.class);
	
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
		lic.setUserMobile("15151539700");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "15151539700");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1101300018421920");
		
		
		lic.setContextParameter(ic);
		
		IScbQueryNewService co = new ScbQueryNewServiceClientImpl();
		QRY040063Result re = co.scbQueryNew("15151539700","201306","201312");
			//co.scbQueryNew("15151539700","1","201312","201405");
		

		logger.info(" ====== 开始返回参数 ======");
		logger.info(" ====== getResultCode ======" + re.getResultCode());

		for(ScbHisDetailNew  sc : re.getScbHisDetailList() ){
			
		    System.out.println("=====================================");
			System.out.println("getChgResonType======" + sc.getChgResonType());
			System.out.println("income======" + sc.getIncome());
			System.out.println("getOprDate======" + sc.getOprDate());
			System.out.println(sc.getScoreValue());
		}
		
	}
}

