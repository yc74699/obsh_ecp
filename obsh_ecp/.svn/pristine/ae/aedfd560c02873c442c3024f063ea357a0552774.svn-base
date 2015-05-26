
package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryNewRealTimeBillingCHService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryNewRealTimeBillingCHServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.FeeDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040108Result;

public class QRY040108Test {
	private static final Logger logger = Logger.getLogger(QRY040108Test.class);
	
	public static void main(String[] args) throws Exception
	{
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("1");
		lic.setUserBrand("1");
		lic.setUserCity("14");
		lic.setUserMobile("13852280375");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13852280375");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1422200000198380");
		
		lic.setContextParameter(ic);
		
		IQueryNewRealTimeBillingCHService co = new QueryNewRealTimeBillingCHServiceClientImpl();
		/**
		 * idType = (long)6;
		modeId = 10000001
		qryType = (Integer)1;
		startCycle传 201112的格式。到月份就行了
		Integer billType = 2;// 账单类型(默认2)
		Integer billLevel = 3;	

		 */
		QRY040108Result rs = co.queryNewRealTimeBillingCH("13852280375", (long)1, "10000001", "201412", "201503", (Integer)2, (Integer)3,"1");
		System.out.println("getCycle: "+rs.getCycle());
		System.out.println("getGroupPay: "+rs.getGroupPay());
		System.out.println("getOtherPay: "+rs.getOtherPay());
		System.out.println("getTotalFee: "+rs.getTotalFee());
		List<FeeDetail> lists = rs.getFeeDetailList();
		if(lists != null && lists.size() > 0)
		{
		}

	}
}
