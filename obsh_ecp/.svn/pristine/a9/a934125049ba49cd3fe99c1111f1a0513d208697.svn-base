package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetProResourceService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetProResourceServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY050016Result;

public class QRY050016Test
{
	private static final Logger logger = Logger.getLogger(QRY050016Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		 props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8081/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
	//props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://127.0.0.1/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.86:8080/openapi_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("22"); 
		lic.setUserMobile("15252318843");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15252318843");
		ic.addContextParameter("route_type", "1");  //13861422424//1527200002161863
		ic.addContextParameter("route_value", "22");//13611542424/1101300022887974
		ic.addContextParameter("ddr_city", "22");///13913032424/1419200008195160
		
		ic.addContextParameter("user_id", "1214200010698885");  //2056200011182291
		
		lic.setContextParameter(ic);
		try{
			//13914640447/2264300010549822
			IGetProResourceService co = new GetProResourceServiceClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13801542424 user_id：1101200007185584
			QRY050016Result re = co.getProResource("YCDQ");
		
		logger.info(" ====== 开始返回参数 ======"+re.getProResources().size());
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
