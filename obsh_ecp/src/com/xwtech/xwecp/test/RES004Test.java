package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.reserve.IOrderOperateService;
import com.xwtech.xwecp.service.logic.client_impl.reserve.impl.OrderOperateServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.OrderUpdateInfo;
import com.xwtech.xwecp.service.logic.pojo.RES004Result;

public class RES004Test {
    private static final Logger logger = Logger.getLogger(RES002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("预约业务查询");
		lic.setUserBrand("11"); //用户信息里面的brand
		lic.setUserMobile("13913928099");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913928099");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1423200000471569");
		//以下两个是预约系统特有的，必须要传
		ic.addContextParameter("brand", "11");
		ic.addContextParameter("channel", "01");
		lic.setContextParameter(ic);

		
		IOrderOperateService os = new OrderOperateServiceClientImpl();
		
//		RES004Result re = os.orderCancel("B20121226224807100000002");//业务订单取消
		
//		RES004Result re = os.orderCancel("M20121228011242100000000");//营销案订单取消
		
		OrderUpdateInfo  orderUpdateInfo = new OrderUpdateInfo();//业务修改
		orderUpdateInfo.setOfficeId("20130301");
		orderUpdateInfo.setExpectTime("2013-03-01");
		orderUpdateInfo.setExpectPeriod("1");
		orderUpdateInfo.setBz("ttttt");
//		RES004Result re = os.orderUpdate("B20121226224807100000002000", orderUpdateInfo);
		
		
//		OrderUpdateInfo orderUpdateInfo = new OrderUpdateInfo();//营销方案修改
		orderUpdateInfo.setOfficeId("20130401");
		orderUpdateInfo.setExpectTime("2013-04-01");
		orderUpdateInfo.setExpectPeriod("2");
		orderUpdateInfo.setBz("test");
		RES004Result re = os.orderUpdate("M20121223000000182", orderUpdateInfo);
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			logger.info(" =========================================== ");
		}
	
	}
}
