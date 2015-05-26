package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserPackageService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserPackageServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050027Result;
import com.xwtech.xwecp.service.logic.pojo.UserPackage;

public class QRY050027Test
{
	private static final Logger logger = Logger.getLogger(QRY050027Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8080/js_ecp/xwecp.do");
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
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1423200000471569");
		
		lic.setContextParameter(ic);
		
		IQueryUserPackageService co = new QueryUserPackageServiceClientImpl();
		QRY050027Result re = co.queryUserPackageList("13921909348","23");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			if (null != re.getUserPackageList() && re.getUserPackageList().size() > 0)
			{
				logger.info(" ====== re.getUserPackageList.size() ====== " + re.getUserPackageList().size());
				for (UserPackage dt : re.getUserPackageList())
				{
					logger.info(" ====== getProId ======" + dt.getProId());
					logger.info(" ====== getStartDate ======" + dt.getStartDate());
					logger.info(" ====== getEndDate ======" + dt.getEndDate());
					logger.info(" ====== getNextDate ======" + dt.getNextDate());
					logger.info(" ====== getPkgType ======" + dt.getPkgType());
					logger.info(" ====== getPkgId ======" + dt.getPkgId());
					logger.info(" ====== getPkgCode ======" + dt.getPackageCode());
					logger.info(" ====== getPackageName ======" + dt.getPackageName());
					logger.info(" ====== getPackageDesc ======" + dt.getPackageDesc());
					logger.info(" ====== getCurOpen ======" + dt.getCurOpen());
					
					
					logger.info(" ===================================");
				
					
				}
			}
		}
	}
}
