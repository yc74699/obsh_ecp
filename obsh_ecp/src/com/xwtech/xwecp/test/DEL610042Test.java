package com.xwtech.xwecp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IFamilyProdChgService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.FamilyProdChgServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL610042Result;
import com.xwtech.xwecp.service.logic.pojo.MemberinfoIn;
/**
 * 2.家庭V网套餐变更
 * @author Administrator
 *
 */
public class DEL610042Test
{
	private static final Logger logger = Logger.getLogger(DEL610042Test.class);
	
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
		lic.setUserCity("13");
		lic.setUserMobile("15005232099");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15005232099");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13");
		ic.addContextParameter("ddr_city", "13");
		//ic.addContextParameter("user_id", "1208200008843825");
		
		lic.setContextParameter(ic);
		
		IFamilyProdChgService famChg = new FamilyProdChgServiceClientImpl();
		List<MemberinfoIn> memberInfoIn = new ArrayList<MemberinfoIn>();
		//副号成员号码 ,副号成员用户编号,副号成员归属地,成员短号,操作类型(ADD-增加成员 ,CHG-修改成员短号属性,DEL-删除成员,SMSADD-主号短信邀请成员加入)
		memberInfoIn.add(new MemberinfoIn("13770487703","12102000007383702","12","769","ADD"));
		//主号用户编号,增值包编码,产品生效方式,增值产品编码生效方式,增值产品编码失效方式,新增副号成员列表
		DEL610042Result re =  famChg.familyProdChg("1208200011660016", "2012000007","1" ,"2000003346,1", "2000002387,1|2000003574,1|2000003715,1", memberInfoIn);
		if(re != null)
		{
			logger.info(re.getResultCode());
		}
	}
}
