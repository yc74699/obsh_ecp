package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMonthService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMonthServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY030012Result;
import com.xwtech.xwecp.service.logic.pojo.MonthScoreDetail;

public class QRY030012Test
{
	private static final Logger logger = Logger.getLogger(QRY030012Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段  
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13770468111");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13770468111");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
//		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1208200001136600");
		
		
		lic.setContextParameter(ic);
		
		IQueryMonthService queryMonthList = new QueryMonthServiceClientImpl();
		QRY030012Result re = queryMonthList.qryMonthScore("13770468111", "201101","201112", "0");
		List<MonthScoreDetail> scoreList = re.getScoreDetailList();
		

		logger.info(" ====== 开始返回参数 ======");
		logger.info(" ====== getResultCode ======" + re.getBossCode());
		for(MonthScoreDetail scoreSingle : scoreList )
		if (re != null)
		{		
			logger.info(" ====== 有效账期月 =========" + scoreSingle.getValidBillCyc());
			logger.info(" ====== 积分类型 ======" + scoreSingle.getScoreDetailId());
			logger.info(" ====== 积分数值 ======" + scoreSingle.getScoreDetailAmt());
		}
	}
}
