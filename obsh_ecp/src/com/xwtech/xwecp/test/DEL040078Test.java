package com.xwtech.xwecp.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.INetcardChgconfirmService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.NetcardChgconfirmServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040078Result;
/**
 * 4g换卡操作
 * 
 * @author xufan
 * 2014-04-08
 */
public class DEL040078Test
{
	private static final Logger logger = Logger.getLogger(DEL040078Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp_test/xwecp.do");
//		props.put("platform.url", "http://10.32.65.238:8081/sms_ecp/xwecp.do");
		props.put("platform.user", "xl");
		props.put("platform.password", "xl");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13813382424");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419200008195116");  //2056200011182291
		
		
		lic.setContextParameter(ic);
		
		INetcardChgconfirmService co = new NetcardChgconfirmServiceClientImpl();
		Map<String,Object>map=new HashMap<String,Object>();
		map.put("telnum", "");//号码
		map.put("ecardsn", "");//卡序列号
		map.put("password", "");//密码
		map.put("temptelnum", "");//临时号码
		map.put("type",1);//业务类型，1、换卡申请，2、二次换卡撤销，3、配号待回复模式，4、卡号互联换卡确认(由卡端自动上行到短厅，然后转到CRM)
		map.put("ecardimsi", "");//临时卡IMSI
		map.put("restype", 1);//临时卡资源类型
		map.put("cardregion", "14");//临时卡归属地市
		DEL040078Result re = co.netcardChgconfirm(map);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
