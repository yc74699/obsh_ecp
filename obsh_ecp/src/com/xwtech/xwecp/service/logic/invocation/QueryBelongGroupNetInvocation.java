package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import org.apache.log4j.Logger;

import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.IPackageChangeDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY060073Result;

/**
 * 根据名称查询是否属于乡情网
 * 
 * @author 
 * 
 */
public class QueryBelongGroupNetInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger
			.getLogger(QueryBelongGroupNetInvocation.class);


	private IPackageChangeDAO packageChangeDAO;
	
	List<String> parList = null;
	
	public QueryBelongGroupNetInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		//查询乡情网名称dao
		this.packageChangeDAO = (IPackageChangeDAO) (springCtx.getBean("packageChangeDAO"));
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY060073Result res = new QRY060073Result();
		//是否乡情网结果
		String result = "0";
		try {
			String groupName = "";
			String cityId = "";
			
			for (RequestParameter r : params)
			{
				if ("groupName".equals(r.getParameterName())){
					groupName = String.valueOf(r.getParameterValue());
				}
				if("region".equals(r.getParameterName())){
					cityId = String.valueOf(r.getParameterValue());
				}
			}
			parList = this.packageChangeDAO.getXQGroupName(cityId);
			
			if(parList != null && parList.size() > 0){
				
				for (int i=0;i<parList.size();i++) {
					if(groupName.trim().equals(parList.get(i).trim())){
						result = "1";
						break;
					}
				}
				res.setResultCode(LOGIC_SUCESS);
				res.setErrorMessage("查询数据库或者缓存成功");
			}else{
				//读取数据库或者缓存数据失败
				res.setResultCode(LOGIC_ERROR);
				res.setErrorMessage("查询数据库或者缓存失败，请检查配置数据");
			}
		}catch (Exception e) {
			logger.error(e, e);
		}
		res.setFlag(result);
		return res;
	}

	
}