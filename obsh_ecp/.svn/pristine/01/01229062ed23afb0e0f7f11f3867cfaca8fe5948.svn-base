package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryRelationNumService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryRelationNumServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;
import com.xwtech.xwecp.service.logic.pojo.PkgUse;
import com.xwtech.xwecp.service.logic.pojo.QRY050001Result;
import com.xwtech.xwecp.service.logic.pojo.RelationNum;

public class QRY050001Test {
	private static final Logger logger = Logger.getLogger(QRY050001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.238/js_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("15850573615");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15850573615");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		
		ic.addContextParameter("user_id", "1419300006484492");
		
		lic.setContextParameter(ic);
		
		IQueryRelationNumService co = new QueryRelationNumServiceClientImpl();
		QRY050001Result re = co.queryRelationNum("15850573615");//15996258556  15996650428
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());

			if (null != re.getRelationNum() && re.getRelationNum().size() > 0)
			{
				logger.info(" ====== getRelationNum ======" + re.getRelationNum().size());
				for (RelationNum dt : re.getRelationNum())
				{
					logger.info(" ====== getFlag ======" + dt.getFlag());
					logger.info(" ====== getPhoneNum ======" + dt.getPhoneNum());
					logger.info(" ====== getBeginDate ======" + dt.getBeginDate());
					logger.info(" ====== getEndDate ======" + dt.getEndDate());
					logger.info(" ====== ============================== ======");
				}
			}
			if (null != re.getPkgUse() && re.getPkgUse().size() > 0)
			{
				logger.info(" ====== getPkgUse ======" + re.getPkgUse().size());
				for (PkgUse dt : re.getPkgUse())
				{
					logger.info(" ====== getFlag ======" + dt.getFlag());
					logger.info(" ====== getName ======" + dt.getName());
					logger.info(" ====== getTotal ======" + dt.getTotal());
					logger.info(" ====== getUse ======" + dt.getUse());
					logger.info(" ====== getRemain ======" + dt.getRemain());
					logger.info(" ====== ============================== ======");
				}
			}
			
			if (null != re.getPkgDetail() && re.getPkgDetail().size() > 0)
			{
				logger.info(" ====== getPkgDetail ======" + re.getPkgDetail().size());
				for (PkgDetail dt : re.getPkgDetail())
				{
					logger.info(" ====== getPkgType ======" + dt.getPkgType());
					logger.info(" ====== getPkgName ======" + dt.getPkgName());
					logger.info(" ====== getBeginDate ======" + dt.getBeginDate());
					logger.info(" ====== getEndDate ======" + dt.getEndDate());
					logger.info(" ====== getPkgState ======" + dt.getPkgState());
					logger.info(" ====== ============================== ======");
				}
			}
		}
	}
}