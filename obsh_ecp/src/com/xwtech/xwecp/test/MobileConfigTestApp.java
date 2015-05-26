package com.xwtech.xwecp.test;

import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ISetMobileCfgService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.SetMobileCfgServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040011Result;

public class MobileConfigTestApp {
	
   
	public static void main(String[] args)
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:80/ecp/xwecp.do");
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
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913032424");
		ic.addContextParameter("ddr_city", "20");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		ISetMobileCfgService service = new SetMobileCfgServiceClientImpl();
		try {
			
			//操作类型 此属性固定为"1"
			String type = "1";
			//手机号码
			String phoneNum = "13813382424";
			//配置类型  1：彩信 2：WAP 3：GRPS
			String configType = "3";
			//手机串号
			String emiId = "353215035310154";
			//手机品牌
			String brandId = "2";
			//手机型号
			String typeId = "1";
			
			DEL040011Result dEL040011Result = service.setMobileCfg(type, phoneNum, configType, emiId, brandId, typeId);
			
		    String result = dEL040011Result.getResultCode().equals("0") ? "手机参数设置成功" : "手机参数设置失败";
		    System.out.println("=========" + result + "=============");
			
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
