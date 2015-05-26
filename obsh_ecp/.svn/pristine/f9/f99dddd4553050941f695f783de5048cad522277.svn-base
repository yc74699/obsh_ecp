package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.reserve.IQryAmountByOfficeService;
import com.xwtech.xwecp.service.logic.client_impl.reserve.impl.QryAmountByOfficeServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.RES002Result;
import com.xwtech.xwecp.service.logic.pojo.ReserveOrderInfo;

public class RES002Test
{
	private static final Logger logger = Logger.getLogger(RES002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
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
		//以下两个是预约系统特有的，必须要传
		ic.addContextParameter("brand", "11");
		ic.addContextParameter("channel", "01");
		lic.setContextParameter(ic);

		IQryAmountByOfficeService co = new QryAmountByOfficeServiceClientImpl();
		/**
		 * 依次是
		 * 营业厅编号
		 * 预约办理日期YYYYMMDD
		 * 预约办理时段 1-上午 2-下午 3-全天
		 * 查询类型1-查明细 2-查数量 目前只支持传2
		 * 
		 */
		RES002Result re = co.qryAmountByOffice("20111", "20121231", "2", "2");
		//RES001Result re = co.qryReserveOrderInfo("13911111113","1","B0002");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
					logger.info(" ====== getAmount======" + re.getAmount());
			
					logger.info(" =========================================== ");
				}
	
	}
}
