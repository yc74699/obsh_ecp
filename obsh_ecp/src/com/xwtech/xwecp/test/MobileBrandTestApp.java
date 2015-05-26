package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMobileBrandService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMobileBrandServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MobileBrand;
import com.xwtech.xwecp.service.logic.pojo.QRY050007Result;

public class MobileBrandTestApp {
	
	
	
	/**
	 * 手机ID:30 手机名称:OKWAP英华达
手机ID:36 手机名称:Porsche
手机ID:38 手机名称:Sagem 萨基姆
手机ID:2 手机名称:ASUS华硕
手机ID:6 手机名称:Dopod多普达
手机ID:15 手机名称:HP惠普
手机ID:43 手机名称:Toshiba东芝
手机ID:44 手机名称:VK 威科
手机ID:29 手机名称:O2
手机ID:34 手机名称:Pantech泛泰
手机ID:35 手机名称:Philips飞利浦
手机ID:13 手机名称:G-PLUS
手机ID:14 手机名称:Gradiente
手机ID:16 手机名称:HTC宏达
手机ID:25 手机名称:Motorola摩托罗拉
手机ID:32 手机名称:Palm奔迈
手机ID:17 手机名称:i-mate
手机ID:41 手机名称:Sharp夏普
手机ID:42 手机名称:Sonim
手机ID:20 手机名称:LG乐金
手机ID:3 手机名称:Bellwave贝尔威夫
手机ID:5 手机名称:Dbtel迪比特
手机ID:26 手机名称:MWG
手机ID:23 手机名称:Mitsubishi三菱
手机ID:31 手机名称:Orange
手机ID:4 手机名称:Bird波导
手机ID:21 手机名称:Lobster
手机ID:22 手机名称:Micro
手机ID:1 手机名称:Alcatel阿尔卡特
手机ID:8 手机名称:ELIYA
手机ID:24 手机名称:MobileWatch
手机ID:33 手机名称:Panasonic松下
手机ID:39 手机名称:Samsung三星
手机ID:12 手机名称:GIGABYTE技嘉
手机ID:18 手机名称:i-mobile
手机ID:40 手机名称:Sendo仙都
手机ID:27 手机名称:NEC
手机ID:28 手机名称:Nokia诺基亚
手机ID:37 手机名称:Qool
手机ID:7 手机名称:ELITE
手机ID:9 手机名称:Emblaze
手机ID:10 手机名称:Ericsson/Sony Ericsson索爱
手机ID:11 手机名称:Fly
手机ID:19 手机名称:Latte
手机ID:45 手机名称:华为
手机ID:46 手机名称:明基BenQ/西门子
	 * @param args
	 */
   
	public static void main(String[] args)
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1:80/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("用户县市");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("contextParameter", "12345");
		ic.addContextParameter("testParameter", 54321);
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913032424");
		ic.addContextParameter("ddr_city", "20");
		
		ic.addContextParameter("user_id", "2056200011182291");
		
		lic.setContextParameter(ic);
		
		IQueryMobileBrandService service = new QueryMobileBrandServiceClientImpl();
		try {
			QRY050007Result qRY050007Result = service.queryMobileBrand();
			
			List<MobileBrand> mobileBrand = qRY050007Result.getMobileBrand();
			
			for(int i = 0 ; i < mobileBrand.size() ; i ++)
			{
				System.out.println("手机ID:" + mobileBrand.get(i).getBrandId() + " 手机名称:" + mobileBrand.get(i).getBrandName());
			}
			
		} catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
