package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQrygprspkgfluxService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QrygprspkgfluxServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040089Result;

/**
 * GPRS套餐流量查询
 * @author YangXQ
 * 2014-11-04
 */
public class QRY040089Test
{
	private static final Logger logger = Logger.getLogger(QRY040089Test.class);
	
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
		lic.setUserCity("12");
		lic.setUserMobile("");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13401807124");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "");	
		lic.setContextParameter(ic);
 
		IQrygprspkgfluxService co = new QrygprspkgfluxServiceClientImpl();
		// 入参 ：  用户号  查询帐期   业务类型   接口类型 
		QRY040089Result re = co.queryOrderStatus("1299400007822007","201410","2","1"); 
		System.out.println(" ================================================");
		if (re != null)
		{
			System.out.println(" ====== ret_code  ======" + re.getRet_code());
			System.out.println(" ====== Ret_msg ======" + re.getRet_msg()); 
			System.out.println(" ====== 总流量  ======" + re.getGprs_total_flux());
			System.out.println(" ====== 已经使用流量 ======" + re.getGprs_used_flux()); 
			System.out.println(" ====== 使用套餐外流量的标志  ======" + re.getOver_pkg_flag());
			System.out.println(" ====== 套餐外流量======" + re.getOut_pkg_flus()); 
			System.out.println(" ====== 用户全包套餐标识 ======" + re.getGprs_pkg_type()); 
		}
	}
}
