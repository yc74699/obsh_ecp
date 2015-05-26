package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ITpSimOpenPrdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.TpSimOpenPrdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL110004Result;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.ProService;
import com.xwtech.xwecp.service.logic.pojo.SimMarketInfo;

public class DEL110004Test
{
	private static final Logger logger = Logger.getLogger(DEL011002Test.class);
	
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

		lic.setUserCity("14");
		lic.setUserMobile("15250893979");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15250893979");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		
//		ic.addContextParameter("user_id", "1738200005062065");  //2056200011182291

		List<ProPackage> proPackages = new ArrayList<ProPackage>();
		for (int i = 0;i<3;i++){
			ProPackage pckBean = new ProPackage();
			if(i == 0) {pckBean.setPkgId("1591");pckBean.setTypeId("1013");}
			if(i == 1) {pckBean.setPkgId("1265");pckBean.setTypeId("1013");}
			if(i == 2) {pckBean.setPkgId("4788");pckBean.setTypeId("1039");}
			pckBean.setPkgLevel("3");
			proPackages.add(pckBean);
		}
		//组装附加功能的list
		List<ProService> proServices = new ArrayList<ProService>();
		for (int i = 0;i<7;i++){
			ProService serviceBean = new ProService();
			if(i == 0) {serviceBean.setServiceId("2");}
			if(i == 1) {serviceBean.setServiceId("3");}
			if(i == 2) {serviceBean.setServiceId("4");}
			if(i == 3) {serviceBean.setServiceId("9");}
			if(i == 4) {serviceBean.setServiceId("10");}
			if(i == 5) {serviceBean.setServiceId("13");}
			if(i == 6) {serviceBean.setServiceId("15");}
			proServices.add(serviceBean);
		}
		
		//组装增值业务的list
		List<ProIncrement> proIncrements = new ArrayList<ProIncrement>();
		for (int i = 0;i<1;i++){
			ProIncrement incBean = new ProIncrement();
			if(i == 0) {incBean.setIncrementId("5006");}
			
			proIncrements.add(incBean);
		}
		
		//组装自有业务的list
		List<ProSelf> proSelfs = new ArrayList<ProSelf>();
		lic.setContextParameter(ic);
		//组装自有业务的list
		List<SimMarketInfo> marketInfo = new ArrayList<SimMarketInfo>();
		for (int i = 0;i<1;i++){
			SimMarketInfo b = new SimMarketInfo();
			if(i == 0) {
				b.setMarket_busi_pack_id("");
				b.setMarket_fee("");
				b.setMarket_goods_pack_id("");
				b.setWebcustinfo_market_id("");
				b.setWebcustinfo_market_levelid("");
			}
			
			marketInfo.add(b);
		}
		
		
		ITpSimOpenPrdService co = new TpSimOpenPrdServiceClientImpl();
		DEL110004Result re = co.doTpSimopenPrd("15250893979",
				"15250893979", "testName", "客户地址", "HADQ", "HASQ",
				"321084198307140016", "江苏南京江宁区xxx路xx号xx室", "5000", "100063", "DGDD_DGDD2", "1",proPackages, proServices, proIncrements, proSelfs,marketInfo);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
			logger.info(" === re.getBookId() === " + re.getBookId());
		}
	}
}
