package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IVirtualMemService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.VirtualMemServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL610045Result;
import com.xwtech.xwecp.service.logic.pojo.MemberinfoIn;

/**
 * V网成员维护
 * @author Administrator
 *
 */
public class DEL610045Test
{
	private static final Logger logger = Logger.getLogger(DEL610045Test.class);
	
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
		lic.setUserMobile("15005232099");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15005232099");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		//ic.addContextParameter("user_id", "1208200008843825");
		
		lic.setContextParameter(ic);
		
		List<MemberinfoIn> memberInfoIn = new ArrayList<MemberinfoIn>();
		MemberinfoIn infoIn = new MemberinfoIn();
		infoIn.setOprttype("DEL");//ADD-增加成员 ,CHG-修改成员短号属性,DEL-删除成员,SMSADD-主号短信邀请成员加入
		infoIn.setMemsubsid("12102000007383702");//副号用户编号
		infoIn.setServnumber("15005232099");//副号号码
		infoIn.setMemregion("12");//副号归属地
		infoIn.setShortnum("");//成员短号
		memberInfoIn.add(infoIn);
		//主号用户编号,操作来源
		IVirtualMemService famVirtual = new VirtualMemServiceClientImpl();
		DEL610045Result re = famVirtual.virtualMem("1208200011660016", "1", memberInfoIn);
		if(re != null)
		{
			logger.error("===========错误编号============" + re.getErrorCode());
			logger.error("===========错误信息============" + re.getErrorMessage());
		}
	}
}
