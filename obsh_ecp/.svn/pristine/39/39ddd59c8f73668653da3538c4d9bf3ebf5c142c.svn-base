package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryWlanUsedInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryWlanUsedInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040050Result;

public class QRY040050Test {
private static final Logger logger = Logger.getLogger(QRY040050Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		//props.put("platform.url", "http://10.32.229.74:8080/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("13861437474");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13861437474");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "11");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101300022696521");
		
		lic.setContextParameter(ic);
		
		IQueryWlanUsedInfoService co = new QueryWlanUsedInfoServiceClientImpl();
		QRY040050Result re = co.queryWlanUsedInfo("13861437474", "201301");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			System.out.println(" ====== getTotalWlan ======" + re.getTotalWlan());
			System.out.println(" ====== getUsedWlan ======" + re.getUsedWlan());
		}
	}
}
