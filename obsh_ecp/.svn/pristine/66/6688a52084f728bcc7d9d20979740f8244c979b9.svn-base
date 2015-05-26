package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryOrderInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryOrderInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.OrderDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040090Result;

public class QRY040090Test {
	private static final Logger logger = Logger.getLogger(QRY040090Test.class);
	public static void main(String[] args) throws Exception
	{
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13515248887");
		lic.setUserMobile("13515248887");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13515248887");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		
		/**
		 * qrytype	1	int	F2	查询类型	1  按手机号查询  2  按订单号查询
			qryid	1	string	V20	查询标识	根据查询类型zzzz而定，如按手机号查询，该值为客户手机号码；如按订单号查询，则是客户业务受理订单号；
			operid	1	string	V32	营业员工号	必填，通过营业员工号查询所归属该厅的所有订单

		 */
		
		IQryOrderInfoService qryOrderInfoService = new QryOrderInfoServiceClientImpl();
		//按号码查询
		QRY040090Result re = qryOrderInfoService.qryOrderInfo(2, "88283080061697", "sysadmin");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			for(OrderDetail od : re.getOrderList()){
				System.out.println(" ====== getOrderId ======" + od.getOrderId());
			}
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
		//按订单账号查询
//		re = qryOrderInfoService.qryOrderInfo(2, "88282439418757", "");
//		if (re != null)
//		{
//			for(OrderDetail od : re.getOrderList()){
//				System.out.println(" ====== getOrderId ======" + od.getOrderId());
//			}
//			System.out.println(" ====== getResultCode ======" + re.getResultCode());
//			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
//		}
		
	}
}
