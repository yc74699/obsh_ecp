package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.INewphoneOrderService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.NewphoneOrderServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010023Result;

/**
 * 带附加属性产品（来话管家）订购
 * @author YangXQ
 * 2014-10-15
 */
public class DEL010023Test
{
	private static final Logger logger = Logger.getLogger(DEL010023Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel"); 
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");  
//		props.put("platform.url", "http://10.32.65.238:8182/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8080/obsh_forward/obsh_js
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("13"); 
		lic.setUserMobile("13584037474");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13584037474");	
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "");   			
		lic.setContextParameter(ic);//13641582424 - 2157200003124230
		INewphoneOrderService co = new NewphoneOrderServiceClientImpl();
		
//入参：  主号号码
//		 产品编码
//		 附加属性ID
//		 附加属性值:  开通或者重置密码时输入密码值	 13655232471, 100866
//		 操作编码 : 1：订购 2：重置密码 0：退订   2的时候 
		DEL010023Result re = co.newphoneOrder("13655232471","LHGJ","s100079","100866","1");  
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getRet_code() =========== " + re.getRet_code());
			logger.info(" === re.getCc_operating_srl() === " + re.getCc_operating_srl());
			logger.info(" === re.getResultCode()   ======= " + re.getResultCode());
			logger.info(" === re.getErrorCode()    ======= " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() ======= " + re.getErrorMessage());
		}
	}
}
