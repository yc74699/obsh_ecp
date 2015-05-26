package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQryVirtualProdService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QryVirtualProdServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.Product_Info;
import com.xwtech.xwecp.service.logic.pojo.QRY610003Result;
/**
 * 主号查询V网产品信息
 * @author Administrator
 *
 */
public class QRY610003Test
{
private static final Logger logger = Logger.getLogger(QRY610003Test.class);
	
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
		lic.setUserMobile("15050944609");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15050944609");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "13");
		ic.addContextParameter("ddr_city", "13");
		//ic.addContextParameter("user_id", "1208200008843825");
		
		lic.setContextParameter(ic);
		IQryVirtualProdService famQryPord = new QryVirtualProdServiceClientImpl();
		//主副号都行
		QRY610003Result re = famQryPord.qryVirtualProd("1315300000183241");
		if(re != null)
		{
			List<Product_Info> productInfo = re.getProductInfo();
			for(Product_Info p:productInfo) 
			{
				System.out.println("===== 主号用户编号 =====" + p.getMainsubsid());
				System.out.println("===== 包 =====" + p.getPkgprodid());
				System.out.println("===== 增值产品编码 ===== " + p.getIncreprodid());
				System.out.println("===== 异地成员是否能享受  =====" + p.getRemoteuse());
				System.out.println("===== 生效时间  =====" + p.getStartdate());
				System.out.println("===== 失效时间  ===== " + p.getEnddate());
			}
		}
	}
}
