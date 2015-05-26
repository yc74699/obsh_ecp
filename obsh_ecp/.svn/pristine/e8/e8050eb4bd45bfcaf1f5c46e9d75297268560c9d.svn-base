
package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserScoreNewService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserScoreNewServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.NewScoreDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY030011Result;
import com.xwtech.xwecp.service.logic.pojo.YearScoreDetail;

public class QRY030011Test 
{
	private static final Logger logger = Logger.getLogger(QRY040004Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		//props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.223/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.81:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.86:8080/openapi_ecp/xwecp.do");
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
		
		IQueryUserScoreNewService service = new QueryUserScoreNewServiceClientImpl();
		QRY030011Result re = service.queryUserScoreNew("13913814503","1","2012","2012");
		
		logger.info(" ====== getResultCode ======" + re.getResultCode());
		logger.info(" ====== getErrorCode ======" + re.getErrorCode());
		logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
		
		if(re.getNewScoreDetail() != null)
		{
			for(NewScoreDetail nsd : re.getNewScoreDetail())
			{
				System.out.println("id: "+nsd.getScorenId());
				System.out.println("score: "+nsd.getScore());
			}
		}
		if(re.getYearScoreDetail() != null)
		{
			for(YearScoreDetail ysd : re.getYearScoreDetail())
			{
				logger.info(" ====== getYear ======" +ysd.getYear());
				logger.info(" ====== getPhoneScore ======" + ysd.getPhoneScore());
				logger.info(" ====== getBrandScore ======" + ysd.getBrandScore());
				logger.info(" ====== getBountyScore ======" + ysd.getBountyScore());	
				logger.info(" ====== getAgeScore ======" + ysd.getAgeScore());	
				logger.info(" ====== getExchangedScore ======" + ysd.getExchangedScore());	
				logger.info(" ====== getTotalScore ======" + ysd.getTotalScore());	

			}
		}
	}
}
