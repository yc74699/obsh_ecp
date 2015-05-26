package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryYxfaYwbService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryYxfaYwbServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY070003Result;
import com.xwtech.xwecp.service.logic.pojo.YxfaYwbInfo;

/**
 * 业务包查询
 * 2014-09-25
 */
public class QRY070003Test {
	private static final Logger logger = Logger.getLogger(QRY070003Test.class);
	
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
		lic.setUserMobile("13775232424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13775232424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12"); 
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		 
		IQueryYxfaYwbService co = new QueryYxfaYwbServiceClientImpl();
		// 入参：地市， 档次编码，营销方案编码,是否校验
		QRY070003Result re = co.queryYxfaYwb("12", "180100008935","2518102086","1");
		 
		if(null != re)
		{
			for(YxfaYwbInfo yy : re.getYxfaYwbInfo()){
				System.out.println("奖品包编码：" + yy.getMarketingbusipackcfg_busi_pack_id());
				System.out.println("奖品包名称：" + yy.getMarketingbusipackcfg_busi_pack_name());
				System.out.println("奖品包的最小数量限制：" + yy.getMarketingbusipackcfg_mincount());
				System.out.println("奖品包的最大数量限制：" + yy.getMarketingbusipackcfg_maxcount());
				System.out.println("业务包名称：" + yy.getMarketingbusipackcfg_operating_remark());
				System.out.println("档次编码：" + yy.getMarketingbusipackcfg_plan_id());
				System.out.println("奖品ID：" + yy.getMarketingbusipackcfg_rwd_id());
				System.out.println("奖品名称：" + yy.getMarketingbusipackcfg_rwd_name());
				System.out.println("选择类型：" + yy.getMarketingbusipackcfg_rwd_selecttype());
				System.out.println("状态：" + yy.getMarketingbusipackcfg_state());
				System.out.println("选择类型：" + yy.getSelectType());
			}
		}
	}
}
