package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client_impl.common.IOpenVnetShortNumService;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.OpenVnetShortNumServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.DEL010016Result;
import com.xwtech.xwecp.service.logic.pojo.VnetGroupProductInfo;

public class DEL010016Test
{
	private static final Logger logger = Logger.getLogger(DEL010016Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("11");
		lic.setUserMobile("13601542424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13601542424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13601542424");
		ic.addContextParameter("ddr_city", "11");
		ic.addContextParameter("user_id", "1101200007185587");  //13813382424-1419200008195116,13913032424-1419200008195160
		lic.setContextParameter(ic);
		
		IOpenVnetShortNumService co = new OpenVnetShortNumServiceClientImpl();
		//动感地带 13401312424  brand_id：11 city_id：17 user_id：1738200005062065
		//全球通 13913032424 user_id：1419200008195160
		//1、开通 2、关闭 3、变更
		//1、立即 2、次日 3、次月
		DEL010016Result re = co.openVnetShortNum("13601542424", 1);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" === re.getResultCode() === " + re.getResultCode());
			logger.info(" === re.getErrorCode() === " + re.getErrorCode());
			logger.info(" === re.getErrorMessage() === " + re.getErrorMessage());
			if(null != re.getVnetGroupProductInfo())
			{
				for(VnetGroupProductInfo vInfo : re.getVnetGroupProductInfo())
				{
					logger.info(" === vInfo.getSubsName() === " + vInfo.getSubsName());
					logger.info(" === vInfo.getGrpsubsId() === " + vInfo.getGrpsubsId());
					logger.info(" === vInfo.getEffectDate() === " + vInfo.getEffectDate());
					logger.info(" === vInfo.getShortNum() === " + vInfo.getShortNum());
					logger.info(" === vInfo.getShortNumEffectDate() === " + vInfo.getShortNumEffectDate());
				}
			}
		}
	}
}
