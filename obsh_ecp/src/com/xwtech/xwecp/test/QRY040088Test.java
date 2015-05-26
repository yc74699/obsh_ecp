package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryOrderStatusService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryOrderStatusServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040088Result;

/**
 * 订单状态查询接口
 * @author YangXQ
 * 2014-10-23
 */
public class QRY040088Test
{
	private static final Logger logger = Logger.getLogger(QRY040088Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "");	
		lic.setContextParameter(ic);
 
		IQueryOrderStatusService co = new QueryOrderStatusServiceClientImpl();
		// 入参 ： 号码
		QRY040088Result re = co.queryOrderStatus("12","212201409202517822714");//  地区编码   订单编码 
		System.out.println(" ================================================");
		if (re != null)
		{
			System.out.println(" ====== Ret_code  ======" + re.getRet_code());
			System.out.println(" ====== Ret_msg ======" + re.getRet_msg()); 
			System.out.println(" ====== Stauts ======" + re.getStauts()); // Stauts 为0 时，表示审核通过，否则审核不通过
		}
	}
}
