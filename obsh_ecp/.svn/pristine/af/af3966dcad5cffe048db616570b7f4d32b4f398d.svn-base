package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.IZxrwDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.LanOrderInfo;

public class QueryLanOrderInfoInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryLanOrderInfoInvocation.class);
	private IZxrwDAO zxrwDAO;

	public QueryLanOrderInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.zxrwDAO = (IZxrwDAO) (springCtx.getBean("zxrwDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		LanOrderInfo res = new LanOrderInfo();
		try {
			String orderId = (String) getParameters(params, "sid");
			res = zxrwDAO.queryLanOrderInfo(orderId);
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorCode("0000");
			res.setErrorMessage("");
		}
		catch (Exception e) {
			logger.error(e, e);
		}
		
		return res;
	}
}
