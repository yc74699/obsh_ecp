package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryVirtualFamilyService;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryVirtualProdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryVirtualFamilyServiceClientImpl;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryVirtualProdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.Member_Info;
import com.xwtech.xwecp.service.logic.pojo.Product_Info;
import com.xwtech.xwecp.service.logic.pojo.QRY610001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY610003Result;

/**
 * 用户查询归属V网信息
 * @author Administrator
 *
 */
public class QRY610001Test
{
private static final Logger logger = Logger.getLogger(QRY610001Test.class);
	
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
		lic.setUserMobile("15851728596");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15851728596");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13");
		ic.addContextParameter("ddr_city", "13");
		//ic.addContextParameter("user_id", "1208200008843825");
		
		lic.setContextParameter(ic);
		IQryVirtualFamilyService famQryVirtual = new QryVirtualFamilyServiceClientImpl();
		
	/*	String pkgId = null;
		IQryVirtualProdService famQryPord = new QryVirtualProdServiceClientImpl();
		QRY610003Result re = famQryPord.qryVirtualProd("1210200007383736");
		List<Product_Info> productInfo = re.getProductInfo();
		for(Product_Info p:productInfo)
		{
			 pkgId = p.getPkgprodid();
		}*/
		
		//用户编号,增值产品包编码
		QRY610001Result re1 = famQryVirtual.qryVirtualFamily("1315300000183297", "2012000007");
		if(re1 != null)
		{
			List<Member_Info> memberInfo = re1.getMemberInfo();
			for(Member_Info m:memberInfo)
			{
				System.out.println("===== 主号用户编号 =====" + m.getFamilysubsid());
				System.out.println("===== 家庭归属地 =====" + m.getRegion());
				System.out.println("===== 成员用户编号 ===== " + m.getMemsubsid());
				System.out.println("===== 成员号码  =====" + m.getServnumber());
				System.out.println("===== 是否主号  =====" + m.getIsprima());
				System.out.println("===== 成员归属地  ===== " + m.getMemregion());
				System.out.println("===== 短号号码 ===== " + m.getShortnum());
				System.out.println("===== 成员生效时间  =====" + m.getStartdate());
				System.out.println("===== 成员失效时间  =====" + m.getEnddate());
				System.out.println("===== 成员类型名称  ===== " + m.getMemtype());
			}
		}
	}
}
