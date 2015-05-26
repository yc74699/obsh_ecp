package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.reserve.IDoReservationService;
import com.xwtech.xwecp.service.logic.client_impl.reserve.impl.DoReservationServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.RES003Result;
import com.xwtech.xwecp.service.logic.pojo.ReserveOrderInfo;

public class RES003Test
{
	private static final Logger logger = Logger.getLogger(RES003Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "market_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.153:10006/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "ytf");
		props.put("platform.password", "ytf");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("预约业务查询");
		lic.setUserBrand("11");
		lic.setUserMobile("13913928099");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15151868645");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		//以下两个是预约系统特有的，必须要传
		ic.addContextParameter("brand", "11");
		ic.addContextParameter("channel", "01");
		lic.setContextParameter(ic);
	
		IDoReservationService co = new DoReservationServiceClientImpl();
		/**
		 * 依次是
		 * 手机号
		 * 业务或者营销案编码
		 * 1-预约；2-取消预约
		 * 1-业务；2-营销案
		 * 营业厅编码，业务预约可空
		 * 到厅日期，可空
		 * 1-上午；2-下午；3-全天
		 * 
		 */
		RES003Result re = co.doReservation("13770492424", "B0002", "1", "1", "14118413", "20130112" ,"2","xxxxxxxxx"); //预约业务
		//RES003Result re = co.doReservation("15151868645", "MS00081", "1", "2", "14118413", "20121227" ,"1");  //预约营销案
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
			logger.info(" === re.getOrderId() === " + re.getOrderId());
		}
	}
}
