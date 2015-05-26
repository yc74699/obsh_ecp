package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IB2bCancelService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.B2bCancelServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.B2B007Result;
import com.xwtech.xwecp.service.logic.pojo.DistributeInfo;
import com.xwtech.xwecp.service.logic.pojo.IMEIInfo;

public class B2B007Test
{
	private static final Logger logger = Logger.getLogger(B2B007Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "terminal_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "tyl");
		props.put("platform.password", "tyl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("CZJF_HKCZ");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("20");
//		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
//		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "20");
		ic.addContextParameter("ddr_city", "20");///13611542424//1101300022887974
//		ic.addContextParameter("user_id", "1419200008195116");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		//QQTMS_3Y
		DistributeInfo info = new DistributeInfo();
		info.setRegion("12");
		info.setOperId("99090028");
		info.setSrcRegion("99");
		info.setSupplerId("99100000");
		info.setSrcorgId("99100000");
		info.setSrcstoreId("99100000");
		info.setDesRegion("12");
		info.setDesStoreId("20124132");
		info.setResTypeId("rsclM.6347.634700000004");
		info.setDesOrgId("20124132");
		
		List<IMEIInfo> imeiList = new ArrayList<IMEIInfo>();
		IMEIInfo ime = new IMEIInfo();
		ime.setImeiId("201305162000007");
		imeiList.add(ime);
		info.setImeiList(imeiList);
		
		IB2bCancelService co = new B2bCancelServiceClientImpl();
		
		B2B007Result re = co.b2bCancel(info,"130517032600014559349317");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
		}
	}
}