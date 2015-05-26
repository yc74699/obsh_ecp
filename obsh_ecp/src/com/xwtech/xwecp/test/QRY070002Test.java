package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryYxfaMxService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryYxfaMxServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.YxfaMx;
import com.xwtech.xwecp.service.logic.pojo.QRY070002Result;

/**
 * 档次查询
 * 2014 - 09 -24
 */
public class QRY070002Test {
	private static final Logger logger = Logger.getLogger(QRY070002Test.class);
	
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
		lic.setUserBrand("");
		lic.setUserCity("14");
		lic.setUserMobile("13601400067");
		InvocationContext ic = new InvocationContext(); 
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12"); 
		ic.addContextParameter("ddr_city", "12"); 
		
		lic.setContextParameter(ic);
		 
		IQueryYxfaMxService co = new QueryYxfaMxServiceClientImpl();
		// 入参 ：地市，档次编码
		QRY070002Result re = co.queryYxfaMx("12", "300001288031");
		logger.info(" ====== 开始返回参数 ======================= ");
		if (re != null)
		{
			logger.info(" ====== getResultCode ============ " + re.getResultCode());
			logger.info(" ====== getErrorCode ============= " + re.getErrorCode());
			logger.info(" ====== getErrorMessage ========== " + re.getErrorMessage());
			
			if (null != re.getYxfaMx() && re.getYxfaMx().size() > 0)
			{ 
				System.out.println("地市：" + re.getDdr_city());
				System.out.println("===========================================");
				for (YxfaMx g : re.getYxfaMx())
				{
					System.out.println("档次名称：" + g.getMarketingplanpriv_name());  
					System.out.println("活动描述：" + g.getMarketingplan_type_id());
					System.out.println("活动大类：" + g.getMarketingplantype_class()); 
					System.out.println("协议期：" + g.getMarketingplan_validity()); 
					System.out.println("首月到账：" + g.getMarketingplan_firstmonth()); 
					System.out.println("分月释放：" + g.getMarketingplan_permonth()); 
					System.out.println("月最低消费：" + g.getMarketingplan_minimum()); 
					System.out.println("金额：" + g.getMarketingplan_cash_amount()); 
					System.out.println("开始时间：" + g.getMarketingplan_end_date());  
					System.out.println("结束时间：" + g.getMarketingplan_start_date());
					System.out.println("营销方案编码：" + g.getMarketingplan_type_id());   
					System.out.println("营销方案名字：" + g.getMarketingplantype_name()); 
					System.out.println("二级boss编码：" + g.getMarketingplan_plan_id()); 
					System.out.println("二级boss名称：" + g.getMarketingplanpriv_name()); 
					System.out.println("月最低消费：" + g.getMarketingplan_minimum()); 
					System.out.println("===========================================");
				}
			}
		}

	}
}
