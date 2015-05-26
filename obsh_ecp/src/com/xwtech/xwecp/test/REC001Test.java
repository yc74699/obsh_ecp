package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetRecommendInfoService;
import   com.xwtech.xwecp.service.logic.client_impl.common.impl.GetRecommendInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.REC001Result;
import com.xwtech.xwecp.service.logic.pojo.ActivitiesDetail;
	  
public class REC001Test
{
	private static final Logger logger = Logger.getLogger(REC001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10000/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("23");
		lic.setUserMobile("13776632514");  
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13921909348");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "2371200001226345");
		
		lic.setContextParameter(ic);
		
		IGetRecommendInfoService co = new GetRecommendInfoServiceClientImpl();
		REC001Result re = co.getRecommendInfo("13776632514", "14", "457");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 可查询个数 ======" + re.getMmsDealDetail().size());
			
			if (null != re && re.getMmsDealDetail().size() > 0)
			{
				for (ActivitiesDetail dt : re.getMmsDealDetail())
				{
					logger.info(" ====== getAccess_type_id ======" + dt.getAccess_type_id());
					logger.info(" ====== getActivities_area ======" + dt.getActivities_area());
					logger.info(" ====== getActivities_big_pic ======" + dt.getActivities_big_pic());
					logger.info(" ====== getActivities_code ======" + dt.getActivities_code());
					logger.info(" ====== getActivities_first_code ======" + dt.getActivities_first_code());
					logger.info(" ====== getActivities_name ======" + dt.getActivities_name());
					logger.info(" ====== getActivities_offline_time ======" + dt.getActivities_offline_time());
					logger.info(" ====== getActivities_online_time ======" + dt.getActivities_online_time());
					logger.info(" ====== getActivities_page_name ======" + dt.getActivities_page_name());
					logger.info(" ====== getActivities_pic ======" + dt.getActivities_pic());
					logger.info(" ====== getActivities_state ======" + dt.getActivities_state());
					logger.info(" ====== getGrade ======" + dt.getGrade());
					logger.info("======= getActivites_code2 ======" +dt.getActivites_code2());
					logger.info("======= getActivites_page_name2 ======" +dt.getActivites_page_name2());
					logger.info("======= getActivites_name2 ======" +dt.getActivites_name2());
					logger.info("=================================================================");
				}
			}
		}
	}
}
