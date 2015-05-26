package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryUserJTUsedInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryUserJTUsedInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.GrpvpmInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgInfo;
import com.xwtech.xwecp.service.logic.pojo.PkgUsedInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY040020Result;

public class QRY060071Test {
	private static final Logger logger = Logger.getLogger(QRY060071Test.class);

	public static void main(String[] args) throws Exception {
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "icc_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		props.put("platform.user", "yt");
		props.put("platform.password", "yt");

		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");

		lic.setContextParameter(ic);

		IQueryUserJTUsedInfoService co = new QueryUserJTUsedInfoServiceClientImpl();

		QRY040020Result re = co.queryUserJTUsedInfo("13901587903","201309");

		if (null != re)
		{
			logger.info(" ====== getBossCode ======" + re.getResultCode());
			logger.info(" ====== getBossCode ======" + re.getErrorMessage());
			for (PkgInfo dt : re.getPkgInfoList())
			{
				
				logger.info(" ====== getPkgName ======" + dt.getPkgName());
				logger.info(" ====== getPkgId ======" + dt.getPkgId());
				if(dt.getSubUsedInfoList() != null)
					for(PkgUsedInfo sub : dt.getSubUsedInfoList()){
						logger.info(" ====== -- getPkgName ======" + sub.getPkgName());
						logger.info(" ====== -- getTotal ======" + sub.getTotal());
						logger.info(" ====== -- getRemain ======" + sub.getRemain());
						logger.info(" ====== -- getFlag ======" + sub.getFlag());
						
					}
				
			}
		}

	}
}
