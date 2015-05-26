package com.xwtech.xwecp.test;

import com.xwtech.xwecp.service.logic.pojo.Imei;
import com.xwtech.xwecp.service.logic.pojo.QRY090102Result;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryActBindInfobyImeiNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryActBindInfobyImeiNewServiceClientImpl;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;

import java.util.List;
import java.util.Properties;

public class QRY090102Test
{
	private static final Logger logger = Logger.getLogger(QRY090102Test.class);
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
		lic.setUserCity("12");
//		lic.setUserMobile("13813382424");
//		lic.setUserMobile("13813382424");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13813382424");
//		ic.addContextParameter("route_type", "1");
//		ic.addContextParameter("route_value", "13813382424");
//		ic.addContextParameter("ddr_city", "14");
		
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13770492424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		String imei = "86467802135930";
		IQryActBindInfobyImeiNewService qryActBindInfobyImeiNewService= new QryActBindInfobyImeiNewServiceClientImpl();
		QRY090102Result re = qryActBindInfobyImeiNewService.qryActBindInfobyImeiNew(imei);
			System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			List<Imei> imeis = re.getImeiList();
			if(0 < imeis.size())
			{
				for(Imei imei2 : imeis)
				{
					System.out.println(imei2);
				}
			}
		}
	}
}