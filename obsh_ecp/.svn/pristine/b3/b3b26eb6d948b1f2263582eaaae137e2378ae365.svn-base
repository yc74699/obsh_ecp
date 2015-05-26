package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.flow.works.chains.AbstractFlowControl;
import com.xwtech.xwecp.memcached.IMemcachedManager;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ActivitiesDetail;
import com.xwtech.xwecp.service.logic.pojo.REC001Result;

public class GetRecommendInfoInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(GetRecommendInfoInvocation.class);

	public REC001Result executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {

		String phoneNum = (String) getParameters(params, "phoneNum");
		String city = (String) getParameters(params, "region");
		String privid = (String) getParameters(params, "privid");

		REC001Result res = GetRecendInfo(phoneNum, city, privid);
		return res;
	}

	public static REC001Result GetRecendInfo(String phoneNum, String city,
			String privid) {

		REC001Result res = new REC001Result();
		try {
			IMemcachedManager cache = (IMemcachedManager) XWECPApp.SPRING_CONTEXT
					.getBean("cache1");
			String recommendStr = (String) cache.get(phoneNum);
			if (null != recommendStr) {
				String[] codes = recommendStr.split(",");
				List<ActivitiesDetail> adList = new ArrayList<ActivitiesDetail>();
				ActivitiesDetail ad = null;
				for (int i = 0; i < codes.length; i++) {
					Map m = (Map) cache.get(codes[i]);
					if (null != m) {
						String act_code2 = (String) m.get("ACTIVITIES_CODE_2") == null ? ""
								: (String) m.get("ACTIVITIES_CODE_2");
						String area = (String) m.get("ACTIVITIES_AREA");
						if ((privid.equals("") || act_code2.equals(privid)) && ("0".equals(area) || city.equals(area)))  {
								ad = new ActivitiesDetail();
								ad.setActivities_code((String) m
										.get("ACTIVITIES_CODE"));
								ad.setAccess_type_id((String) m
										.get("ACCESS_TYPE_ID"));
								ad.setActivities_big_pic((String) m
										.get("ACTIVITIES_BIG_PIC"));
								ad.setActivities_first_code((String) m
										.get("ACTIVITIES_FIRST_CODE"));
								ad.setActivities_name((String) m
										.get("ACTIVITIES_NAME"));
								ad.setActivities_offline_time((String) m
										.get("ACTIVITIES_OFFLINE_TIME"));
								ad.setActivities_online_time((String) m
										.get("ACTIVITIES_ONLINE_TIME"));
								ad.setActivities_page_name((String) m
										.get("ACTIVITIES_PAGE_NAME"));
								ad.setActivities_pic((String) m
										.get("ACTIVITIES_PIC"));
								ad.setActivities_pkid((String) m
										.get("ACTIVITIES_PKID"));
								ad.setActivities_state((String) m
										.get("ACTIVITIES_STATE"));
								ad.setSub_type((String) m.get("SUB_TYPE"));
								ad.setScene((String) m.get("SCENE"));
								ad.setButton_value((String) m
										.get("BUTTON_VALUE"));
								ad.setActivities_area((String) m
										.get("ACTIVITIES_AREA"));
								ad.setAccess_type_id((String) m
										.get("ACCESS_TYPE_ID"));
								ad.setActivities_type((String) m
										.get("ACTIVITIES_TYPE"));
								ad.setGrade(Integer.parseInt(m.get("GRADE")
										.toString()));
								ad.setRemark((String) m.get("REMARK"));
								ad.setActivites_code2((String) m
										.get("ACTIVITIES_CODE_2"));
								ad.setActivites_name2((String) m
										.get("ACTIVITIES_NAME_2"));
								ad.setActivites_page_name2((String) m
										.get("ACTIVITIES_PAGE_NAME_2"));
								ad.setWeb_code((String) m
										.get("WEB_CODE"));
								ad.setActivities_url((String) m
										.get("ACTIVITIES_URL"));
								ad.setParent_activities_code((String) m
										.get("PARENT_ACTIVITIES_CODE"));
								adList.add(ad);
						}
					}
				}
				res.setMmsDealDetail(adList);
				res.setResultCode(LOGIC_SUCESS);
				res.setErrorCode(LOGIC_SUCESS);
			} else {
				res.setResultCode(LOGIC_SUCESS);
				res.setErrorCode(LOGIC_SUCESS);
				res.setErrorMessage("无记录");
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}

}
