package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCreditInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryCreditInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CreditDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY040058Result;

public class QRY040058Test
{
	private static final Logger logger = Logger.getLogger(QRY040058Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
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
		
		ic.addContextParameter("user_id", "1419200008195116");  //2056200011182291
		
		lic.setContextParameter(ic);
		//1105300025226117  13813382424   1419200008195116
		
		IQueryCreditInfoService is = new QueryCreditInfoServiceClientImpl();
		
		QRY040058Result re = is.getQueryCreditInfo("1210200003324821");
		
		logger.info(" ====== 开始返回参数 ======");
		if(null != re){
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			
			logger.info(" ====================================================");
			logger.info(" ====== getCreditlevel ======" + re.getCreditlevel());
			logger.info(" ====== re.getSource ======" + re.getSource());
			
			logger.info(" ====== 信用详细参数 ======");
			if(null != re.getCreditDetails() && re.getCreditDetails().size() > 0){
				for(CreditDetail detail :re.getCreditDetails()){
					logger.info(" ====== 信用服务类型 ======" + detail.getCreditservid());
					logger.info(" ====== 信用额度 ======" + detail.getCreditvalue());
					logger.info(" ====== 信用时长 ======" + detail.getCreditduration());
					logger.info(" ====== 生效日期 ======" + detail.getStart_date());
					logger.info(" ====== 失效日期 ======" + detail.getEnd_date());
				}
			}
		}
		
	}	
}
