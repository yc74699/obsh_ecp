package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.IReserveDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.RES001Result;
import com.xwtech.xwecp.service.logic.pojo.ReserveOrderInfo;

/**
 * 预约系统查询订单状态
 * @author yg
 */
public class QryReserveOrderInfoInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QryReserveOrderInfoInvocation.class);
	private IReserveDAO reserveDAO;
	public QryReserveOrderInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.reserveDAO = (IReserveDAO) (springCtx.getBean("reserveDAO"));
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		RES001Result res = new RES001Result();
		try {
			String mobile = (String) getParameters(params, "mobile");
			String busiType = (String) getParameters(params, "busiType");
			String busiNum = (String) getParameters(params, "busiNum");
			//res.setAccessId(msg.getHead().getSequence());
			List<ReserveOrderInfo> orders = new ArrayList<ReserveOrderInfo>();
			if("1".equals(busiType))		//查询类型 1-业务 2-营销案 4-全部
			{
				orders = reserveDAO.getBusiOrder(mobile,busiNum);
				res.setReserveBusiList(orders);
			}else if("2".equals(busiType))
			{
				orders = reserveDAO.getMarketOrder(mobile,busiNum);
				res.setReserveMarketList(orders);
			}
			else if("4".equals(busiType))
			{
				orders = reserveDAO.getAllOrder(mobile);
				res.setReserveAllList(orders);
			}
			
			res.setResultCode(LOGIC_SUCESS);
		}
		catch (Exception e) {
			logger.error("执行手机号查询订单信息处理类Service失败");
			e.printStackTrace();
			res.setResultCode(LOGIC_ERROR);
			res.setErrorMessage("查询数据库失败");
		
			return res;
		}
		
		return res;
	}
}


