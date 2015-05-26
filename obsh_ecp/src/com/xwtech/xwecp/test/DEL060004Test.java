package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactBroadService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransactBroadServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL060004Result;
    
public class DEL060004Test
{
	private static final Logger logger = Logger.getLogger(DEL060004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		 
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		//lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		//ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419200008195116");
		
		lic.setContextParameter(ic);
		
		ITransactBroadService co = new TransactBroadServiceClientImpl();
		String  bossmms_services_type="4";
		String  ddr_city="14";
		String usermarketingbaseinfo_user_id="1419200008195116";
		int   id_type=1; 
		String  detail_operating_srl="1419200008195116"; 
		String  marketingbusiinfo_busi_pack_id="";
		String  bossmms_pack_id="";
		String  creditreleasegrade_grade_amount="3000";
		String  usermarketingbaseinfo_plan_id="3001768035";
		//String rewardlist = "88009932661262|88009945136376";
		String rewardlist = "88210542918051";
		DEL060004Result re = co.transactBroad(bossmms_services_type, ddr_city, usermarketingbaseinfo_user_id, id_type, detail_operating_srl, marketingbusiinfo_busi_pack_id, bossmms_pack_id, creditreleasegrade_grade_amount, usermarketingbaseinfo_plan_id,rewardlist);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
