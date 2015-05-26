package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IVirtualProdInstallMemChkService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.VirtualProdInstallMemChkServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL610048Result;
import com.xwtech.xwecp.service.logic.pojo.MemberinfoIn;

/**
 * 10.家庭V网订购时校验副号
 * @author Administrator
 *
 */
public class DEL610048Test
{
private static final Logger logger = Logger.getLogger(DEL610047Test.class);
	
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
		lic.setUserBrand("12");
		lic.setUserCity("12");
		lic.setUserMobile("13401801004");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13401801004");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		//ic.addContextParameter("user_id", "1208200008843825");
		
		lic.setContextParameter(ic);
		/*
		 * 13401801004   1299201501346165 主号
			13401802574   1299201501346168副号
		 */
		List<MemberinfoIn> memberInfoIn = new ArrayList<MemberinfoIn>();
		//副号成员号码,副号成员用户编号,副号成员归属地,成员短号
		memberInfoIn.add(new MemberinfoIn("13401802574"," 1299201501346168","12","767"));
		IVirtualProdInstallMemChkService chk = new VirtualProdInstallMemChkServiceClientImpl();
		//主号用户编号,增值包编码,增值产品编码,产品生效方式,新增副号成员列表
		DEL610048Result re = chk.virtualProdInstallMemChk("1299201501346165", "2012000007", "2000003715,1", "1", memberInfoIn);
		if(re != null)
		{
			logger.error("===========错误编号============" + re.getErrorCode());
			logger.error("===========错误信息============" + re.getErrorMessage());
		}
	}
}
