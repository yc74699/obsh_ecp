package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetUserProInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetUserProInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.ProResource;
import com.xwtech.xwecp.service.logic.pojo.QRY050020Result;

public class QRY050020Test
{
	private static final Logger logger = Logger.getLogger(QRY050020Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8081/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.80:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.152:10004/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.86:8080/openapi_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13646272637");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13646272637");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "2050300009720405");
//		ic.addContextParameter("user_id", "2371200001226345");
		
		lic.setContextParameter(ic);
		
		IGetUserProInfoService co = new GetUserProInfoServiceClientImpl();
		QRY050020Result re = co.getUserProInfo("13646272637");//.queryUserPackageList("13913814503","14");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getResultCode ======" + re.getBossCode());
			logger.info(" ====== getResultCode ======" + re.getErrorCode());
			logger.info(" ====== getResultCode ======" + re.getErrorMessage());
			if (null != re.getProResources()&& re.getProResources().size() > 0)
			{
				logger.info(" ====== re.getUserPackageList.size() ====== " + re.getProResources().size());
				for (ProResource dt : re.getProResources())
				{
					logger.info(" ====== getProId ======" + dt.getProId());
					logger.info(" ====== getProName ======" + dt.getProName());
					logger.info(" ====== getBrandName ======" + dt.getBrandName());
					logger.info(" ====== getPayMode ======" + dt.getPayMode());
					logger.info(" ====== getProDesc ======" + dt.getProDesc());
					
					logger.info(" ===================================");
				
					
				}
			}
		}
	}
}
