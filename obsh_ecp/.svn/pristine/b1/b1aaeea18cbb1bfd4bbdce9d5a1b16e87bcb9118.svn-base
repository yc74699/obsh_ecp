package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryYxfaList1Service;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryYxfaList1ServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY070001Result;
import com.xwtech.xwecp.service.logic.pojo.YxfaInfo;
import com.xwtech.xwecp.service.logic.pojo.YxfaYwbInfo;

/**
 * 查询营销方案列表 
 * 2014 - 09 -24
 */
public class QRY070001Test {
	private static final Logger logger = Logger.getLogger(QRY070001Test.class);
	
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
		lic.setUserMobile("13913032424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913032424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("request_seq", "1_1");
		ic.addContextParameter("request_time", "20090728024911");
		ic.addContextParameter("ddr_city", "12");	
		ic.addContextParameter("user_id", "2056200011182291");	
		lic.setContextParameter(ic);
		 
		IQueryYxfaList1Service co = new QueryYxfaList1ServiceClientImpl();	
		// 入参：地市
		QRY070001Result re = co.queryYxfaList1("12");
		List<YxfaInfo> retList = re.getYxfaInfo();
		System.out.println("记录数：" + retList.size());
		logger.info(" ====== 开始返回参数 ======");	
		if(null != re)
		{
			System.out.println("地市：" + re.getDdr_city());
			System.out.println("================================================");
			for(YxfaInfo yy : re.getYxfaInfo()){
				System.out.println("一级boss编码：" + yy.getMarketingplan_type_id());  
				System.out.println("一级boss名称：" + yy.getMarketingplantype_name());
				System.out.println("活动说明：" + yy.getMarketingplantype_desc());
				System.out.println("开始时间：" + yy.getMarketingplan_start_date());
				System.out.println("结束时间：" + yy.getMarketingplan_end_date());
				System.out.println("营销案类型 ：" + yy.getMarketingplantype_class());
				System.out.println("品牌 ：" + yy.getMarketingplan_permit_brand_ids());
				System.out.println("二级id ：" + yy.getMarketingplan_plan_id());
				System.out.println("二级方案名称 ：" + yy.getMarketingplan_name()); 
				System.out.println(".....");
				System.out.println("================================================");
			}

		}
	}
}
