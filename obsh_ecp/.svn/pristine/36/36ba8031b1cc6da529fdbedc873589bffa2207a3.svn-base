package com.xwtech.xwecp.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryPkgUseStateService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryPkgUseStateServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.PkgUse;
import com.xwtech.xwecp.service.logic.pojo.PkgUseState;
import com.xwtech.xwecp.service.logic.pojo.QRY020003Result;

public class QRY020003Test
{
	private static final Logger logger = Logger.getLogger(QRY020003Test.class);
	
	public static void main(String[] args) throws Exception
	{
		Map map = new HashMap();
		map.put("0", "其他");
		map.put("1", "GSM秒数");
		map.put("2", "短信条数");
		map.put("3", "彩信条数");
		map.put("4", "GPRS流量(B)");
		map.put("5", "费用");
		map.put("6", "彩铃条数");
		map.put("7", "语音");
		map.put("8", "WLAN时长");
		map.put("9", "本地通话");
		map.put("10", "IP电话");
		
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
		lic.setUserCity("用户县市");
		lic.setUserMobile("13776669264");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13921909348");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13921909348");
		ic.addContextParameter("ddr_city", "23");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "2371200001226345");
		
		lic.setContextParameter(ic);
		
		IQueryPkgUseStateService co = new QueryPkgUseStateServiceClientImpl();
		QRY020003Result re = co.queryPkgUseState("13921909348", 1, "1431");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getPkgUseState ======" + re.getPkgUseState().size());
			
			if (null != re.getPkgUseState() && re.getPkgUseState().size() > 0)
			{
				for (PkgUseState dt : re.getPkgUseState())
				{
					logger.info(" ====== getPkgName ======" + dt.getPkgName());
					logger.info(" ====== getBeginDate ======" + dt.getBeginDate());
					logger.info(" ====== getEndDate ======" + dt.getEndDate());
					logger.info(" ====== getPkgUse ======" + dt.getPkgUse().size());
					if (null != dt.getPkgUse() && dt.getPkgUse().size() > 0)
					{
						for (PkgUse p : dt.getPkgUse())
						{
							logger.info(" ---------------------- ");
							logger.info(" ====== getName ====== " + p.getName());
							logger.info(" ====== getFlag ====== " + p.getFlag() + " : " + map.get(String.valueOf(p.getFlag())));
							logger.info(" ====== getTotal ====== " + p.getTotal());
							logger.info(" ====== getUse ====== " + p.getUse());
							logger.info(" ====== getRemain ====== " + p.getRemain());
						}
					}
					logger.info(" ====== ============================== ======");
				}
			}
		}
	}
}
