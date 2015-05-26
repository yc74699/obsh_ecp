package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGSMService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGSMServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY120002Result;
import com.xwtech.xwecp.service.logic.pojo.ServiceSerialBean;

public class QRY120002Test {
	private static final Logger logger = Logger.getLogger(QRY120002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "data_channel");
//		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.100:10004/openapi_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.73:10006/openapi_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.92:10008/sms_ecp/xwecp.do");
		props.put("platform.user", "lw");
		props.put("platform.password", "lw");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("");
		lic.setUserCity("14");
		lic.setUserMobile("13701542424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13701542424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13701542424");
		lic.setContextParameter(ic);
		
		IQueryGSMService co = new QueryGSMServiceClientImpl();
		
		QRY120002Result re = co.queryGSMInfo("13701542424");//13705180803
		logger.info(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			System.out.println(re.getResultCode());
			System.out.println(re.getErrorCode());
			System.out.println(re.getErrorMessage());
			if(null != re.getGsmList())
			{
				logger.info("语音...............");
				for(ServiceSerialBean bean : re.getGsmList())
				{
					
					logger.info("本人号码："+bean.getMsisdn());
					logger.info("月份"+bean.getMonth_number());
					logger.info("对方号码："+bean.getOther_party());
					logger.info("品牌："+bean.getOther_party_big_brand_p());
					logger.info("频次"+bean.getJwq_exp_serial());
					logger.info("-----------------");
				}
			}
			
			
			if(null != re.getSmsList())
			{
				logger.info("短信...............");
				for(ServiceSerialBean bean : re.getSmsList())
				{
					
					logger.info("本人号码："+bean.getMsisdn());
					logger.info("月份"+bean.getMonth_number());
					logger.info("对方号码："+bean.getOther_party());
					logger.info("品牌："+bean.getOther_party_big_brand_p());
					logger.info("频次"+bean.getJwq_exp_serial());
					logger.info("-----------------");
				}
			}

		}
	}
}
