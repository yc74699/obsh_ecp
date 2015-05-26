package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.reserve.IOrderReceiptService;
import com.xwtech.xwecp.service.logic.client_impl.reserve.impl.OrderReceiptServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.OrderReceiptInfo;
import com.xwtech.xwecp.service.logic.pojo.RES005Result;

public class RES005Test {
    private static final Logger logger = Logger.getLogger(RES002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "openapi_channel");
		props.put("platform.url", "http://127.0.0.1:8080/js-ecp/xwecp.do");
		props.put("platform.user", "fgh");
		props.put("platform.password", "fgh");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("预约业务回单");
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

		
		IOrderReceiptService os = new OrderReceiptServiceClientImpl();
		
//		RES004Result re = os.orderCancel("B20121226224807100000002");//业务订单取消
		
//		RES004Result re = os.orderCancel("M20121228011242100000000");//营销案订单取消
		
		OrderReceiptInfo  orderReceiptInfo = new OrderReceiptInfo();//业务修改
		orderReceiptInfo.setOrderState("3");
		orderReceiptInfo.setOrderErrorResult("0");
		orderReceiptInfo.setOrderErrorBz("");
//		RES004Result re = os.orderUpdate("B20121226224807100000002000", orderUpdateInfo);
		
		
//		OrderUpdateInfo orderUpdateInfo = new OrderUpdateInfo();//营销方案修改
		RES005Result re = os.orderReceipt("M20121223000000182", orderReceiptInfo);
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			logger.info(" =========================================== ");
		}
	
	}
}
