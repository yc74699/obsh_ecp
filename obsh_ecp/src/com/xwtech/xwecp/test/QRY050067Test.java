package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFreeTimesService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFreeTimesServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050067Result;

public class QRY050067Test {
	private static final Logger logger = Logger.getLogger(QRY050067Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        //初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11"); 
		lic.setUserMobile("13773229666");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13773229666");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		
		ic.addContextParameter("user_id", "1104200014390441");  //2056200011182291
		
		lic.setContextParameter(ic);
		try{
		   IQueryFreeTimesService qf = new QueryFreeTimesServiceClientImpl();
		   QRY050067Result re=qf.getLeftFreeTimes("13773229666", "11");
		   if(null != re)
		   {
			   logger.info("=========re.getLeftFressTimes()========"+re.getLeftFressTimes());
		   }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}