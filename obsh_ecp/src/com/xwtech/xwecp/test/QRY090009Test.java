package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.INewCommonScoreTranscommitService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.NewCommonScoreTranscommitServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY090009Result;

public class QRY090009Test
{
	//private static final Logger logger = Logger.getLogger(QRY090007Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://localhost/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13770442064");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13770442064");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		ic.addContextParameter("user_id", "1208200010626798");
		
		lic.setContextParameter(ic);
		
		INewCommonScoreTranscommitService co = new NewCommonScoreTranscommitServiceClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160 
		QRY090009Result re = co.newCommonScoreTranscommit("13651554248", "10", "0", "0", "10", "15061204861");
		//logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(re);
			System.out.println("返回码="+re.getResultCode());
			
		}
	}
}
