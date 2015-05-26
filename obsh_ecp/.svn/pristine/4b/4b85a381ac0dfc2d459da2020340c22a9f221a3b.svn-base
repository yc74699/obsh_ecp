package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryFamilyPackageService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryFamilyPackageServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.FamilyPkgUse;
import com.xwtech.xwecp.service.logic.pojo.FamilyPkgUseState;
import com.xwtech.xwecp.service.logic.pojo.QRY020012Result;

public class QRY020012Test
{
	private static final Logger logger = Logger.getLogger(DEL040023Test.class);
	
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
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("13901583754");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13806152071");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "19");
		ic.addContextParameter("ddr_city", "19");
		
		ic.addContextParameter("user_id", "1949200000475035");
		
		lic.setContextParameter(ic);
		
		IQueryFamilyPackageService co = new QueryFamilyPackageServiceClientImpl();
		
		QRY020012Result re = co.queryFamilyPackage("1380652071", "1949200010270434",null);
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorCode ======" + re.getErrorCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			if (null != re.getFamilyPkgUseState()&& re.getFamilyPkgUseState().size() > 0)
			{
				logger.info(" ====== re.getFamilyPkgUseState.size() ====== " + re.getFamilyPkgUseState().size());
				for (FamilyPkgUseState dt : re.getFamilyPkgUseState())
				{
					logger.info(" ====== getPkgId ======" +dt.getPkgId());
					logger.info(" ====== getPkgName ======" + dt.getPkgName());
					logger.info(" ====== getPkgDesc ======" + dt.getPkgDesc());
					logger.info(" ====== getPkgType ======" + dt.getPkgType());
					logger.info(" ====== getBeginDate ======" + dt.getBeginDate());
					logger.info(" ====== getEndDate ======" + dt.getEndDate());
					logger.info(" ====== getFlag ======" + dt.getFlag());
					logger.info(" ====== getPkgUse().size() ======" + dt.getPkgUse().size());
					for (FamilyPkgUse dt2 : dt.getPkgUse()){
						logger.info(" ====== getName()======" + dt2.getName());
						logger.info(" ====== getTotal()======" + dt2.getTotal());
						logger.info(" ====== getUse()======" + dt2.getUse());
						logger.info(" ====== getRemain()======" + dt2.getRemain());
						logger.info(" ====== getFlag()======" + dt2.getFlag());
					}
					logger.info(" ===================================");
				
					
				}
			}
		}
	}
	
}
