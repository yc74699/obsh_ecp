package com.xwtech.xwecp.test;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IGetPkgMoreInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.GetPkgMoreInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY050079Result;
import com.xwtech.xwecp.service.logic.pojo.TransProPackageBusi;
import com.xwtech.xwecp.service.logic.pojo.UsrTransSelectBean;
import com.xwtech.xwecp.util.DESEncrypt;

/**
 * 查询用户新主题产品下增值产品树关系(校园4G专用)
 * @author 张斌
 * 2015-5-4
 */
public class QRY050079Test
{
	private static final Logger logger = Logger.getLogger(QRY050079Test.class);

	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
//		props.put("platform.url", "http://10.32.229.82:10000/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.71:8080/obsh_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("1");
		lic.setUserCity("19");
		lic.setUserMobile("13921129992");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13921129992");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13921129992");
		ic.addContextParameter("user_id", "1946300018256536");
		
		lic.setContextParameter(ic);
		IGetPkgMoreInfoService co = new GetPkgMoreInfoServiceClientImpl();
		QRY050079Result result = co.getPkgMoreInfo("19", "ChangeProduct", "13921129992", "1000100110");
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
