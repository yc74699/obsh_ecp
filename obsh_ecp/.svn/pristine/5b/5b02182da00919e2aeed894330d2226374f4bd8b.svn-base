package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.dao.OdcDao;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY080002Result;
import com.xwtech.xwecp.service.logic.pojo.UserOdcInfo;

/**
 * 用户一维码获奖信息查询
 * 
 * @author 吴宗德
 * 
 */
public class QueryUserOdcInfoInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryUserOdcInfoInvocation.class);

	private OdcDao odcDao;

	public QueryUserOdcInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.odcDao = (OdcDao) (springCtx.getBean("odcDao"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY080002Result res = new QRY080002Result();
		res.setResultCode(LOGIC_ERROR);
		res.setErrorMessage("");
		try {
			String mobile = (String)this.getParameters(params, "mobile");
			String mrcNum = (String)this.getParameters(params, "mrcNum");
			String odcNum = (String)this.getParameters(params, "odcNum");
			String merAccountNum = (String)this.getParameters(params, "merAccountNum");
			String merAccountPwd = (String)this.getParameters(params, "merAccountPwd");
			
			if (StringUtils.isBlank(mobile) || StringUtils.isBlank(mrcNum)
					|| StringUtils.isBlank(merAccountNum) || StringUtils.isBlank(merAccountPwd)) {
				res.setErrorCode(LOGIC_PARAM_ERROR);
				res.setErrorMessage("参数错误！");
				return res;
			}
			
			if (odcDao.checkMerAccount(merAccountNum, merAccountPwd)) {
				res.setResultCode(LOGIC_SUCESS);
				List<UserOdcInfo> result = odcDao.queryUserOdcInfo(mobile, mrcNum, odcNum);
				res.setUserOdcList(result);
			} else {
				res.setErrorCode("-60005");
				res.setErrorMessage("账号、密码校验失败！");
			}

		} catch (Exception e) {
			logger.error(e, e);
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode(LOGIC_EXCEPTION);
		}
		return res;
	}

}