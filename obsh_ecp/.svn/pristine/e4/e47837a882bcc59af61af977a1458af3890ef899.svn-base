package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserScoreNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserScoreNewServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.sms.IQrySMSvbaScoreService;
import com.xwtech.xwecp.service.logic.client_impl.sms.impl.QrySMSvbaScoreServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.NewScoreDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY030011Result;
import com.xwtech.xwecp.service.logic.pojo.QRY030013Result;
import com.xwtech.xwecp.service.logic.pojo.VipBarTradeInfo;
import com.xwtech.xwecp.service.logic.pojo.YearScoreDetail;

public class QRY030013Test 
{
	private static final Logger logger = Logger.getLogger(QRY030013Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
//		props.put("platform.url", "http://10.32.229.81:10000/sms_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913814503");
		ic.addContextParameter("ddr_city", "14");
		
//		ic.addContextParameter("user_id", "1419200009683023");
		
		lic.setContextParameter(ic);
		
		IQrySMSvbaScoreService service = new QrySMSvbaScoreServiceClientImpl();
		QRY030013Result re = service.qrySMSvbaScore("13913814503","15050547190");
		
		logger.info(" ====== getResultCode ======" + re.getResultCode());
		logger.info(" ====== getErrorCode ======" + re.getErrorCode());
		logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		
		if(re.getVipbartradeInfo() != null)
		{
			for(VipBarTradeInfo ysd : re.getVipbartradeInfo())
			{
				logger.info(" ====== dealScore ======" +ysd.getDealScore());
				logger.info(" ====== dealAmount ======" + ysd.getDealAmount());
				logger.info(" ====== leaveAmount ======" + ysd.getLeaveAmount());
				logger.info(" ====== deductAmount ======" + ysd.getDeductAmount());	
				logger.info(" ====== leaveScore ======" + ysd.getLeaveScore());	
				logger.info(" ====== deductScore ======" + ysd.getDeductScore());	
				logger.info(" ====== name ======" + ysd.getName());	
				logger.info(" ====== password ======" + ysd.getPassword());	
			}
		}
	}
}
