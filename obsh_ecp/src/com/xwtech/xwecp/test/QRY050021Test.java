package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUser17202Service;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUser17202ServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050021Result;
import com.xwtech.xwecp.service.logic.pojo.User17202;

public class QRY050021Test
{
	private static final Logger logger = Logger.getLogger(QRY050021Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:8080/xwecp/xwecp.do");
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
		ic.addContextParameter("login_msisdn", "13401312424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419200008195160");
		
		lic.setContextParameter(ic);
		
		IQueryUser17202Service co = new QueryUser17202ServiceClientImpl();
		QRY050021Result re = co.queryUser17202("13913032424");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			if (null != re.getUser17202() && re.getUser17202().size() > 0)
			{
				logger.info(" ====== re.getUser17202.size() ====== " + re.getUser17202().size());
				for (User17202 dt : re.getUser17202())
				{
					logger.info(" ====== getPkgCode ======" + dt.getPkgCode());
					logger.info(" ====== getPwd ======" + dt.getPwd());
					logger.info(" ====== getPkgType ======" + ("3006".equals(dt.getPkgCode()) ? "按时长计费" 
							: ( "4151".equals(dt.getPkgCode()) ? "20元包1800分钟" : "50元包月不限量" ) ));
					logger.info(" ====== getLimitFlag ======" + ("0".equals(dt.getLimitFlag())?"无限制":"唯一性"));
					logger.info(" ====== getStatus ======" + ("1".equals(dt.getStatus())?"正常":("2".equals(dt.getStatus())?"暂停":"注销")));
				}
			}
		}
	}
}
