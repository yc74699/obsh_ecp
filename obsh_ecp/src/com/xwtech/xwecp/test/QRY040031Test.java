package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.sms.IQuerySmsPkgUsedInfoService;
import com.xwtech.xwecp.service.logic.client_impl.sms.impl.QuerySmsPkgUsedInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.QRY040031Result;
import com.xwtech.xwecp.service.logic.pojo.SmsPkgInfo;
import com.xwtech.xwecp.service.logic.pojo.SmsPkgUsedInfo;

public class QRY040031Test
{
	private static final Logger logger = Logger.getLogger(QRY040031Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "sms_channel");
		//props.put("platform.url", "http://10.32.229.82:10008/sms_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10009/js_ecp/xwecp.do");
		props.put("platform.url", "http://127.0.0.1:8080/sms_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("16");
		lic.setUserMobile("18361725593");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "18361725593");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "16");
		ic.addContextParameter("ddr_city", "16");
		ic.addContextParameter("user_id", "1637300007487801");//1423200000471569
		
		lic.setContextParameter(ic);
		
		IQuerySmsPkgUsedInfoService co = new QuerySmsPkgUsedInfoServiceClientImpl();
		QRY040031Result re = co.querySmsPkgUsedInfo("18361725593");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== 结果码 ======" + re.getResultCode());
			logger.info(" ====== 可查询套餐个数 ======" + re.getPkgInfoList().size());
			
			if (null != re.getPkgInfoList() && re.getPkgInfoList().size() > 0)
			{
				for (SmsPkgInfo dt : re.getPkgInfoList())
				{
					logger.info(" ====== 可查询套餐名称 ======" + dt.getPkgName());
					logger.info(" ====== getPkgDec ======" + dt.getPkgDec());
					logger.info(" ====== getPkgType ======" + dt.getPkgType());
					logger.info(" ====== getPkgId ======" + dt.getPkgId());
					if(dt.getSubUsedInfoList() != null)
						for(SmsPkgUsedInfo sub : dt.getSubUsedInfoList()){
							logger.info(" ====== -- 子项名称 ======" + sub.getPkgName());
							logger.info(" ====== -- 总额 ======" + sub.getTotal());
							logger.info(" ====== -- 剩余 ======" + sub.getRemain());
							logger.info(" ====== -- getFlag ======" + sub.getFlag());
							logger.info(" ====== -- ============================== ======");
						}
					logger.info(" ====== ============================== ======");
				}
			}
		}
	}
}
