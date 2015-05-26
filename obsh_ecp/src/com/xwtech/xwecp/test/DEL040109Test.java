package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICustomDataSubmitSelfService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CustomDataSubmitSelfServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040109Result;
import com.xwtech.xwecp.service.logic.pojo.ProIncrement;
import com.xwtech.xwecp.service.logic.pojo.ProPackage;
import com.xwtech.xwecp.service.logic.pojo.ProSelf;
import com.xwtech.xwecp.service.logic.pojo.ProService;

public class DEL040109Test {
private static final Logger logger = Logger.getLogger(DEL040109Test.class);
	
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
		lic.setUserMobile("13611504135");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13611504135");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("context_loginiplock_login_ip", "127.0.0.1");
		
		ic.addContextParameter("user_id", "1738200005062065");  //2056200011182291

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
		
		ICustomDataSubmitSelfService co = new CustomDataSubmitSelfServiceClientImpl();
		
		DEL040109Result re = co.customDataSubmitSelf("18762022754",
				"往往", "HADQ", "HA", "4356234657456234456", "江苏省南京市白下区中山南路1号",
				"211600", "白下区中山南路1号", "18762022754", "", "江苏移动通信有限责任公司淮安分公司(12100000)",
				"江苏移动通信有限责任公司淮安分公司", "3", "10000", "", "1000100157", 
				"DGDD_DGDD2", "1",proPackages, proServices, proIncrements, proSelfs,
				"3000866021","300001282032","","","399","","","460027620215645","12");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
			logger.info(" === re.getWebcustinfo_web_booking_id() === " + re.getWebcustinfo_web_booking_id());
		}
	}
}
