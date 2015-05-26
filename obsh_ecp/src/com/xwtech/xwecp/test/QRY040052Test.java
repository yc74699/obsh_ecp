package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetMultiNumsService;
import com.xwtech.xwecp.service.logic.client_impl.common.LIException;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetMultiNumsServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MultiNumInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040052Result;

public class QRY040052Test {

	private static final Logger logger = Logger.getLogger(QRY040052Test.class);
	public static void main(String[] args) throws LIException {
//		初始化ecp客户端片段
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
		lic.setUserMobile("13912986834");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18251968869");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		lic.setContextParameter(ic);
		
		IGetMultiNumsService mi = new GetMultiNumsServiceClientImpl();
		QRY040052Result re =mi.getMultiNums("12", "1299400006814606");
		System.out.println("--operFlag---"+re.getOperFlag());
		for (MultiNumInfo m : re.getMultiNumInfo()){
			System.out.println("---proid--------"+m.getProdId());
			System.out.println("---canremote----"+m.getCanRemote());
			System.out.println("---vnum_flag----"+m.getVnumFlag());
			System.out.println("---vnum_sid-----"+m.getVnumSid());
			System.out.println("---vnum_seq-----"+m.getVnumSeq());
			System.out.println("---vnum_city----"+m.getVnumCity());
			System.out.println("---start_date---"+m.getStartDate());
			System.out.println("---end_date-----"+m.getEndDate());
			System.out.println("---------------------------");
		}
		

	}

}
