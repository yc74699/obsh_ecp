package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITransMarketBatchService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TransMarketBatchServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL100006Result;
import com.xwtech.xwecp.service.logic.pojo.RecOidList;

public class DEL100006Test
{
	private static final Logger logger = Logger.getLogger(DEL100006Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "terminal_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8081/obsh_ecp/xwecp.do");
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13861590233");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13861590233");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		

		lic.setContextParameter(ic);
		
		List<RecOidList> roL = new ArrayList<RecOidList>();
		RecOidList ro = null;
		 ro = new RecOidList();
		ro.setRecoid("88104146327527");
		ro.setIsOperId("1");
		roL.add(ro);
		 ro = new RecOidList();
		ro.setRecoid("88104146327528");
		ro.setIsOperId("1");
		roL.add(ro);
		ITransMarketBatchService service = new TransMarketBatchServiceClientImpl();
		
		//中断
		DEL100006Result re = service.transMarketBatchStop("13861590233", "3", roL, "test", "1","1");
		
		
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" === re.getResultCode() === " + re.getResultCode());
		}else{
			System.out.println(" ====== re 为null");
		}
	}
}
