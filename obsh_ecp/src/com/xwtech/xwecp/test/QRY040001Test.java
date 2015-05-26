package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040001Result;

public class QRY040001Test {
	private static final Logger logger = Logger.getLogger(QRY040001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
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
		lic.setUserBrand("12");
		lic.setUserCity("12");
		lic.setUserMobile("13915170950");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13915170950");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13915170950");
		
		
		lic.setContextParameter(ic);
		
		IQueryUserInfoService co = new QueryUserInfoServiceClientImpl();
		QRY040001Result re = co.queryUserInfo("13915170950");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			
			logger.info(" ====== getAppDate ======" + re.getAppDate());
			logger.info(" ====== getBrand ======" + re.getBrand());
			logger.info(" ====== getCity ======" + re.getCity());
			logger.info(" ====== getCountry ======" + re.getCountry());
			logger.info(" ====== getGrade ======" + re.getGrade());
			logger.info(" ====== getIcNo ======" + re.getIcNo());
			logger.info(" ====== getIcType ======" + re.getIcType());
			logger.info(" ====== getPayMode ======" + re.getPayMode());
			logger.info(" ====== getPostCode ======" + re.getPostCode());
			logger.info(" ====== getRelAddr ======" + re.getRelAddr());
			logger.info(" ====== getRelEmail ======" + re.getRelEmail());
			logger.info(" ====== getRelName ======" + re.getRelName());
			logger.info(" ====== getRelPhone ======" + re.getRelPhone());
			logger.info(" ====== getState ======" + re.getState());
			logger.info(" ====== getUserId ======" + re.getUserId());
			logger.info(" ====== getUserName ======" + re.getUserName());
			
		}
	}
}
