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
import com.xwtech.xwecp.service.logic.pojo.QRY050018Result;
import com.xwtech.xwecp.service.logic.pojo.ZxrwSiteOfficeInfo;

/**
 * 获取营业厅信息
 * @author 丁亮
 * 开发日期:Nov 12, 2012 6:56:05 PM
 */
public class QueryNewSaleOfficeInfoInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryNewSaleOfficeInfoInvocation.class);
	private IZxrwDAO zxrwDAO;
	public QueryNewSaleOfficeInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.zxrwDAO = (IZxrwDAO) (springCtx.getBean("zxrwDAO"));
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params){
		QRY050018Result res = new QRY050018Result();
		try {
			res.setErrorMessage("");
			String dq = (String)getParameters(params, "city");
			String xs = (String)getParameters(params, "county");
			List<ZxrwSiteOfficeInfo> siteOfficeInfoList = zxrwDAO.querySaleOfficeInfo(dq, xs);
			res.setResultCode(LOGIC_SUCESS);
			res.setZxrwSiteOfficeInfo(siteOfficeInfoList);
		}
		catch (Exception e) {
			logger.error(e, e);
		}
		
		return res;
	}
}