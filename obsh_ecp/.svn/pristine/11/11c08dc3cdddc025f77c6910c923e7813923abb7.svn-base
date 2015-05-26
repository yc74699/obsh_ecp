package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICheckUserPackageMessagesService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CheckUserPackageMessagesServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.JTVWPackageInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY210010Result;

public class QRY2100010Test {
	private static final Logger logger = Logger.getLogger(QRY2100010Test.class);
	public static void main(String[] args)throws Exception {
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		ICheckUserPackageMessagesService co=new CheckUserPackageMessagesServiceClientImpl();
		long starttime = System.currentTimeMillis();
		System.out.println("startTime: "+starttime);
		QRY210010Result re=co.checkUserPackageMessages("14", "25010007131#25030170015#25010001519","13675144094");
		long endTime = System.currentTimeMillis();
		System.out.println("endTime:"+ endTime);
		System.out.println("totalTime:"+ (endTime-starttime));
		if (re != null)
		{
			System.out.println(re.getResultCode());

			for(JTVWPackageInfo info : re.getJtvwPackageInfos())
			{
				System.out.println(info.getCustomer_code());
				System.out.println(info.getPackageCode());
				System.out.println(info.getPackageDesc());
				System.out.println(info.getProdid());
				System.out.println(info.getProdname());
				System.out.println(info.getTelNumber());
				System.out.println(info.getPrice());
				System.out.println("---------------------------");
			}
		}
	}
	
}
