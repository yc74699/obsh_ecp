package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGprsFluxService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGprsFluxServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.GprsPkg;
import com.xwtech.xwecp.service.logic.pojo.QRY040057Result;

public class QRY040057Test {	

	private static final Logger logger = Logger.getLogger(QRY040057Test.class);

	public static void main(String[] args) throws LIException {

		// 初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.166:10004/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		// 逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13815890413");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13815890413");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419300019772126");
		lic.setContextParameter(ic);

		IQueryGprsFluxService is = new QueryGprsFluxServiceClientImpl();
		try {
			QRY040057Result re = is.queryGprsFlux("1419300019772126", "201307", "3");
			System.out.println("getTotalFlux   ======   "+re.getTotalFlux());
			System.out.println("getUsedFlux    ======   "+re.getUsedFlux());
			System.out.println("getUsedFlux    ======   "+re.getPkgType());
			List<GprsPkg> gprsPkgList = re.getGprsPkgList();
			for(GprsPkg g : gprsPkgList)
			{
				System.out.println("getNum              ======   "+g.getNum());
				System.out.println("getId               ======   "+g.getId());
				System.out.println("getName             ======   "+g.getName());
				System.out.println("getProductType      ======   "+g.getProductType());
				System.out.println("getRateType         ======   "+g.getRateType());
				System.out.println("getUsedFlux         ======   "+g.getUsedFlux());
				System.out.println("getTotalFlux        ======   "+g.getTotalFlux());
				System.out.println("getUseType          ======   "+g.getUseType());
			}
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}