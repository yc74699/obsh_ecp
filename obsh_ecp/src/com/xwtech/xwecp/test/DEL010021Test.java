package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IpreCheckBusiService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.PreCheckBusiServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010021Result;

public class DEL010021Test {
	private static final Logger logger = Logger.getLogger(DEL010021Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.167:10000/js_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8081/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("12");
		lic.setUserCity("12");
		lic.setUserMobile("15252318843");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15252318843");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15996190955");
		
		
		lic.setContextParameter(ic);
		
		IpreCheckBusiService co = new PreCheckBusiServiceClientImpl();
		DEL010021Result re = co.preCheckBusi("15996190955","CL","DEL","1");
		logger.info(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			
			logger.info(" ====== getPhoneNum ======" + re.getPhoneNum());
			logger.info(" ====== getPkgProdId ======" + re.getPkgProdId());
			logger.info(" ====== getProdId ======" + re.getProdId());
			logger.info(" ====== getOprType ======" + re.getOprType());
		}
	}
}
