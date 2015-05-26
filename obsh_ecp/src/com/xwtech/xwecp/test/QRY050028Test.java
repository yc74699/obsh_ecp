package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPackageDependService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPackageDependServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PackageDepend;
import com.xwtech.xwecp.service.logic.pojo.QRY050028Result;

public class QRY050028Test
{
	private static final Logger logger = Logger.getLogger(QRY050028Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13905189887");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13905189887");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419200006289834");
		
		lic.setContextParameter(ic);
	 
		
		IQueryPackageDependService co = new QueryPackageDependServiceClientImpl();
		//全球通数据
//		QRY050028Result re = co.queryPackageDepend("13913814503", 
//				"100063", new String[]{"1031","1031"}, new String[]{"1741","1742"},  new String[]{"0","1"});
		
		//动感地带数据
//		QRY050028Result re = co.queryPackageDepend("13805157824", "14",
//				"100070","4991","1146","1018");
		
//		QRY050028Result re = co.queryPackageDepend("13805157824", "NJDQ",
//				"100070","TCBG_DGDDYYTC","TCBG_DGDDWLTC","1018");
		
		QRY050028Result re = co.queryPackageDepend("13905189887", "NJDQ",
				"100062","TCBG_QQT188TCC","TCBG_QQT128TC","1031");
	
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			if (null != re.getPackageDependList()&& re.getPackageDependList().size() > 0)
			{
				logger.info(" ====== re.getUserPackageList.size() ====== " + re.getPackageDependList().size());
				for (PackageDepend dt : re.getPackageDependList())
				{
					logger.info(" ====== getOprateBizCode ======" + dt.getOprateBizCode());
					logger.info(" ====== getOprateBizName ======" + dt.getOprateBizName());
					logger.info(" ====== getOptWay ======" + dt.getOptWay());
					logger.info(" ====== getCurStatus ======" + dt.getCurStatus());
					logger.info(" ====== getCanChoose ======" + dt.getCanChoose());
					logger.info(" ====== getOptWay ======" + dt.getOptWay());
					logger.info(" ====== getStartDate ======" + dt.getStartDate());
					logger.info(" ====== getEndDate ======" + dt.getEndDate());
					logger.info(" ====== getNextDate ======" + dt.getNextDate());
					logger.info(" ===================================");
				
					
				}
			}
		}
	}
}
