package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;


import com.xwtech.xwecp.service.logic.client_impl.common.IScbQueryService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ScbQueryServiceClientImpl;


import com.xwtech.xwecp.service.logic.pojo.QRY030014Result;
import com.xwtech.xwecp.service.logic.pojo.ScbHisDetail;


public class QRY030014Test
{
	private static final Logger logger = Logger.getLogger(QRY030014Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段  
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8080/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("19");
		lic.setUserMobile("13485064354");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13485064354");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "19");
		ic.addContextParameter("ddr_city", "19");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1908200001136600");
		
		
		lic.setContextParameter(ic);
		
		IScbQueryService co = new ScbQueryServiceClientImpl();
		QRY030014Result re = co.scbQuery("13485064354","2","201207","201207");
		

		logger.info(" ====== 开始返回参数 ======");
		logger.info(" ====== getResultCode ======" + re.getResultCode());
		logger.info(" ====== getResultCode ======" + re.getAmount());

		for(ScbHisDetail  sc : re.getScbHisDetailList() ){
			
		    System.out.println("=====================================");
		    System.out.println("getSubSid======" + sc.getSubSid());
			System.out.println("getChgResonRemark======" + sc.getChgResonRemark());
			System.out.println("getChgResonType======" + sc.getChgResonType());
		    System.out.println("getIsRollback======" + sc.getIsRollback());
			System.out.println("getOperator======" + sc.getOperator());
			System.out.println("getOperatorName======" + sc.getOperatorName());
			System.out.println("getOprSource======" + sc.getOprSource());
			System.out.println("getOprSrl======" + sc.getOprSrl());
			System.out.println("getOprDate======" + sc.getOprDate());
			System.out.println("getOprType======" + sc.getOprType());
		}
		
	}
}

