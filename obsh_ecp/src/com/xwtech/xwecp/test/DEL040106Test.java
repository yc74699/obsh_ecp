package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.ICreateOrderInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.CreateOderInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040106Result;

public class DEL040106Test {
	private static final Logger logger = Logger.getLogger(DEL040106Test.class);
	public static void main(String[] args) throws Exception
	{
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
		lic.setUserMobile("13515248887");
		lic.setUserMobile("13515248887");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13515248887");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		lic.setContextParameter(ic);
		
		/**
		 * <servnumber>13515248887</servnumber>
           <paytype>1</paytype>
           <payfee>10</payfee>
           <busitype>2</busitype>
           <operid></operid>
           <marketplanid>300001282032</marketplanid>
           <goodspackid></goodspackid>
           <busipackid></busipackid>
           <drawflag>0</drawflag>
           <scoreclass></scoreclass>
           <smsflag>1</smsflag>
           <rewardlist>88009923949886</rewardlist>
           <remark></remark>
           <reserved></reserved>
		 */
		String phoneNum = "13515248887";
		int paytype =1;
		String payfee ="10";
		int busitype = 2;
		String operid ="sysadmin";
		String marketplanid = "300001282032";
		String goodspackid = null;
		String busipackid =null;
		String drawflag = "0";
		String scoreclass = null;
		String smsflag = "1";
		String rewardlist = "88009923949886";
		String remark = null;
		
		
		ICreateOrderInfoService iCreateOderInfoServices = new CreateOderInfoServiceClientImpl();
		DEL040106Result re = iCreateOderInfoServices.createOrderInfo(phoneNum, paytype, payfee, busitype, operid, marketplanid, goodspackid, busipackid, drawflag, scoreclass, smsflag, rewardlist, remark);
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== getOrderId ======" + re.getOrderId());
			System.out.println(" ====== getResultCode ======" + re.getResultCode());
			System.out.println(" ====== getErrorMessage ======" + re.getErrorMessage());
		}
	}
}
