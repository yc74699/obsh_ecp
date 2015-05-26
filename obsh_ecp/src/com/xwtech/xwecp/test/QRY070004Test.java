package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryYxfaLpbService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryYxfaLpbServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY070004Result;
import com.xwtech.xwecp.service.logic.pojo.YxfaLpbInfo;

/**
 * 奖品包查询
 * 2014 - 09 -24
 */
public class QRY070004Test {
	private static final Logger logger = Logger.getLogger(QRY070004Test.class);
	
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
		 
		IQueryYxfaLpbService co = new QueryYxfaLpbServiceClientImpl();
		
		//入参：地市， 档次编码，营销方案编码,是否校验
		QRY070004Result re = co.queryYxfaLpb("12", "180100008935","2518102086","0");
		 
		if(null != re)
		{   System.out.println("================================================");
			System.out.println("Ret_code：" + re.getRet_code());
			System.out.println("Ret_msg：" + re.getRet_msg());
			System.out.println("Mainprodid：" + re.getMainprodid());
			System.out.println("getMindrawcount：" + re.getMaxdrawcount());
			System.out.println("Mindrawcount：" + re.getMindrawcount());
			System.out.println("Limit_brand：" + re.getLimit_brand());
			System.out.println("================================================");
			for(YxfaLpbInfo yy : re.getYxfaLpbInfo()){	
				System.out.println("业务包：" + yy.getPresentgoodspackcfg_pack_id());
				System.out.println("业务包名称：" + yy.getPresentgoodspackcfg_pack_name());
				System.out.println("业务包名称：" + yy.getSelectType());
				System.out.println("奖品包的最大数量限制 ：" + yy.getPresentgoodspackcfg_maxcount());
				System.out.println("奖品包的最小数量限制 ：" + yy.getPresentgoodspackcfg_mincount());
				System.out.println("档次编码 ：" + yy.getPresentgoodspackcfg_plan_id());
				System.out.println("奖品ID ：" + yy.getPresentgoodspackcfg_rwd_id());
				System.out.println("奖品名称 ：" + yy.getPresentgoodspackcfg_rwd_name());
				System.out.println("选择类型 ：" + yy.getPresentgoodspackcfg_rwd_selecttype());
				System.out.println("状态 ：" + yy.getPresentgoodspackcfg_state());
				System.out.println("================================================");
			}

		}
	}
}
