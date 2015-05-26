package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.DEL010004Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.MobileGame;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 手机游戏办理
 * 
 * @author 吴宗德
 * 
 */
public class TransactMobileGameInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(TransactMobileGameInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	public TransactMobileGameInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL010004Result res = new DEL010004Result();
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			List<MobileGame> mobileGames = null;
			for (RequestParameter param : params) {
				if ("mobileGames".equals(param.getParameterName())) {
					mobileGames = (List<MobileGame>) param.getParameterValue();
					break;
				}
			}

			int oprType = 0;
			if (mobileGames != null && mobileGames.size() > 0) {
				oprType = mobileGames.get(0).getOprType();
			}

			BaseResult transactRet = null;
			if (oprType == 1) {
				transactRet = this.openMobileGame(accessId, config, params);
			} else if (oprType == 2) {
				transactRet = this.closeMobileGame(accessId, config, params);
			}

			if (transactRet != null) {
				if (!LOGIC_SUCESS.equals(transactRet.getResultCode())) {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(transactRet.getErrorCode());
					res.setErrorMessage(transactRet.getErrorMessage());
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 手机游戏订购
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult openMobileGame(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		// String key = "PRO_LIST_";

		try {
			// for (RequestParameter param:params) {
			// if ("city".equals(param.getParameterName())) {
			// key += param.getParameterValue();
			// }
			// }
			//			
			// Object obj = this.wellFormedDAO.getCache().get(key);
			// if(obj != null && obj instanceof List)
			// {
			// proList = (List<CWebProductOpenCfgDtBean>)obj;
			// } else {

			String operationSrl = "";
			String todaychar14 = DateTimeUtil.getTodayChar14();

			BaseResult operatingSrlRet = this.generateOperatingSrl(accessId, config, params);

			if (LOGIC_SUCESS.equals(operatingSrlRet.getResultCode())) {
				operationSrl = (String) operatingSrlRet.getReObj();
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(operatingSrlRet.getErrorCode());
				res.setErrorMessage(operatingSrlRet.getErrorMessage());
				return res;
			}

			List<RequestParameter> paramNew = copyParam(params);
			paramNew.add(new RequestParameter("operationSrl", operationSrl));
			paramNew.add(new RequestParameter("todaychar14", todaychar14));

			reqXml = this.bossTeletextUtil.mergeTeletext("cc_dealmobile_829", paramNew);

			logger.debug(" ====== 手机游戏办理 手机游戏订购请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_dealmobile_829", super.generateCity(params)));
			logger.debug(" ====== 手机游戏办理 手机游戏订购返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("DEL010004", "cc_dealmobile_829", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
			// }
			// res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 手机游戏退订
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult closeMobileGame(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		// String key = "PRO_CFG_LIST_";

		try {
			// for (RequestParameter param:params) {
			// if ("city".equals(param.getParameterName())) {
			// key += param.getParameterValue();
			// }
			// }
			//			
			// Object obj = this.wellFormedDAO.getCache().get(key);
			// if(obj != null && obj instanceof List)
			// {
			// proCfgList = (List<CcCGetProByCityBean>)obj;
			// } else {

			String operationSrl = "";
			String todaychar14 = DateTimeUtil.getTodayChar14();

			BaseResult operatingSrlRet = this.generateOperatingSrl(accessId, config, params);

			if (LOGIC_SUCESS.equals(operatingSrlRet.getResultCode())) {
				operationSrl = (String) operatingSrlRet.getReObj();
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(operatingSrlRet.getErrorCode());
				res.setErrorMessage(operatingSrlRet.getErrorMessage());
				return res;
			}

			List<RequestParameter> paramNew = copyParam(params);
			paramNew.add(new RequestParameter("operationSrl", operationSrl));
			paramNew.add(new RequestParameter("todaychar14", todaychar14));

			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdelmobile_773", paramNew);

			logger.debug(" ====== 手机游戏办理 手机游戏退订请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cdelmobile_773", super.generateCity(params)));
			logger.debug(" ====== 手机游戏办理 手机游戏退订返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("DEL010004", "cc_cdelmobile_773", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
			// }
			// res.setReObj(proCfgList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 流水生成
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult generateOperatingSrl(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		// String key = "PRO_CFG_LIST_";
		try {
			// for (RequestParameter param:params) {
			// if ("city".equals(param.getParameterName())) {
			// key += param.getParameterValue();
			// }
			// }
			//			
			// Object obj = this.wellFormedDAO.getCache().get(key);
			// if(obj != null && obj instanceof List)
			// {
			// proCfgList = (List<CcCGetProByCityBean>)obj;
			// } else {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_getmobilesrl_830", params);

			logger.debug(" ====== 手机游戏办理 流水生成请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_getmobilesrl_830", super.generateCity(params)));
			logger.debug(" ====== 手机游戏办理 流水生成返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				if (!BOSS_SUCCESS.equals(errCode)) {
					errDt = this.wellFormedDAO.transBossErrCode("DEL010004", "cc_getmobilesrl_830", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);

				}
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					Element content = root.getChild("content");
					String operatingSrl = p.matcher(content.getChildText("cc_operating_srl")).replaceAll("");
					res.setReObj(operatingSrl);
				}
			}
			// }
			// res.setReObj(proCfgList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
}