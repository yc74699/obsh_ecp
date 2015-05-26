package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBillDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBillDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.GsmBillDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010001Result;

public class QRY010001Test {
	private static final Logger logger = Logger.getLogger(QRY010001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp_test/xwecp.do");
		//props.put("platform.url", "http://10.32.229.73:8080/js_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.74:8080/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13851382595");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13851382595");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
     	ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_passwd", "AE6A5396C8A7E892");
		ic.addContextParameter("user_id", "1315200003768705");
		
		lic.setContextParameter(ic);
		
		IQueryBillDetailService co = new QueryBillDetailServiceClientImpl();
		QRY010001Result re = co.queryBillDetail("13851382595", "201402", "20140201",22, 0);
		List<GsmBillDetail> retList = re.getGsmBillDetail();
		System.out.println("记录数：" + retList.size());
		logger.info(" ====== 开始返回参数 ======");
	}
}
