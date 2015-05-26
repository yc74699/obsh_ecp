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
import com.xwtech.xwecp.service.logic.pojo.QRY090017Result;

/**
 * 根据号码和流水号查询锁号状态(商城调用)
 * @author 丁亮
 * 开发日期:Nov 22, 2012 9:15:24 PM
 */
public class QueryOtherNewMobileStateInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryOtherNewMobileStateInvocation.class);
	private IZxrwDAO zxrwDAO;
	public QueryOtherNewMobileStateInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.zxrwDAO = (IZxrwDAO) (springCtx.getBean("zxrwDAO"));
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY090017Result res = new QRY090017Result();
		try {
			String mobile = (String) getParameters(params, "mobile");
			String orderId = (String) getParameters(params, "orderId");
			String state = zxrwDAO.queryMobileLockState(mobile, orderId);
			res.setResultCode(LOGIC_SUCESS);
			res.setState(state);
		}
		catch (Exception e) {
			logger.error(e, e);
		}
		
		return res;
	}
}