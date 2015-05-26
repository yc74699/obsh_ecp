package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.IReserveDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.RES002Result;

/**
 * 查询营业厅订单数量
 * @author yg
 * 开发日期:Nov 12, 2012 7:04:50 PM
 */
public class QryAmountByOfficeInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QryAmountByOfficeInvocation.class);
	private IReserveDAO reserveDAO;
	public QryAmountByOfficeInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.reserveDAO = (IReserveDAO) (springCtx.getBean("reserveDAO"));
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		RES002Result res = new RES002Result();
		try {
			String officeId = (String) getParameters(params, "officeId");
			String expectTime = (String) getParameters(params, "expectTime");
			String expectPeriod = (String) getParameters(params, "expectPeriod");
			String amountFlag = (String) getParameters(params, "amountFlag"); //固定传2
			String busiOrderNum = reserveDAO.getBusiOfficeOrderNum(officeId, expectTime, expectPeriod);
			String marketOrderNum = reserveDAO.getMarketOfficeOrderNum(officeId, expectTime, expectPeriod);
			String allNum = String.valueOf((Integer.parseInt(busiOrderNum)+Integer.parseInt(marketOrderNum)));
			res.setResultCode(LOGIC_SUCESS);
			res.setAmount(allNum);
		}
		catch (Exception e) {
			
			logger.error("执行营业厅编码查询订单信息处理类Service失败");
			e.printStackTrace();
			res.setResultCode(LOGIC_ERROR);
			res.setErrorMessage("查询数据库失败");
		
			return res;
		}
		
		return res;
	}
}