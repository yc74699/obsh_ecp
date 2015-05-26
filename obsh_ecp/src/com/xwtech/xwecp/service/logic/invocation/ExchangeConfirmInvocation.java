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
import com.xwtech.xwecp.service.logic.pojo.DEL070002Result;
import com.xwtech.xwecp.service.logic.pojo.UserOdcInfo;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 一维码兑换确认
 * 
 * @author 吴宗德
 * 
 */
public class ExchangeConfirmInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(ExchangeConfirmInvocation.class);

	private OdcDao odcDao;

	public ExchangeConfirmInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.odcDao = (OdcDao) (springCtx.getBean("odcDao"));
 
	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL070002Result res = new DEL070002Result();
		res.setResultCode(LOGIC_ERROR);
		res.setErrorMessage("");
		try {
			String confirmSeq = (String)this.getParameters(params, "confirmSeq");
//			String merAwardNum = (String)this.getParameters(params, "merAwardNum");
			String merAccountNum = (String)this.getParameters(params, "merAccountNum");
			String merAccountPwd = (String)this.getParameters(params, "merAccountPwd");
			//1：兑换确认 2：兑换取消
			Integer type = (Integer)this.getParameters(params, "type");
			
			if (StringUtils.isBlank(confirmSeq) || StringUtils.isBlank(merAccountNum) 
					|| StringUtils.isBlank(merAccountPwd) || !(type == 1 || type == 2)) {
				res.setErrorCode(LOGIC_PARAM_ERROR);
				res.setErrorMessage("参数错误！");
				return res;
			}
			
			if (odcDao.checkMerAccount(merAccountNum, merAccountPwd)) {
				String odcNum = odcDao.queryOdcNumByXh(confirmSeq);
				if (StringUtils.isNotBlank(odcNum)) {
					UserOdcInfo odcInfo = odcDao.queryUserOdcInfoByOdcNum(odcNum);
					if (odcInfo != null) {
						//判断是否已兑换
						if (odcInfo.getIsExchange() != 1) {
							if (odcDao.queryActEndTime(odcInfo.getActNum()) < 1) {
								res.setErrorCode("-60014");
								res.setErrorMessage("已超过兑换截止日期！");
								return res;
							}
							String lockTime = odcDao.queryLockTimeByOdcNum(odcNum);
							String now = DateTimeUtil.getTodayChar14();
							//判断是否超时
							if (Integer.parseInt(DateTimeUtil.getDistanceDT(lockTime, now, "h")) < 1) {
								
								int eRet = 1;
								if (type == 1) {
									eRet = odcDao.odcExchangeConfirm(odcNum);
								}
								if (eRet > 0) {
									res.setResultCode(LOGIC_SUCESS);
									//更新一维码兑换明细
									odcDao.updateOdcExchangeDetail(confirmSeq, type);
									//删除一维码锁定信息
									odcDao.delLockOdcNum(odcNum);
								}
							} else {
								res.setErrorCode("-60011");
								res.setErrorMessage("已超时！");
							}
						} else {
							res.setErrorCode("-60004");
							res.setErrorMessage("已兑换！");
						}
					} else {
						res.setErrorCode("-60003");
						res.setErrorMessage("一维码不存在！");
					}
				} else {
					res.setErrorCode("-60012");
					res.setErrorMessage("确认码不存在！");
				}
			} else {
				res.setErrorCode("-60001");
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