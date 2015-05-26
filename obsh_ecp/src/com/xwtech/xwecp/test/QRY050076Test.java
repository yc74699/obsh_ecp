package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryIsYingXingCard;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryIsYingXingCardClientImpl;
import com.xwtech.xwecp.service.logic.pojo.FluxDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040070Result;
import com.xwtech.xwecp.service.logic.pojo.QRY050076Result;

public class QRY050076Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
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
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13813382424");
//		ic.addContextParameter("route_type", "2");
//		ic.addContextParameter("route_value", "13813382424");
//		ic.addContextParameter("ddr_city", "13813382424");
//		ic.addContextParameter("user_id", "1419200008195116");  //13813382424-1419200008195116,13913032424-1419200008195160
//		lic.setContextParameter(ic);
//		IQueryIsYingXingCard card = new QueryIsYingXingCardClientImpl();
//		QRY050076Result re = card.queryIsYingXingCard("13813382424", "14");
		
//		lic.setUserMobile("15996349759");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "15996349759");
//		ic.addContextParameter("route_type", "2");
//		ic.addContextParameter("route_value", "15996349759");
//		ic.addContextParameter("ddr_city", "15996349759");
//		ic.addContextParameter("user_id", "1419300008990942");  //13813382424-1419200008195116,13913032424-1419200008195160
//		lic.setContextParameter(ic);
//		IQueryIsYingXingCard card = new QueryIsYingXingCardClientImpl();
//		QRY050076Result re = card.queryIsYingXingCard("15996349759", "14");
		
//		lic.setUserMobile("13901585309");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13901585309");
//		ic.addContextParameter("route_type", "2");
//		ic.addContextParameter("route_value", "13901585309");
//		ic.addContextParameter("ddr_city", "13901585309");
//		ic.addContextParameter("user_id", "1419200007452901");  //13813382424-1419200008195116,13913032424-1419200008195160
//		lic.setContextParameter(ic);
//		IQueryIsYingXingCard card = new QueryIsYingXingCardClientImpl();
//		QRY050076Result re = card.queryIsYingXingCard("13901585309", "14");
		long time1 = System.currentTimeMillis();
		lic.setUserMobile("15999048770");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15999048770");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15999048770");
		ic.addContextParameter("ddr_city", "15999048770");
		//ic.addContextParameter("user_id", "1419200007452901");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		
		IQueryIsYingXingCard card = new QueryIsYingXingCardClientImpl();
		QRY050076Result re = card.queryIsYingXingCard("15999048770", "14");
		
//		lic.setUserMobile("13951825843");
//		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13951825843");
//		ic.addContextParameter("route_type", "2");
//		ic.addContextParameter("route_value", "13951825843");
//		ic.addContextParameter("ddr_city", "13951825843");
//		ic.addContextParameter("user_id", "1419200006827882");  //13813382424-1419200008195116,13913032424-1419200008195160
//		lic.setContextParameter(ic);
//		IQueryIsYingXingCard card = new QueryIsYingXingCardClientImpl();
//		QRY050076Result re = card.queryIsYingXingCard("13951825843", "14");
		 
		System.out.println(" ====== 开始返回参数 ======");
	
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
			String string = re.getIsYingXingNum();
			if(null != string )
			{
				System.out.println("是否是迎新卡："+string);
			}
		}
		System.out.println("总共用时。。。"+(System.currentTimeMillis()-time1)+"毫秒");
	}

}
