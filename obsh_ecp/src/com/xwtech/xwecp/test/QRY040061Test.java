package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IPrivilegeLeftCrossService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.PrivilegeLeftCrossServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.LogLeft;
import com.xwtech.xwecp.service.logic.pojo.QRY040061Result;

/**
 * 
 * 增加成员
 *
 */
public class QRY040061Test
{
	private static final Logger logger = Logger.getLogger(QRY040061Test.class);
	
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
		lic.setUserCity("11");
		lic.setUserMobile("13511600004");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13511600004");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13511600004");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101300005632214");
		
		lic.setContextParameter(ic);
		
		IPrivilegeLeftCrossService  is = new PrivilegeLeftCrossServiceClientImpl();
		QRY040061Result re = is.qryLeftCross("GPRSLL_60YJB", "88022064903405", "20131102", "20140131");
		if(null != re){
			System.out.println(re.getResultCode()+"--getResultCode");
			System.out.println(re.getProdid()+"--getProdid");
			System.out.println(re.getProd_instance_id()+"--getProd_instance_id");
			for (LogLeft lg :re.getLoglist()){
				System.out.println(lg.getTypeid()+"-getTypeid");
				System.out.println(lg.getAlldata()+"-getAlldata");
				System.out.println(lg.getUsedata()+"-getUsedata");
				System.out.println(lg.getLeftdata()+"-getLeftdata");
				System.out.println(lg.getDataunit()+"-getDataunit");
			}
		}
		
	}
}
