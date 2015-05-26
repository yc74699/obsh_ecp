package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IProvChainService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.ProvChainServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040115Result;
import com.xwtech.xwecp.service.logic.pojo.ProvchainOperDetail;

/**
 * 终端串号信息同步
 * @author wang.h
 *
 */
public class DEL040115Test {
	private static final Logger logger = Logger.getLogger(DEL040115Test.class);
	
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
		lic.setUserCity("17");
		lic.setUserMobile("13921078544");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13921078544");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "17");
		ic.addContextParameter("ddr_city", "17");
		lic.setContextParameter(ic);
		
		IProvChainService ics = new ProvChainServiceClientImpl();
		String operSrl = "20090507111100101252";
		String opType = "1";
		String orgId = "12100000";
		String brandId ="0";
		String resTypeId = "rsclM.1016.101610000029";
		String vendorId = "203060";
		List<ProvchainOperDetail> operList = new ArrayList<ProvchainOperDetail>();
		ProvchainOperDetail pd1 = new ProvchainOperDetail();
		pd1.setImei("353178034756802");
		ProvchainOperDetail pd2 = new ProvchainOperDetail();
		pd2.setImei("353178034756819");
		ProvchainOperDetail pd3 = new ProvchainOperDetail();
		pd3.setImei("353178034756805");
		ProvchainOperDetail pd4 = new ProvchainOperDetail();
		pd4.setImei("353178034756801");
		
		operList.add(pd1);
		operList.add(pd2);
		operList.add(pd3);
		operList.add(pd4);

		DEL040115Result re = ics.synchronousPhoneNum(operSrl, opType, orgId, brandId, resTypeId, vendorId, operList);
		
		if (re != null)
		{
			System.out.println(" === re.getResultCode() === " + re.getResultCode());
			System.out.println(" === re.getErrorCode() === " + re.getErrorCode());
			System.out.println(" === re.getErrorMessage() === " + re.getErrorMessage());
			System.out.println(" === re.getRespCode ===" + re.getRespCode());
			System.out.println(" === re.getRespDesc ===" + re.getRespDesc());
			System.out.println(" === re.getRespType ===" + re.getRespType());
		}
	}
}
