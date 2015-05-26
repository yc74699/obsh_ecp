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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050009Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 查询用户手机俱乐部信息
 * 
 * @author 吴宗德 2010-01-12
 */
public class QueryMobileClubInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryMobileClubInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	public QueryMobileClubInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));

	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY050009Result res = new QRY050009Result();
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			res.setResultCode("0");
			res.setErrorMessage("");
			// 查询会员信息
			String accountId = this.getAccountId(accessId, config, params);

			if (accountId != null && !"".equals(accountId)) {
				params.add(new RequestParameter("account_id", accountId));

				rspXml = (String) this.remote
						.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("cc_cqrymemberinfo_822", params), accessId, "cc_cqrymemberinfo_822", super.generateCity(params)));
				logger.debug(" ====== 查询账务信息返回报文 ======\n" + rspXml);

				if (null != rspXml && !"".equals(rspXml)) {
					Element root = this.getElement(rspXml.getBytes());
					String resp_code = root.getChild("response").getChildText("resp_code");
					// String resp_desc = root.getChild("response").getChildText("resp_desc");
					res.setResultCode("0000".equals(resp_code) ? "0" : "1");
					if (!"0000".equals(resp_code)) {
						errDt = this.wellFormedDAO.transBossErrCode("QRY050009", "cc_cqrymemberinfo_822", resp_code);
						if (null != errDt) {
							res.setErrorCode(errDt.getLiErrCode());
							res.setErrorMessage(errDt.getLiErrMsg());
						}
					}
					if (null != resp_code && "0000".equals(resp_code)) {
						Element content = root.getChild("content");
						if (content != null) {
							res.setMemberLevel(p.matcher(content.getChildText("memberlevel")).replaceAll(""));
							res.setIsFree(p.matcher(content.getChildText("isfree")).replaceAll(""));
							
							//获取余额,boss返回为元,需要分
							String balanceStr = p.matcher(content.getChildText("ccbalance")).replaceAll("");
							float balance = Float.parseFloat(balanceStr == null ? "0" : balanceStr);
							res.setBalance((balance * 100) + "");
							
							res.setUserState(p.matcher(content.getChildText("user_state")).replaceAll(""));
							res.setUserPayMode(p.matcher(content.getChildText("user_pay_mode")).replaceAll(""));
						}
					}
				}
			} else {
				logger.debug("==========获取用户帐户id失败!");
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 获取用户默认帐户ID
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected String getAccountId(String accessId, ServiceConfig config, List<RequestParameter> params) {
		String accountId = "";
		String rspXml = "";
		try {
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("ac_getrelbyuser_608", params), accessId, "ac_getrelbyuser_608", super.generateCity(params)));
			logger.debug(" ====== 查询账务信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				// String resp_desc = root.getChild("response").getChildText("resp_desc");
				if (null != resp_code && ("0000".equals(resp_code))) {
					List acordCount = null;
					try {
						acordCount = root.getChild("content").getChildren("arecord_count");
					} catch (Exception e) {
						acordCount = null;
					}
					if (null != acordCount && acordCount.size() > 0) {
						for (int i = 0; i < acordCount.size(); i++) {
							Element accountDt = ((Element) acordCount.get(i)).getChild("caccountingrelationdt");
							if (null != accountDt) {
								String isDefault = p.matcher(accountDt.getChildText("acrelation_isdefault")).replaceAll("");

								if (isDefault != null && "1".equals(isDefault)) {
									accountId = p.matcher(accountDt.getChildText("account_id")).replaceAll("");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return accountId;
	}

}
