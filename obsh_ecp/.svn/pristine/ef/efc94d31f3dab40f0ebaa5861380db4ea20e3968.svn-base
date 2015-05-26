package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IChangeUserInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.IOpenpCustService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ChangeUserInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.OpenpCustServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CallFeeAccount;
import com.xwtech.xwecp.service.logic.pojo.DEL030003Result;
import com.xwtech.xwecp.service.logic.pojo.NewBizAccount;
import com.xwtech.xwecp.service.logic.pojo.DEL030002Result;
import com.xwtech.xwecp.service.logic.pojo.SepcAccount;
import com.xwtech.xwecp.service.logic.pojo.SpecAccountDetail;
import com.xwtech.xwecp.service.logic.pojo.YearlyPayDetail;

public class DEL030003Test
{
	private static final Logger logger = Logger.getLogger(DEL030003Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15298395174");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("country", "1419");
		lic.setContextParameter(ic);
		
		
		IOpenpCustService service = new OpenpCustServiceClientImpl();
		DEL030003Result result = service.openpCust("15298395174", "帅气锅", "320107198604110319", "帅气大学", "帅气学院");
		System.out.println();
	}
}
