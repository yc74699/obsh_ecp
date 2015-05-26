package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.xwtech.xwecp.service.logic.pojo.QRY030004Result;
import com.xwtech.xwecp.service.logic.pojo.ScoreReserve;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DictQueryUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryScoreReserveDetailInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryScoreReserveDetailInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryScoreReserveDetailInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig configInfo, List<RequestParameter> params) {

		QRY030004Result res = new QRY030004Result();

		// 积分预约信息
		List<ScoreReserve> scoreReserveList = null;

		ScoreReserve scoreReserve = null;

		List<ScoreReserveExt> scoreReserveExtList = null;

		// 类型、金额字典
		Map<String, Map<String, String>> exchangeDetailMap = null;

		try {
			scoreReserveExtList = queryScoreReserve(accessId, params, res);

			if (scoreReserveExtList != null && scoreReserveExtList.size() > 0) {
				RequestParameter p = new RequestParameter("extType", 5);
				params.add(p);
				exchangeDetailMap = DictQueryUtil.queryScoreExchangeTypeAndFee(accessId, params, res, bossTeletextUtil, remote, wellFormedDAO, config);

				// 类型map字典
				Map<String, String> typeMap = null;

				if (exchangeDetailMap != null && exchangeDetailMap.size() > 0) {
					typeMap = exchangeDetailMap.get("SERVERNAME");
				}

				scoreReserveList = new ArrayList<ScoreReserve>(scoreReserveExtList.size());
				Iterator iter = scoreReserveExtList.iterator();
				while (iter.hasNext()) {
					ScoreReserveExt scoreReserveExt = (ScoreReserveExt) iter.next();
					scoreReserve = new ScoreReserve();
					// 类型转换
					if (typeMap != null && typeMap.get(scoreReserveExt.getRedeemType()) != null) {
						scoreReserveExt.setProduct(typeMap.get(scoreReserveExt.getRedeemType()));
					}

					scoreReserve.setExchangeTime(scoreReserveExt.getExchangeTime());
					scoreReserve.setProduct(scoreReserveExt.getProduct());
					scoreReserve.setExchangeScore(scoreReserveExt.getExchangeScore());
					scoreReserve.setExchangeState(scoreReserveExt.getExchangeState());
					scoreReserve.setSendTime(scoreReserveExt.getSendTime());
					scoreReserve.setNum(scoreReserveExt.getNum());
					scoreReserveList.add(scoreReserve);
				}
				res.setScoreReserve(scoreReserveList);

			}
		} catch (Exception e) {
			res.setResultCode("1");
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 查询积分预约记录
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<ScoreReserveExt> queryScoreReserve(String accessId, List<RequestParameter> params, QRY030004Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<ScoreReserveExt> scoreReserveExtListResult = null;
		ScoreReserveExt scoreReserveExt = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetbyuser_567", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetbyuser_567", super.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY030004", "cc_cgetbyuser_567", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				// 用户预约信息
				List userReserveList = this.config.getContentList(root, "score_send_user_id");
				// 产品操作信息
				List scoreOprReserveList = this.config.getContentList(root, "score_change_opearting_srl");

				if (userReserveList != null && scoreOprReserveList != null) {
					if (userReserveList.size() == scoreOprReserveList.size()) {
						res.setResultCode("1");
						return scoreReserveExtListResult;
					}

					scoreReserveExtListResult = new ArrayList<ScoreReserveExt>(userReserveList.size());

					for (int i = 0; i < userReserveList.size(); i++) {
						Element userReserveInfo = this.config.getElement((Element) userReserveList.get(i), "cscoresenddt");
						Element oprReserveInfo = this.config.getElement((Element) scoreOprReserveList.get(i), "coperatingscorechangedt");
						scoreReserveExt = new ScoreReserveExt();
						scoreReserveExt.setExchangeTime(this.config.getChildText(oprReserveInfo, "score_change_redeemed_date"));
						scoreReserveExt.setExchangeScore(Long.valueOf(this.config.getChildText(oprReserveInfo, "score_change_change_num")));
						scoreReserveExt.setExchangeState(this.config.getChildText(userReserveInfo, "score_send_status"));
						scoreReserveExt.setSendTime(this.config.getChildText(userReserveInfo, "score_send_date"));
						scoreReserveExt.setNum(this.config.getChildText(oprReserveInfo, "score_change_redeemed_number"));
						scoreReserveExtListResult.add(scoreReserveExt);
					}
				}
			}
		} catch (Exception e) {
			res.setResultCode("1");
			logger.error(e, e);
		}
		return scoreReserveExtListResult;
	}

	class ScoreReserveExt {
		private String exchangeTime;

		private String product;

		private long exchangeScore;

		private String exchangeState;

		private String sendTime;

		private String num;

		private String redeemType;

		public String getExchangeTime() {
			return exchangeTime;
		}

		public void setExchangeTime(String exchangeTime) {
			this.exchangeTime = exchangeTime;
		}

		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public long getExchangeScore() {
			return exchangeScore;
		}

		public void setExchangeScore(long exchangeScore) {
			this.exchangeScore = exchangeScore;
		}

		public String getExchangeState() {
			return exchangeState;
		}

		public void setExchangeState(String exchangeState) {
			this.exchangeState = exchangeState;
		}

		public String getSendTime() {
			return sendTime;
		}

		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getRedeemType() {
			return redeemType;
		}

		public void setRedeemType(String redeemType) {
			this.redeemType = redeemType;
		}
	}

}
