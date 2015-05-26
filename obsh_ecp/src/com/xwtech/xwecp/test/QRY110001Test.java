package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryRewardListByAcctIdClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryRewardListByAcctId;
import com.xwtech.xwecp.service.logic.pojo.QRY110001Result;
import com.xwtech.xwecp.service.logic.pojo.MarketPlanInfo;

/**
 * 判断是否是国内亲情号码测试类
 * 
 * @author yangli
 * 
 */
public class QRY110001Test {
	private static final Logger logger = Logger.getLogger(QRY110001Test.class);

	public static void main(String[] args) throws Exception {
		// 初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		// 逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("13585198722");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13585198722");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("request_seq", "0_111");
		ic.addContextParameter("request_time", "12110804022825");
		ic.addContextParameter("ddr_city", "12");

		ic.addContextParameter("user_id", "1208200000060545");

		lic.setContextParameter(ic);

		IQryRewardListByAcctId co = new QryRewardListByAcctIdClientImpl();
		QRY110001Result re = co.qryRewardListByAcctId("");

		if (re != null) {
			System.out.println("BOSS返回码：" + re.getBossCode());
			System.out.println("BOSS返回码：" + re.getMarketPlanInfos().size());
			for (MarketPlanInfo mpInfo : re.getMarketPlanInfos()) {
				System.out.println("---------------------------------");
				System.out.println("活动编码:" + mpInfo.getActiveId());
				System.out.println("活动名称:" + mpInfo.getActiveName());
				System.out.println("档次编码:" + mpInfo.getLevelId());
				System.out.println("档次名称:" + mpInfo.getLevelName());
				System.out.println("组织单位:" + mpInfo.getOrg());
				System.out.println("可审请品牌;" + mpInfo.getBrand());
				System.out.println("方案起用时间;" + mpInfo.getStartDate());
				System.out.println("方案结束时间;" + mpInfo.getEndDate());
				System.out.println("---------------------------------");
			}

		}

	}

}
