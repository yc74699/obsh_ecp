package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.common.IQueryLableService;
import com.xwtech.xwecp.service.logic.client_impl.common.impl.QueryLableServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.LableResultBean;
import com.xwtech.xwecp.service.logic.pojo.QRY120001Result;

public class QRY120001Test {
	private static final Logger logger = Logger.getLogger(QRY120001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "data_channel");
//		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.100:10004/openapi_ecp/xwecp.do");
		props.put("platform.url", "http://10.32.122.167:10005/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.73:10006/openapi_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
//		props.put("platform.url", "http://10.32.229.92:10008/sms_ecp/xwecp.do");
		props.put("platform.user", "lw");
		props.put("platform.password", "lw");
		
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("biz_code_19234");
		lic.setOpType("开通/关闭/查询/变更");
		lic.setUserBrand("");
		lic.setUserCity("14");
		lic.setUserMobile("13606118860");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "1");
		ic.addContextParameter("route_type", "2");
		ic.addContextParameter("route_value", "13606118860");
		lic.setContextParameter(ic);
		
		IQueryLableService co = new QueryLableServiceClientImpl();
		
		QRY120001Result re = co.queryLableInfo("13606118860");
		logger.info(" ====== 开始返回参数 ======"+re);
		
		for(LableResultBean bean : re.getLableList() )
		{
			logger.info("tdKey: "+bean.getTdKey());
			logger.info("tdValue: "+bean.getTdValue());
		}
		if (re != null)
		{
//			 logger.info(" ====== getResultCode ======" + re.getResultCode());
//				logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
//				logger.info(" ====== getStat_date //统计月份======" + re.getStat_date());
//				logger.info(" ====== getUser_id //用户标识======" + re.getUser_id());
//				logger.info(" ====== getMsisdn //手机号码======" + re.getMsisdn());
//				logger.info(" ====== getArea_id //归属县市======" + re.getArea_id());
//				logger.info(" ====== getMarket_type //属地区域特征 ======" + re.getMarket_type());
//				logger.info(" ====== getHob_net //网络使用======" + re.getHob_net());
//				logger.info(" ====== getL_call_prefer //长途语音业务偏好======" + re.getL_call_prefer());
//				logger.info(" ====== getR_call_prefer //漫游通话偏好======" + re.getR_call_prefer());
//				logger.info(" ====== getPer_pack_saturation //套餐饱和度======" + re.getPer_pack_saturation());
//				logger.info(" ====== getPer_free_call //闲时通话集中度======" + re.getPer_free_call());
//				logger.info(" ====== getPer_busy_call //忙时通话集中度======" + re.getPer_busy_call());
//				logger.info(" ====== getFlow_free_percent //手机上网集中度======" + re.getFlow_free_percent());
//				logger.info(" ====== getPer_pack_saturation2 //套餐饱和度======" + re.getPer_pack_saturation2());
//				logger.info(" ====== getIs_sms_pot //潜在用户======" + re.getIs_sms_pot());
////				logger.info(" ====== getRelation_type //交往圈类型======" + re.getRelation_type());
//				logger.info(" ====== getIs_priv_type_reward //实物营销案偏好======" + re.getIs_priv_type_reward());
//				logger.info(" ====== getIs_priv_type_fee //话费营销案偏好======" + re.getIs_priv_type_fee());
//				logger.info(" ====== getMechine_brand //终端品牌======" + re.getMechine_brand());
//				logger.info(" ====== getPrice_desc //裸机价格======" + re.getPrice_desc());
//				logger.info(" ====== getAgent_type //终端购买渠道======" + re.getAgent_type());
//				logger.info(" ====== getIs_home //家庭特征======" + re.getIs_home());
//				logger.info(" ====== getIs_caption //是否主号======" + re.getIs_caption());
//				logger.info(" ====== getIs_many_m //是否多成员======" + re.getIs_many_m());
////				logger.info(" ====== getIs_kd_user //宽带目标客户======" + re.getIs_kd_user());
//				logger.info(" ====== getTwomodes_type_socre //双模手机得分======" + re.getTwomodes_type_socre());
//				logger.info(" ====== getLocal_call_dur //本地通话时长======" + re.getLocal_call_dur());
//				logger.info(" ====== getRoam_call_dur //漫游通话时长======" + re.getRoam_call_dur());
			
		}
	}
}
