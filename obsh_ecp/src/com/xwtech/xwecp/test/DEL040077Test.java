package com.xwtech.xwecp.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGiveOrTakeRedPackService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GiveOrTakeRedPackServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040077Result;
/**
 * 红包赠送与领取
 * 
 * @author xufan
 * 2014-03-27
 */
public class DEL040077Test
{
	private static final Logger logger = Logger.getLogger(DEL040077Test.class);
	
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
		IGiveOrTakeRedPackService co = new GiveOrTakeRedPackServiceClientImpl();
		Map<String,Object>map=new HashMap<String,Object>();
		map.put("msisdn1", "13813382424");//赠送方手机号码
		map.put("user_id1", "1419200008195116");//赠送方用户ID
		map.put("msisdn2", "14705178780");//接收方手机号码
		map.put("user_id2", "1419300014667224");//接收方用户ID
		map.put("package_type", 1);//套餐类型:1 包10M ；2包70M；3 包150；4包30M
		map.put("package_code", "2000003540");//红包档次 2000003540 3元000003541 5元2000003754 10元2000003755 20元
		map.put("package_inure_mode", 1);//生效方式1：立即，2：次日3：次月，如果外围不传默认立即。
		map.put("donateoid", "");//赠送流水号
		map.put("giveOrTake",0);//领取还是赠送 0赠送 1领取
		DEL040077Result re = co.giveOrTakeRedPack(map);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}
