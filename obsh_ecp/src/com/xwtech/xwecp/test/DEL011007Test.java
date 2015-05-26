package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICtsjyDealService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CtsjyDealServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL011007Result;
import com.xwtech.xwecp.service.logic.pojo.SmsTsjyInfo;



public class DEL011007Test {
private static final Logger logger = Logger.getLogger(DEL011007Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://127.0.0.1:8080/js_ecp_new/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("15861781288");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15861781288");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
	
		List<SmsTsjyInfo> smsTsjyInfos = new ArrayList<SmsTsjyInfo>();
		SmsTsjyInfo ctsj = new SmsTsjyInfo();
		ctsj.setCreateDate("20110101");
		ctsj.setMsisdn("15861781288");
		ctsj.setType("1");
		ctsj.setRemark("welcome");
		smsTsjyInfos.add(ctsj);
		
		ICtsjyDealService service = new CtsjyDealServiceClientImpl();
		DEL011007Result result = service.ctsjyDeal(smsTsjyInfos);

		logger.info(" ====== 开始返回参数 ======");
		if (result != null)
		{
			logger.info(" === re.getResultCode() === " + result.getResultCode());
			logger.info(" === re.getErrorCode() === " + result.getErrorCode());

			logger.info(" === re.getErrorMessage() === " + result.getErrorMessage());
		}
		
	}
	
	
	
}
