package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryAllBrandOrderService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryAllBrandOrderServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050019Result;

public class QRY050019Test {

	public static void main(String[] args) {

		Properties props = new Properties();
		props.put("client.channel", "terminal_channel");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setUserMobile("13913313873");
		lic.setUserCity("14");
//		lic.setBizCode("biz_code_19234");
//		lic.setOpType("开通/关闭/查询/变更");
		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "15951872765");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		XWECPLIClient.createInstance(props);
		 IQueryAllBrandOrderService queryAllBrandOrderService = new QueryAllBrandOrderServiceClientImpl();
		try {
			QRY050019Result retOrder = queryAllBrandOrderService.queryAllBrandOrder(2, "321088198704116912", "NJDQ");
			if(retOrder!=null)
			{
				System.out.println(retOrder.getResultCode());
				System.out.println(retOrder.getErrorMessage());
				System.out.println(retOrder.getErrorCode());
//				retOrder.getOrderInfos();
			}
			else
			{
				System.out.println("空");
			}
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
