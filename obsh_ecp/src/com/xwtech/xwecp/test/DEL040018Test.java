package com.xwtech.xwecp.test;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IDonatePointsService;
import com.xwtech.xwecp.service.logic.client_impl.common.IModifyAgentPwdService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAgentBalanceService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryBillDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGamePointService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryOperDetailService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserEPackageService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.DonatePointsServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ModifyAgentPwdServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAgentBalanceServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryBillDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGamePointServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryOperDetailServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserEPackageServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040016Result;
import com.xwtech.xwecp.service.logic.pojo.DEL040018Result;
import com.xwtech.xwecp.service.logic.pojo.EPkgDetail;
import com.xwtech.xwecp.service.logic.pojo.GamePoint;
import com.xwtech.xwecp.service.logic.pojo.GsmBillDetail;
import com.xwtech.xwecp.service.logic.pojo.OperDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY010001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040004Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040014Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040015Result;
import com.xwtech.xwecp.service.logic.pojo.QRY050003Result;

public class DEL040018Test {
private static final Logger logger = Logger.getLogger(QRY040014TestApp.class);
	
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
		lic.setUserCity("用户县市");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13813382424");
		ic.addContextParameter("ddr_city", "17");
		
		ic.addContextParameter("user_id", "2050200009867279");
		lic.setContextParameter(ic);
		
		IDonatePointsService service = new DonatePointsServiceClientImpl();
		DEL040018Result res = service.donatePoints("13813382424", "1");
		String result = res.getResultCode().equals("0") ? "积分捐赠成功" : "积分捐赠失败";
		System.out.println(result);
		System.out.println("-----end-----------");
		
	
	}
	
	
}
