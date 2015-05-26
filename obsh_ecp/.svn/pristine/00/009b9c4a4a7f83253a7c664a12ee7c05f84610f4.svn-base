package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICashpaymentflowService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CashpaymentflowServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040116Result;

/**
 * 现金购买流量
 * @author YangXQ
 * 2015-05-07
 */
public class DEL040116Test
{
	private static final Logger logger = Logger.getLogger(DEL040116Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");  
//		props.put("platform.url", "http://10.32.65.238:8080/sms_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.152:10003/obsh_ecp/xwecp.do");  
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
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");	
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "");   			
		lic.setContextParameter(ic);//13641582424 - 2157200003124230
		
		
		ICashpaymentflowService co  = new CashpaymentflowServiceClientImpl();
		DEL040116Result re = co.cashpaymentflow("a","a","a","a",0,
				"0","a","a","a","a","a","a");  
		
//		aliPayNew(String phoneNum, String amount, String paymethod, String bankcode, int payFlag,
//				String formnum, String accesstype, String levelid,String paysubtype,String payflowvalue,String startdate,String enddate)
//入参说明
//手机号码 1
//充值金额 1  单位：分
//支付宝充值类型:赋值:bankPay(网银);cartoon(卡通); directPay(余额) 
//银行编码		
//充值类型  0– 现金 1 –余额 2 – 赠送   1	
		
//外部流水号1
//受理渠道1       掌厅19，网厅 4
//充值档次编码1   0：0.2元1M流量 1：100元1G流量
//交易子类型1     1001国内通用、1002省内流量、1003国内4G单模、1004省内4G单模
//购买流量值1     单位KB，无小数
//流量开始时间1   YYYYMMDDHH24MISS
//流量结束时间1   YYYYMMDDHH24MISS

		//旧接口必传
//		<user_msisdn>$phoneNum</user_msisdn>
//		<user_city>$cityId</user_city>
//		<ddr_city>$cityId</ddr_city>
//		<oper_source>9</oper_source>
//		<bu_open_flag>$payFlag</bu_open_flag>
//		<bank_type>$bankType</bank_type>
	
		//新
//	      <servnumber>$servnumber</servnumber>      
//	      <formnum>$formnum</formnum>--      
//	      <paytype>$paytype</paytype>      
//	      <accesstype>$accesstype</accesstype>--	      
//	      <levelid>$levelid</levelid>--
//	      <payfee>$payfee</payfee>
//	      <paysubtype>$paysubtype</paysubtype>
//	      <payflowvalue>$payflowvalue</payflowvalue>      
//	      <startdate>$startdate</startdate>
//	      <enddate>$enddate</enddate>
		
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode()   ======= " + re.getResultCode());
			logger.info(" === re.getErrorCode()    ======= " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() ======= " + re.getErrorMessage());
		}
	}
}
