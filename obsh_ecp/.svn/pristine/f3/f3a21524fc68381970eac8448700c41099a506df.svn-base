package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IAliPayService;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransactYxfaService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.AliPayServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransactYxfaServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040029Result;
import com.xwtech.xwecp.service.logic.pojo.DEL060001Result;


    
public class DEL060001Test
{
	private static final Logger logger = Logger.getLogger(DEL060001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "market_channel");
		//props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10000/js_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.71:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.91:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.167:10000/js_ecp/xwecp.do");
		props.put("platform.user", "ytf");
		props.put("platform.password", "ytf");
		 
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		//lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		//ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "15");
		ic.addContextParameter("ddr_city", "15");
		//ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		//ic.addContextParameter("user_id", "1419200008195160");
//		南京     1488140645225351 //流水
//		苏州	    1188049687190048
//		无锡	    1988076333154765
//		常州  	1788091609987713
//		南通		2088102148979766
//		镇江	    1888082199650137
//		扬州	    2388069529950894
//		泰州	    2188090479015206
//		徐州		1688143613280699
//		盐城		2288065933440514
//		淮安	    1288088934829657
//		连云港	1588143604761675
//		宿迁	    1388083906168423
		lic.setContextParameter(ic);
		
		ITransactYxfaService co = new TransactYxfaServiceClientImpl();
		String  bossmms_services_type="1";//1非充值类 ，4充值类
		String  ddr_city="15";
		String usermarketingbaseinfo_user_id="1527300001533678"; //手机号码唯一id
		int   id_type=0; //1充值，0非充值
		//String  detail_operating_srl=ailPay("18","13775321176","480");  //银联号2088127304263590
		String  detail_operating_srl="";
		String  boss_busi_pack_id="";//业务编码
		
		String  bossgiftid="";//礼品编码
		String  creditreleasegrade_grade_amount="0";//钱
		String  usermarketingbaseinfo_plan_id="300003054102";//二级编码
		//88017064817546|88016748381706|88017064837314|88016852833810|88017064837314|88016852840830
		String bizGiftList="88017144448194|88017144448222|88017144448478|88017144448366|88017144448478|88017144448402";
		DEL060001Result re = co.transactYxfa(bossmms_services_type, ddr_city, usermarketingbaseinfo_user_id, id_type, detail_operating_srl, boss_busi_pack_id, bossgiftid, creditreleasegrade_grade_amount, usermarketingbaseinfo_plan_id,bizGiftList);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
	
	public static String ailPay(String city,String number,String payMoney){
		Properties props = new Properties();
		props.put("client.channel", "market_channel");
		//props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10000/js_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.71:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.91:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.167:10000/js_ecp/xwecp.do");
		props.put("platform.user", "ytf");
		props.put("platform.password", "ytf");
		 
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity(city);
		lic.setUserMobile(number);
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", number);
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", city);
		ic.addContextParameter("ddr_city", city);
		lic.setContextParameter(ic);
		IAliPayService service = new AliPayServiceClientImpl();
		try {
			DEL040029Result payResult = service.aliPay(number, payMoney, "directPay"," ", 2);
			if(null !=payResult  )
			 {
				return payResult.getOut_trade_no();
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
