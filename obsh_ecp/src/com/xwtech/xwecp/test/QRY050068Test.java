package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryCountryNetworkInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryCountryNetworkInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.CountryNetworkInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY050068Result;

public class QRY050068Test {

	private static final Logger logger = Logger.getLogger(QRY050067Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        //初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12"); 
		lic.setUserMobile("13625151208");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13625151208");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
		ic.addContextParameter("user_id", "1210200007634256");//2056200011182291
		
		lic.setContextParameter(ic);
		
		IQueryCountryNetworkInfoService icni = new QueryCountryNetworkInfoServiceClientImpl();
		QRY050068Result res = new QRY050068Result();
		try {
//			res = icni.queryCouNetInfo("13", "1317","王");
			res = icni.queryCouNetInfo("1208022034364035");
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List resList = res.getCouNetInfoList();
		for(int i = 0;i<resList.size(); i++)
		{
			CountryNetworkInfo cni = (CountryNetworkInfo)resList.get(i);
			System.out.println("getBusiName =================== "+cni.getBusiName());
			System.out.println("getCountryId =================== "+cni.getCountryId());
			System.out.println("getDictName =================== "+cni.getDictName());
			System.out.println("getGroupName =================== "+cni.getGroupName());
			System.out.println("getRegion =================== "+cni.getRegion());
			System.out.println("getSubsId =================== "+cni.getSubsId());
			System.out.println("getCustId =================== "+cni.getCustId());
		}
	}
}