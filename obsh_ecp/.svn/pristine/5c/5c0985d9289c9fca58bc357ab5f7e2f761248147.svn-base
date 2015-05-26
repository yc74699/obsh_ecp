package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQrysubsappendinfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QrysubsappendinfoServiceClientImpl;

import com.xwtech.xwecp.service.logic.pojo.QRY060076Result;

/*
 * 校园客户夹寄卡资料录入情况查询
 * YangXQ 2014-6-18
 */
public class QRY060076Test {
	private static final Logger logger = Logger.getLogger(QRY060076Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); //渠道名称 wap_channel sms_channel obsh_channel
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");//渠道帐户
		props.put("platform.password", "jhr");//渠道密码 wap_channel pwd: and: user: xl
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("12");//卡在类型
		lic.setUserCity("12");//地市
		lic.setUserMobile("13913032424");//手机号
		
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913032424");//手机号
		ic.addContextParameter("route_type", "2");            //路由类型：1为地市，2为手机号
		ic.addContextParameter("route_value", "13913032424"); 
//		ic.addContextParameter("sys_channel_param", "cz_channel"); 
		
		lic.setContextParameter(ic);
		
		IQrysubsappendinfoService co = new QrysubsappendinfoServiceClientImpl();
		QRY060076Result re = co.qrysubsappendinfo("13913032424");//18351002158
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode   ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());			
			logger.info(" ====== getRet_msg      ======" + re.getRet_msg());
			logger.info(" ====== getRet_code     ======" + re.getRet_code());
			logger.info(" ====== getResult       ======" + re.getResult());
		}
	}
}
