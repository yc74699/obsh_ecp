package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryJtmsService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryJtmsServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050010Result;
import com.xwtech.xwecp.service.logic.pojo.TrffInfo;

public class QRY050010Test
{
	private static final Logger logger = Logger.getLogger(QRY050010Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/xwecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419200008195160");
		
		lic.setContextParameter(ic);
		
		IQueryJtmsService co = new QueryJtmsServiceClientImpl();
		QRY050010Result re = co.queryJtms("13913032424");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			if (null != re.getTrffInfo() && re.getTrffInfo().size() > 0)
			{
				logger.info(" ====== re.getTrffInfo.size() ====== " + re.getTrffInfo().size());
				for (TrffInfo dt : re.getTrffInfo())
				{
					logger.info(" ====== getPrdCode ======" + dt.getPrdCode());
					logger.info(" ====== getStatus ======" + ("2".equals(dt.getStatus())?"已开通":"未开通"));
					logger.info(" ====== getBeginDate ======" + dt.getBeginDate());
					logger.info(" ====== getEndDate ======" + dt.getEndDate());
					logger.info(" ====== getCarId ======" + dt.getCarId());
					logger.info(" ====== getDriver1 ======" + dt.getDriver1());
					logger.info(" ====== getDriver2 ======" + dt.getDriver2());
					logger.info(" ====== getCarType ======" + (null != dt.getCarType()?(dt.getCarType().equals("1")?"大型车":("2".equals(dt.getCarType())?"小型车":"外籍车")):""));
					logger.info(" ==================================== ");
				}
			}
		}
	}
}
