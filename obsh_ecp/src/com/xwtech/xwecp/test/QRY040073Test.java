package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IBucheckdealUnionpayService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.BucheckdealUnionpayServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040073Result;

/**
 * 掌营银联充值业务检查接口 
 * @author YangXQ
 * 2014-6-27
 */
public class QRY040073Test {
	
	private static final Logger logger = Logger.getLogger(QRY040073Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");//obsh_channel jhr
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "xl");
		props.put("platform.password", "xl");//wap_channel  xl 
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("15895096970");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15895096970");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1208200008843787");
		lic.setContextParameter(ic);
		
		IBucheckdealUnionpayService co = new BucheckdealUnionpayServiceClientImpl();
		
		/**
		 * user_msisdn       充值号码
		 * clt_operating_srl 订单号
		 * ddr_city          地市编码
		 * bu_card_id        充值金额
		 * bu_busi_type      固定传的1
		 * String user_msisdn,  String clt_operating_srl,  long ddr_city,  String bu_card_id,  String bu_busi_type
		 * "15895096970",       "1288122425679100",        12,             "1000",             "1"
		 */		
		QRY040073Result re = co.bucheckdealUnionpay("15895096970", "1288122425679100", 12,"1000","1");
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}