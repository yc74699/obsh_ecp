package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IVirtualFamInfoByTelService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.VirtualFamInfoByTelServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY610002Result;

/**
 * 校验用户是否加入V网
 * @author Administrator
 *
 */
public class QRY610002Test
{
private static final Logger logger = Logger.getLogger(QRY610002Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("12");
		lic.setUserCity("12");
		lic.setUserMobile("15851728596");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15851728596");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		//ic.addContextParameter("user_id", "1208200008843825");
		
		lic.setContextParameter(ic);
		IVirtualFamInfoByTelService famTel = new VirtualFamInfoByTelServiceClientImpl();
		//号码
		QRY610002Result re = famTel.virtualFamInfoByTel("15805237872");//15851728596
		
		//  13401810018
		if(re != null)
		{
			logger.error("===========错误编号============" + re.getErrorCode());
			logger.error("===========错误信息============" + re.getErrorMessage());
			logger.error("===========是否户主============" + re.getISHOUSEHOLD());
			logger.error("===========是否有V网产品============" + re.getHASFMYPROD());
		}
	}
}
