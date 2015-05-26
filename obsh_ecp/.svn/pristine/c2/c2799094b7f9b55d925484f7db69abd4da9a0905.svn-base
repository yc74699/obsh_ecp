package com.xwtech.xwecp.test;

import com.xwtech.xwecp.service.logic.pojo.DEL610039Result;
import com.xwtech.xwecp.service.logic.client_impl.common.IAliPay4LanService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.AliPay4LanServiceClientImpl;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import java.util.Properties;

public class DEL610039Test
{
	private static final Logger logger = Logger.getLogger(DEL610039Test.class);
		public static void main(String[] args) throws Exception
	{
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		//		props.put("platform.user", "xl");
		//		props.put("platform.password", "xl");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13665212424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13665212424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "23");
		ic.addContextParameter("ddr_city", "23");
		lic.setContextParameter(ic);
		String phoneNum = "13665212424";
		String amount = "100";
		String unionPaySrl = "2388271167074434";
		IAliPay4LanService aliPay4LanService= new AliPay4LanServiceClientImpl();
		DEL610039Result re = aliPay4LanService.aliPay4Lan(phoneNum,amount,unionPaySrl);
			System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			System.out.println(re.getItemUrl());
			String beginIndex =re.getItemUrl();
			
			System.out.println(re.getItemUrl().substring(beginIndex.indexOf("total_fee\" value=\""),beginIndex.indexOf("<input type=\"hidden\" name=\"subject\" value=\"手机话费")));
		}
	}
}