package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMyTotalInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMyTotalInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MyTotalInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040047Result;


public class QRY040047Test
{
	private static final Logger logger = Logger.getLogger(QRY040047Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段  
		Properties props = new Properties();
		props.put("client.channel", "icc_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "yt");
		props.put("platform.password", "yt");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setUserCity("14");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13851668807");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200006261854");
		
		
		lic.setContextParameter(ic);
		
		IQueryMyTotalInfoService co = new QueryMyTotalInfoServiceClientImpl();
		QRY040047Result re = co.queryMyTotalInfo("13851668807", "DGDD_DGDD2", "NJDQ");
		

		logger.info(" ====== 开始返回参数 ======");
		logger.info(" ====== getResultCode ======" + re.getResultCode());

		for(MyTotalInfo  sc : re.getMyTotalInfoList() ){
			
		    System.out.println("=====================================");
		    System.out.println("getRetCode======" + sc.getRetCode());
		    System.out.println("getType======" + sc.getType());
			System.out.println("getTypeName======" + sc.getTypeName());
			System.out.println("getValue======" + sc.getValue());
		}
		
	}
}

