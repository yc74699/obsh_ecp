package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetPkgInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetPkgInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050053Result;
import com.xwtech.xwecp.service.logic.pojo.TransProPackageBusi;
import com.xwtech.xwecp.service.logic.pojo.UsrTransSelectBean;
import com.xwtech.xwecp.util.DESEncrypt;

public class QRY050053Test
{
	private static final Logger logger = Logger.getLogger(QRY050053Test.class);

	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://10.32.229.82:10000/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.65.71:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13401462424");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13401462424");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13401462424");
		
		lic.setContextParameter(ic);
		IGetPkgInfoService co = new GetPkgInfoServiceClientImpl();
		QRY050053Result result = co.getPkgInfo("11", "ChangeProduct", "13401462424", "1000100301");
		if (result != null)
		{
			logger.info(" ====== getResultCode ======" + result.getResultCode());
			logger.info(" ====== getResultCode ======" + result.getBossCode());
			logger.info(" ====== getErrorCode ======" + result.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + result.getErrorMessage());
		    Map<String,UsrTransSelectBean> bxtcMap  = result.getBxtcMap();
			for(Entry<String,UsrTransSelectBean> entry : bxtcMap.entrySet())
			{
				System.out.println("key:"+entry.getKey());
				UsrTransSelectBean bean = entry.getValue();
				if(null != bean )
				{
					List<TransProPackageBusi> beans =bean.getTransBusiList();
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
					System.out.println(beans.size());
					for(TransProPackageBusi bean2 : beans)
					{
						System.out.println(bean2);
					}
					System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				}
			}
			System.out.println("****************************************");
			List<TransProPackageBusi> zzyw = result.getZzyw();
			for(TransProPackageBusi busi : zzyw)
			{
				System.out.println(busi);
			}
			List<TransProPackageBusi> scyw = result.getScyw();
			System.out.println("****************************************");
			for(TransProPackageBusi busi : scyw)
			{
				System.out.println(busi);
			}
		}
	}
}
