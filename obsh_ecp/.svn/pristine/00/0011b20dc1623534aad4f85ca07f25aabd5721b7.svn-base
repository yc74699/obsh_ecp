package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQuery20YFDTCService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.Query20YFDTCServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040087Result;

/**
 * 20元封顶套餐查询接口
 * @author YangXQ
 * 2014-09-25
 */
public class QRY040087Test
{
	private static final Logger logger = Logger.getLogger(QRY040087Test.class);
	
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
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("15050806834");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15050806834");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "");	
		lic.setContextParameter(ic);
 
		IQuery20YFDTCService co = new Query20YFDTCServiceClientImpl();
		// 入参 ： 号码
		QRY040087Result re = co.query20YFDTC("15050806834");// 0: 表示流量
		System.out.println(" ================================================");
		if (re != null)
		{
			System.out.println(" ====== Ret_code  ======" + re.getRet_code());
			System.out.println(" ====== Ret_msg ======" + re.getRet_msg());
			System.out.println(" ====== 是否20元封顶  ======" + re.getIscaps());
			System.out.println(" ====== 20元封顶套餐生效日期  ======" + re.getInure_date());
			
		}
	}
}
