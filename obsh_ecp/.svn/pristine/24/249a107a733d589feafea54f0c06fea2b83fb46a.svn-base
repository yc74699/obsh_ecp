package com.xwtech.xwecp.test;

import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryMultiNumBlackListService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryMultiNumBlackListServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040056Result;

public class QRY040056Test {	

	private static final Logger logger = Logger.getLogger(QRY040056Test.class);

	public static void main(String[] args) throws LIException {

		// 初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		// 逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913825048");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913825048");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200008195116");
		lic.setContextParameter(ic);

		IQryMultiNumBlackListService is = new QryMultiNumBlackListServiceClientImpl();
		try {
			QRY040056Result re = is.qryMultiNumBlackList("14", "13913825048", "13401979553");
			System.out.println("getQryResult    ======   "+re.getQryResult());
			System.out.println("getListType    ======   "+re.getListType());
		} 
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}