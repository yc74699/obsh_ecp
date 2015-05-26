package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IBlcseiQryRedPackService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.BlcseiQryRedPackServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY010031Result;
import com.xwtech.xwecp.service.logic.pojo.RedPack;
/**
 * 红包信息查询
 * 
 * @author xufan
 * 2014-03-27
 */
public class QRY010031Test
{
	private static final Logger logger = Logger.getLogger(QRY010031Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "wap_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp_test/xwecp.do");
		props.put("platform.user", "xl");
		props.put("platform.password", "xl");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13813382424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813382424");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		
		IBlcseiQryRedPackService co = new BlcseiQryRedPackServiceClientImpl();
		QRY010031Result re = co.blcseiQryRedPack("13813382424", "1419200008195116", 1);
		System.out.println(" ====== 开始返回参数 ======");
		if (re != null)
		{
			System.out.println(" ====== 返回结果码 ======" + re.getResultCode());
			System.out.println(" ====== 数量 ======" + re.getListRedPack().size());
			System.out.println(" ====== 错误编码 ======" + re.getErrorCode());
			System.out.println(" ====== 错误信息 ======" + re.getErrorMessage());
			
			if (null != re.getListRedPack() && re.getListRedPack().size() > 0)
			{
				for (RedPack dt : re.getListRedPack())
				{
					System.out.println("***************************返回的结果信息*******************************");
					System.out.println(" ====== --  ======" + dt.getExpiredate());
					System.out.println(" ====== --  ======" + dt.getGivemsisdn());
					System.out.println(" ====== --  ======" + dt.getGiveuserid());
					System.out.println(" ====== --  ======" + dt.getTakemsisdn());
					System.out.println(" ====== --  ======" + dt.getTakeuserid());
					System.out.println(" ====== --  ======" + dt.getGivedate());
					System.out.println(" ====== --  ======" + dt.getTakedate());
					System.out.println(" ====== --  ======" + dt.getStatus());
				 }
					
				
			}
		}
	}
}
