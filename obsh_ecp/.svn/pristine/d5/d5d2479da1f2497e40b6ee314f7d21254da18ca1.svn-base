package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;

import com.xwtech.xwecp.service.logic.client_impl.common.IQueryMemberInfoService;

import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryMemberInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.FamilyMember;
import com.xwtech.xwecp.service.logic.pojo.MemberInfo;

import com.xwtech.xwecp.service.logic.pojo.QRY060072Result;

public class QRY060072Test {
	private static final Logger logger = Logger.getLogger(QRY060072Test.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.229.74:8080/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13913814503");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13913814503");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13913814503");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1423200000471569");
		
		lic.setContextParameter(ic);
		
		IQueryMemberInfoService co = new QueryMemberInfoServiceClientImpl();

		try {
			QRY060072Result re = co.queryMemberInfos("13913814503");
			if (re != null)
			{
				logger.info(" ====== getResultCode ======" + re.getResultCode());
				
				if (null != re.getMemberInfo())
				{
				
					for (MemberInfo info : re.getMemberInfo()) {
						System.out.println(" ====== info.getCode ======" + info.getSubsId());
						System.out.println(" ====== info.getCode ======" + info.getGrpsubsId());
						System.out.println(" ====== info.getCode ======" + info.getTelNum());
						System.out.println(" ====== info.getCode ======" + info.getMemName());
						System.out.println(" ====== ============================== ======");
					}
		
				}
			}
		} 
		catch (LIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
