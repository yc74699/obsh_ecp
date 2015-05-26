package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryGroupUserUsedInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryGroupUserUsedInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PkgInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040051Result;

public class QRY040051Test
{
	private static final Logger logger = Logger.getLogger(QRY040051Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.152:10000/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("15150556323");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15150556323");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "15150556323");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("user_id", "1419200019655887");
		//13913032424/1419200008195160
		lic.setContextParameter(ic);
		
		IQueryGroupUserUsedInfoService co = new QueryGroupUserUsedInfoServiceClientImpl();
		QRY040051Result re = co.queryGroupUserUsedInfo("15150556323");
		logger.info(" ====== 开始返回参数 ======"+re);
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getPkgInfoList ======" + re.getPkgInfoList().size());
			
			if (null != re.getPkgInfoList() && re.getPkgInfoList().size() > 0)
			{
				for (PkgInfo dt : re.getPkgInfoList())
				{
					logger.info(" ====== getPkgName ======" + dt.getPkgName());
					logger.info(" ====== getGroupName ======" + dt.getGroupName());
					if(dt.getSubUsedInfoList() != null)
						for(PkgUsedInfo sub : dt.getSubUsedInfoList()){
							logger.info(" ====== -- getPkgName ======" + sub.getPkgName());
							logger.info(" ====== -- getTotal ======" + sub.getTotal());
							logger.info(" ====== -- getRemain ======" + sub.getRemain());
							logger.info(" ====== -- getFlag ======" + sub.getFlag());
							logger.info(" ====== -- ============================== ======");
						}
					logger.info(" ====== ============================== ======");
				}
			}
		}
	}
}
