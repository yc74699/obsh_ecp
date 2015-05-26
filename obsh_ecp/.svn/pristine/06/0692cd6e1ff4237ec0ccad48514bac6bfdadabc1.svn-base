package com.xwtech.xwecp.test;

import com.xwtech.xwecp.service.logic.pojo.DEL040105Result;
import com.xwtech.xwecp.service.logic.client_impl.common.ICuserFundNew;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CuserFundNewClientImpl;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import java.util.Properties;

public class DEL040105Test
{
	private static final Logger logger = Logger.getLogger(DEL040105Test.class);
		public static void main(String[] args) throws Exception
	{
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.166:10005/obsh_ecp/xwecp.do");
				props.put("platform.user", "xl");
				props.put("platform.password", "xl");
//		props.put("platform.user", "jhr");
//		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("XEHFCZ_300");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("21");
		lic.setUserMobile("13813382424");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13809012424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "21");
		ic.addContextParameter("ddr_city", "21");
		lic.setContextParameter(ic);
		//地势
		String cityId = "21";
		//号码
		String phoneNum = "13809012424";
		//充值金额，以分为单位
		String amount = "200";
		//下发短信1，不下发0
		int isSendMsg=1;
		ICuserFundNew cuserFundNew= new CuserFundNewClientImpl();
		DEL040105Result re = cuserFundNew.cuserFundNew(cityId,phoneNum,amount,isSendMsg);
			System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			System.out.println(" == 充值流水号 ==== "+re.getOperatingSrl());
		}
	}
}