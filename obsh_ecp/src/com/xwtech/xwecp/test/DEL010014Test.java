package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.ISMSDeal172;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.SMSDeal172ClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010014Result;

public class DEL010014Test
{
	private static final Logger logger = Logger.getLogger(DEL010014Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913032424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("login_msisdn", "13913032424");
		lic.setContextParameter(ic);
		
		ISMSDeal172 service = new SMSDeal172ClientImpl();

		String msisdn ="13913032424";
		
		//路由字段
		long ddrCity = 14;
		
		//是否包月 (0-非包月; 1-包月)
		long feeType = 0;;
		
		//17201业务密码
		String user172Password = ""; 

		//17201业务密码(作校验用)
		String user172OldPassword = "";
		
		//1-当月生效，2-次月生效
		String oprType = "";
		
		/**开通wlan传200，修改wlan传201
		开通17202包月计费（20的）：auto_1861_code=100，package_code=4151
		开通17202包月计费（50的）：auto_1861_code=100，package_code=4152
		开通17202按时长计费：auto_1861_code=100，package_code=3006
		取回17202密码：auto_1861_code=101，package_code=0
		开通WLAN功能 ：auto_1861_code=200，package_code=2
		重置WLAN密码：auto_1861_code=202，package_code=2
        **/
		long packageCode = 4152;
		long auto1861Code = 100;
		
		DEL010014Result result = service.smsDeal172(msisdn, ddrCity, feeType,
													user172Password,user172OldPassword,
													oprType,packageCode,auto1861Code);
		logger.info(" ====== 开始返回参数 ======");
		
		if (result != null)
		{
			logger.info(" ====== getResultCode ======" + result.getResultCode());
			logger.info(" ====== getErrorCode ======" + result.getErrorCode());
			logger.info("========getErrorMessage==="+ result.getErrorMessage());			logger.info("========getErrorMessage==="+ result.getErrorMessage());
			logger.info("========getUser172Password==="+ result.getUser172Password());

		}
	}
}
