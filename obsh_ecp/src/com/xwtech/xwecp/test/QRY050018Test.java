package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryNewSaleOfficeInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryNewSaleOfficeInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050018Result;
import com.xwtech.xwecp.service.logic.pojo.ZxrwSiteOfficeInfo;

public class QRY050018Test
{
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
		lic.setUserMobile("13651542424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13651542424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "");
		lic.setContextParameter(ic);
		
		try{
			IQueryNewSaleOfficeInfoService co = new QueryNewSaleOfficeInfoServiceClientImpl();
			String city = "NJDQ";
			String county = "NJ";
			QRY050018Result re = co.queryNewSaleOfficeInfo(city, county);
			List<ZxrwSiteOfficeInfo> siteOfficeInfos = re.getZxrwSiteOfficeInfo();
			for (ZxrwSiteOfficeInfo siteOfficeInfo : siteOfficeInfos) {
				System.out.println("营业厅名称：" + siteOfficeInfo.getSiteName());
				System.out.println("营业厅地址:" + siteOfficeInfo.getSiteAddress());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
