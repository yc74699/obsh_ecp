package com.xwtech.xwecp.test;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.xwtech.xwecp.msg.InvocationContext;
import com.xwtech.xwecp.service.logic.client.LIInvocationContext;
import com.xwtech.xwecp.service.logic.client.XWECPLIClient;
import com.xwtech.xwecp.service.logic.client_impl.reserve.IQryReserveOrderInfoService;
import com.xwtech.xwecp.service.logic.client_impl.reserve.impl.QryReserveOrderInfoServiceClientImpl;
import com.xwtech.xwecp.service.logic.pojo.RES001Result;
import com.xwtech.xwecp.service.logic.pojo.ReserveOrderInfo;

public class RES001Test
{
	private static final Logger logger = Logger.getLogger(RES001Test.class);
	
	public static void main(String[] args) throws Exception
	{
		//初始化ecp客户端片段
		Properties props = new Properties();
		props.put("client.channel", "obsh_channel");
		props.put("platform.url", "http://127.0.0.1:8080/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.65.223:8082/obsh_ecp/xwecp.do");
		//props.put("platform.url", "http://10.32.122.166:10006/obsh_ecp/xwecp.do");
		props.put("platform.user", "jhr");
		props.put("platform.password", "jhr");
		XWECPLIClient client = XWECPLIClient.createInstance(props);
		//逻辑接口调用片段
		LIInvocationContext lic = LIInvocationContext.getContext();
		lic.setBizCode("预约业务查询");
		lic.setUserBrand("11");
		lic.setUserMobile("13913928099");
		InvocationContext ic = new InvocationContext();
		ic.addContextParameter("login_msisdn", "13813071284");
		ic.addContextParameter("route_type", "1");
		ic.addContextParameter("route_value", "14");
		ic.addContextParameter("ddr_city", "14");
		lic.setContextParameter(ic);
		//以下两个是预约系统特有的，必须要传
		ic.addContextParameter("brand", "11");
		ic.addContextParameter("channel", "01");
		try{
			IQryReserveOrderInfoService co = new QryReserveOrderInfoServiceClientImpl();
		//	"","",""
			/**
			 * 依次是
			 * 用户手机号
			 * 查询类型 1-业务 2-营销案
			 * 业务ID或者二级营销案唯一标识ID
			 * 
			 */
		//RES001Result re = co.qryReserveOrderInfo("13770492424","1","");
		//RES001Result re = co.qryReserveOrderInfo("13770492424","2","");
		RES001Result re = co.qryReserveOrderInfo("13770492424","4","");
		logger.info(" ====== 开始返回参数 ======");
		if (re != null)
		{
			logger.info(" ====== getResultCode ======" + re.getResultCode());
			logger.info(" ====== getErrorMessage ======" + re.getErrorMessage());
			if (null != re.getReserveMarketList() && re.getReserveMarketList().size() > 0)
			{
				logger.info(" ====== size ======" + re.getReserveMarketList().size());
				for (ReserveOrderInfo g : re.getReserveMarketList())
				{
					logger.info(" ====== 预约状态======" + g.getState());	
					logger.info(" ====== 预约名称======" + g.getMarketName());
					logger.info(" ====== 预约时间======" + g.getOrderTime());
					logger.info(" ====== 受理时间======" + g.getDoneTime());
					
					logger.info(" =========================================== ");
				}
			}
			
			if (null != re.getReserveBusiList() && re.getReserveBusiList().size() > 0)
			{
				logger.info(" ====== size ======" + re.getReserveBusiList().size());
				for (ReserveOrderInfo g : re.getReserveBusiList())
				{
					logger.info(" ======getBusiName ======" + g.getBusiName());
					logger.info(" ======getBusiNum ======" + g.getBusiNum());
					logger.info(" ======getResBz ======" + g.getOrderChannel());
					logger.info(" ======getResBz ======" + g.getResBz());
					logger.info(" =========================================== ");
				}
			}
			
			if (null != re.getReserveAllList() && re.getReserveAllList().size() > 0)
			{
				logger.info(" ====== size ======" + re.getReserveAllList().size());
				for (ReserveOrderInfo g : re.getReserveAllList())
				{
					
//					logger.info(" ======getBusiName ======" + g.getBusiName());
//					logger.info(" ======getMarketName ======" + g.getMarketName());
//					logger.info(" ======getBusiNum ======" + g.getBusiNum());
//					logger.info(" ======getResBz ======" + g.getOrderChannel());
//					logger.info(" ======getResBz ======" + g.getResBz());
					
					logger.info(" ====== 订单号======" + g.getOrderId());
					logger.info(" ====== 预约状态======" + g.getState());
					logger.info(" ====== 预约名称======" + g.getBusiName());
					logger.info(" ====== 预约名称======" + g.getMarketName());
					logger.info(" ====== 预约时间======" + g.getOrderTime());
					logger.info(" ====== 受理时间======" + g.getDoneTime());
					logger.info(" ====== 失败原因类别======" + g.getError_reuslt());
					logger.info(" ====== 失败原因备注======" + g.getError_bz());
					logger.info(" ====== 业务种类======" + g.getBusiType());
					logger.info(" ====== 备注======" + g.getBz());
					logger.info(" =========================================== ");
				}
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
