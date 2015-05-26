package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryTbankDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryTbankDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040083Result;
import com.xwtech.xwecp.service.logic.pojo.TbankDetail;
public class QRY040083Test {
	private static final Logger logger = Logger.getLogger(QRY040083Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("8");
		lic.setUserMobile("13851664447");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13851664447");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "2");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200000755198");
		
		lic.setContextParameter(ic);
		//
		
		IQueryTbankDetailService co = new QueryTbankDetailServiceClientImpl();
		QRY040083Result re = co.queryTbankDetail("15005156863", "1", "", "", "");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getResultCode ======" + re.getErrorMessage());
			for(TbankDetail t: re.getTbankDetails())
			{
				System.out.println(t.getAmount());
				
			}
				
		
		}
	}
}
