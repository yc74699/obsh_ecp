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
import com.xwtech.xwecp.service.logic.pojo.QRY090018Result;
import com.xwtech.xwecp.service.logic.pojo.ZxrwOrderInfo;

/**
 * 根据流水号列表查询在线入网资料
 * @author 丁亮
 * 开发日期:Nov 12, 2012 7:06:35 PM
 */
public class QueryNewSubmitInfosInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryNewSubmitInfosInvocation.class);
	private IZxrwDAO zxrwDAO;
	
	public QueryNewSubmitInfosInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.zxrwDAO = (IZxrwDAO) (springCtx.getBean("zxrwDAO"));
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params){
		QRY090018Result res = new QRY090018Result();
		try {
			res.setErrorMessage("");
			String orderIds = (String) getParameters(params, "orderId");;
			List<ZxrwOrderInfo> orderInfos = zxrwDAO.queryZxrwSubmits(orderIds); //此处操作数据库查询状态
			res.setResultCode(LOGIC_SUCESS);
			res.setZxrwOrderInfolist(orderInfos);
		}
		catch (Exception e) {
			logger.error(e, e);
		}
		
		return res;
	}

	@SuppressWarnings("unused")
	private List<ZxrwOrderInfo> queryZxrwSubmits(String accessId, ServiceConfig config, List<RequestParameter> params) {
		return null;
	}
}