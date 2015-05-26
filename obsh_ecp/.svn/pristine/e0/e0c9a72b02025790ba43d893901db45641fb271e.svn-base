package com.xwtech.xwecp.test;

import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.UserMarketBInfo;
import com.xwtech.xwecp.service.logic.pojo.DEL040104Result;
import com.xwtech.xwecp.service.logic.client_impl.common.IMarketPlanApplyForIMEIService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.MarketPlanApplyForIMEIServiceClientImpl;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import java.util.Properties;

public class DEL040104Test
{
	private static final Logger logger = Logger.getLogger(DEL040104Test.class);
		public static void main(String[] args) throws Exception
	{
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
				props.put("platform.user", "xl");
				props.put("platform.password", "xl");
//		props.put("platform.user", "jhr");
//		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
//		lic.setUserCity("12");
//		lic.setUserMobile("13770492424");
//		lic.setUserMobile("13770492424");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13770492424");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "13770492424");
//		ic.addContextParameter("ddr_city", "12");
//		lic.setContextParameter(ic);
//		String servicesType = "4";
//		String userId = "1211200001048404";
//		String effectFlag = "1";
//		String bossMmsPackId = "";
//		int smsFlag = 1;
//		int status = 1;
//		int idType = 1;
//		int receiveType = 0;
//		String gradeAmount = "3000";
//		String busiPackId = "";
//		List<UserMarketBInfo> userMarketBaseInfo = new ArrayList<UserMarketBInfo>();
//		UserMarketBInfo umBInfo = new UserMarketBInfo();
//		umBInfo.setAgreementSpecId("");
//		umBInfo.setEnterReason("");
//		umBInfo.setOperatorId("12011001");
//		umBInfo.setPhoneNum("1211200001048404");
//		umBInfo.setPlanId("122511000101");//可以立即生效的
//		umBInfo.setPlanInfoId("");
//		userMarketBaseInfo.add(umBInfo);
//		
//		String rewardList = "2220000000427";
//		String type = "1";
//		String imei = "2014110510225";
		
		
		//宽带办理
		
		lic.setUserCity("12");
		lic.setUserMobile("13912077691");
		lic.setUserMobile("13912077691");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13912077691");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13912077691");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		String servicesType = "4";
		String userId = "1211200001048404";
		String effectFlag = "1";
		String bossMmsPackId = "";
		int smsFlag = 1;
		int status = 1;
		int idType = 1;
		int receiveType = 0;
		String gradeAmount = "3000";
		String busiPackId = "";
		List<UserMarketBInfo> userMarketBaseInfo = new ArrayList<UserMarketBInfo>();
		UserMarketBInfo umBInfo = new UserMarketBInfo();
		umBInfo.setAgreementSpecId("");
		umBInfo.setEnterReason("");
		umBInfo.setOperatorId("12011001");
		umBInfo.setPhoneNum("1211200001048404");
		umBInfo.setPlanId("300001282032");//可以立即生效的
		umBInfo.setPlanInfoId("");
		userMarketBaseInfo.add(umBInfo);
		
		String rewardList = "88009923949874";
		String type = "1";
		String imei = "2014110510224";
		
		IMarketPlanApplyForIMEIService marketPlanApplyForIMEIService= new MarketPlanApplyForIMEIServiceClientImpl();
		DEL040104Result re = marketPlanApplyForIMEIService.marketPlanApplyForIMEI(servicesType,userId,effectFlag,bossMmsPackId,smsFlag,status,idType,receiveType,gradeAmount,busiPackId,userMarketBaseInfo,rewardList,imei,type);
			System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}