package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IOrderMarketActService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.OrderMarketActServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040040Result;
import com.xwtech.xwecp.service.logic.pojo.UserMarketBInfo;



public class DEL040040Test
{
	private static final Logger logger = Logger.getLogger(DEL040040Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200008195116");
		
		lic.setContextParameter(ic);
		List<UserMarketBInfo> lmbList = new ArrayList<UserMarketBInfo>();
		UserMarketBInfo umBInfo = new UserMarketBInfo();
		umBInfo.setAgreementSpecId("");
		umBInfo.setEnterReason("");
		umBInfo.setOperatorId("11011001");
		umBInfo.setPhoneNum("1419200008195116");
		umBInfo.setPlanId("300000410042");
		umBInfo.setPlanInfoId("300000410042");
		lmbList.add(umBInfo);
		
		String rewardList = "";
		IOrderMarketActService co = new OrderMarketActServiceClientImpl();
		DEL040040Result re = co.orderMarketAct("1", "1419200008195116", "1", "6", 1,
			1, 1, 0, "5000", "88011067876594",lmbList,rewardList);
		
	
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 错误信息 ======" + re.getErrorMessage());


		}
	}
}
