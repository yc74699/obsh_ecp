package com.xwtech.xwecp.test;

import java.util.Properties;
import org.apache.log4j.Logger;
import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPkgUsedInfoCHService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPkgUsedInfoCHServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PkgInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040110Result;

public class QRY040110Test
{
	private static final Logger logger = Logger.getLogger(QRY040020Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.122.153:10008/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("12");
		lic.setUserMobile("15189570755");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15189570755");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "12");
		ic.addContextParameter("ddr_city", "12");
		ic.addContextParameter("user_id", "1214300000796048");

		lic.setContextParameter(ic);
		
		IQueryPkgUsedInfoCHService co = new QueryPkgUsedInfoCHServiceClientImpl();
		QRY040110Result re = co.queryPkgUsedInfoCH("15189570755","1");
		
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getPkgInfoList ======" + re.getPkgInfoList().size());
			
			if (null != re.getPkgInfoList() && re.getPkgInfoList().size() > 0)
			{
				for (PkgInfo dt : re.getPkgInfoList())
				{
					logger.info(" ====== getPkgName ======" + dt.getPkgName());
					if(dt.getSubUsedInfoList() != null)
						for(PkgUsedInfo sub : dt.getSubUsedInfoList()){
							logger.info(" ====== -- getPkgName ======" + sub.getPkgName());
							logger.info(" ====== -- getTotal ======" + sub.getTotal());
							logger.info(" ====== -- getRemain ======" + sub.getRemain());
							logger.info(" ====== -- getFlag ======" + sub.getFlag());
							logger.info(" ====== -- ============================== ======");
//							System.out.println("***************************************************");
//							System.out.println(sub);
//							System.out.println("***************************************************");
						}
					logger.info(" ====== ============================== ======");
					System.out.println(dt);
				}
			}
		}
	}
}