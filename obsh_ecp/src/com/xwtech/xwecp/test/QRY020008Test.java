package com.xwtech.xwecp.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryDecibleInfoService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryDecibleInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.MusicClubMember;
import com.xwtech.xwecp.service.logic.pojo.QRY020008Result;
import com.xwtech.xwecp.service.logic.pojo.YearMusicInfo;

public class QRY020008Test
{
	private static final Logger logger = Logger.getLogger(QRY020008Test.class);
	
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
		props.put("client.channel", "test_channel");
		props.put("platform.url", "http://127.0.0.1/js_ecp/xwecp.do");
		props.put("platform.user", "zlbbq");
		props.put("platform.password", "zlbbq99");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("动感地带");
		lic.setUserCity("14");
		lic.setUserMobile("15295557935");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "15295557935");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		ic.addContextParameter("selfplatuserreg_user_id", "userid");
		
		ic.addContextParameter("user_id", "1419300001697777");
		
		lic.setContextParameter(ic);
		
		IQueryDecibleInfoService co = new QueryDecibleInfoServiceClientImpl();
		QRY020008Result re = co.queryDecibleInfo("15295557935", "");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			
			if (null != re.getMusicClubMember() && re.getMusicClubMember().size() > 0)
			{
				logger.info(" ====== re.getMusicClubMember.size() ====== " + re.getMusicClubMember().size());
				for (MusicClubMember dt : re.getMusicClubMember())
				{
					logger.info(" ====== getPkgId ======" + dt.getPkgId());
					logger.info(" ====== getMemberType ======" + (dt.getMemberType().equals("1")?"普通用户":"高级用户"));
					logger.info(" ====== getBeginDate ======" + dt.getBeginDate());
					logger.info(" ====== getEndDate ======" + dt.getEndDate());
					logger.info(" ====== getStatus ======" + dt.getStatus());
					
					if (null != dt.getYearMusicInfo() && dt.getYearMusicInfo().size() > 0)
					{
						logger.info(" ====== getYearMusicInfo.size() ======" + dt.getYearMusicInfo().size());
						for (YearMusicInfo p : dt.getYearMusicInfo())
						{
							logger.info(" ---------------------- ");
							logger.info(" ====== getYear ====== " + p.getYear());
							logger.info(" ====== getConsumption ====== " + p.getConsumption());
							logger.info(" ====== getHortation ====== " + p.getHortation());
							logger.info(" ====== getUse ====== " + p.getUse());
							logger.info(" ---------------------- ");
						}
					}
					if (null != dt.getDecibleInfo())
					{
						logger.info(" ====== getPresent ====== " + dt.getDecibleInfo().getPresent());
						logger.info(" ====== getUsePresent ====== " + dt.getDecibleInfo().getUsePresent());
						logger.info(" ====== getYearUse ====== " + dt.getDecibleInfo().getYearUse());
						logger.info(" ====== getRemain ====== " + dt.getDecibleInfo().getRemain());
					}
					logger.info(" ====== ============================== ======");
				}
			}
		}
	}
}
