package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IMultiNumOperateService;
import com.xwtech.xwecp.service.logic.client_impl.common.LIException;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.MultiNumOperateServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL040055Result;
import com.xwtech.xwecp.service.logic.pojo.MultiNumOperateInfo;

public class DEL040055Test {

	private static final Logger logger = Logger.getLogger(DEL040055Test.class);

	public static void main(String[] args) throws LIException {
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
		lic.setUserMobile("13912986834");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18251968869");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");

		lic.setContextParameter(ic);
		
		IMultiNumOperateService is = new MultiNumOperateServiceClientImpl();
		
		//EffectType：开通时有效  取消时可以不传
		//立即：Type_Immediate
		//次月：Type_NextCycle
		
		/*MultiNumOperateInfo  multinuminfo = new MultiNumOperateInfo();
		 multinuminfo.setProdId("2400000022");
		 multinuminfo.setVnumSid("15051379902");
		 multinuminfo.setVnumSeq("1");
		 multinuminfo.setVnumCity("12");
		 multinuminfo.setOperType("PCOpRec");
		 multinuminfo.setEffectType("Type_Immediate");*/
		
		MultiNumOperateInfo  multinuminfo = new MultiNumOperateInfo();
		 multinuminfo.setProdId("2400000022");
		 multinuminfo.setVnumSid("15051379902");
		 multinuminfo.setVnumSeq("1");
		 multinuminfo.setVnumCity("12");
		 multinuminfo.setOperType("PCOpDel");
		 
		DEL040055Result re =is.multiNumOperate("12", "1299400006814606", multinuminfo);
		if("0".equals(re.getResultCode())){
			System.out.println("----ResultCode---------"+re.getResultCode());
			
		}else{
			System.out.println("----ResultCode---------"+re.getResultCode());
			System.out.println("----ErrorMessage-------"+re.getErrorMessage());
		}
		
	}

}
