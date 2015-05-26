package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.xwtech.xwecp.pojo.ChannelInfo;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY030003Result;
import com.xwtech.xwecp.service.logic.pojo.ScoreExchange;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DictQueryUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryScoreExchangeDetailInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryScoreExchangeDetailInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryScoreExchangeDetailInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig configInfo, List<RequestParameter> params) {

		QRY030003Result res = new QRY030003Result();

		// 积分兑换历史信息
		List<ScoreExchange> scoreExchangeList = null;

		List<ScoreExchangeExt> scoreExchangeExtList = null;

		// 渠道字典
		Map<String, String> channelMap = null;

		// 类型、金额字典
		Map<String, Map<String, String>> exchangeDetailMap = null;

		try {
			scoreExchangeExtList = queryScoreExchange(accessId, params, res);

			if (scoreExchangeExtList != null && scoreExchangeExtList.size() > 0) {
				RequestParameter p = new RequestParameter("dict_type", 10);
				params.add(p);
				List<ChannelInfo> exchangeTypelList = DictQueryUtil.queryOprChannel(accessId, params, res, bossTeletextUtil, remote, wellFormedDAO, config);
				channelMap = DictQueryUtil.getDictMapFromList(exchangeTypelList);

				RequestParameter p1 = new RequestParameter("extType", 5);
				params.add(p1);
				exchangeDetailMap = DictQueryUtil.queryScoreExchangeTypeAndFeeNew(accessId, params, res, bossTeletextUtil, remote, wellFormedDAO, config);

				// 类型map字典
				Map<String, String> typeMap = null;

				// 金额map字典
				Map<String, String> valueMap = null;

				if (exchangeDetailMap != null && exchangeDetailMap.size() > 0) {
					typeMap = exchangeDetailMap.get("SERVERNAME");
					valueMap = exchangeDetailMap.get("FEENAME");
				}

				Iterator iter = scoreExchangeExtList.iterator();
				while (iter.hasNext()) {
					ScoreExchangeExt scoreExchangeExt = (ScoreExchangeExt) iter.next();

					// 渠道转换
					if (channelMap != null && channelMap.get(scoreExchangeExt.getExchangeChannel()) != null) {
						scoreExchangeExt.setExchangeChannel(channelMap.get(scoreExchangeExt.getExchangeChannel()));
					}
					// 类型转换
					if (typeMap != null && typeMap.get(scoreExchangeExt.getRedeemType()) != null) {
						scoreExchangeExt.setExchangeType(typeMap.get(scoreExchangeExt.getRedeemType()));
					}
					// 金额转换
					if (valueMap != null && valueMap.get(scoreExchangeExt.getRedeemType()) != null) {
						scoreExchangeExt.setExchangeFee(Long.valueOf(valueMap.get(scoreExchangeExt.getRedeemType())));
					}
				}

				scoreExchangeList = new ArrayList<ScoreExchange>(scoreExchangeExtList.size());
				for (int i = 0; i < scoreExchangeExtList.size(); i++) {
					ScoreExchangeExt scoreExchangeExt = scoreExchangeExtList.get(i);
					ScoreExchange scoreExchange = new ScoreExchange();
					scoreExchange.setExchangeTime(scoreExchangeExt.getExchangeTime());
					scoreExchange.setExchangeType(scoreExchangeExt.getExchangeType());
					scoreExchange.setExchangeScore(scoreExchangeExt.getExchangeScore());
					scoreExchange.setExchangeFee(scoreExchangeExt.getExchangeFee());
					scoreExchange.setExchangeChannel(scoreExchangeExt.getExchangeChannel());
					scoreExchangeList.add(scoreExchange);
				}

				// 排序
				new SortScoreDetail().srotDetailList(scoreExchangeList, false);
				res.setScoreExchange(scoreExchangeList);
			}
		} catch (Exception e) {
			res.setResultCode("1");
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 查询历史记录
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<ScoreExchangeExt> queryScoreExchange(String accessId, List<RequestParameter> params, QRY030003Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<ScoreExchangeExt> scoreExchangeListResult = null;
		ScoreExchangeExt scoreExchangeExt = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetscoredetail_556", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetscoredetail_556", super.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY030003", "cc_cgetscoredetail_556", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List scoreExchangeList = this.config.getContentList(root, "score_detail_user_id");

				if (null != scoreExchangeList && scoreExchangeList.size() > 0) {
					scoreExchangeListResult = new ArrayList<ScoreExchangeExt>(scoreExchangeList.size());
					for (int i = 0; i < scoreExchangeList.size(); i++) {
						Element scoreExchangeInfo = this.config.getElement((Element) scoreExchangeList.get(i), "cscoreinfodetail_dt");
						scoreExchangeExt = new ScoreExchangeExt();
						scoreExchangeExt.setExchangeTime(this.config.getChildText(scoreExchangeInfo, "score_detail_rec_date"));
						scoreExchangeExt.setExchangeScore(transStringToLong(this.config.getChildText(scoreExchangeInfo, "score_detail_score")));
						scoreExchangeExt.setExchangeChannel(this.config.getChildText(scoreExchangeInfo, "score_detail_source"));
						scoreExchangeExt.setRedeemType(this.config.getChildText(scoreExchangeInfo, "score_detail_redeem_type"));
						scoreExchangeListResult.add(scoreExchangeExt);
					}
				}
			}
		} catch (Exception e) {
			res.setResultCode("1");
			logger.error(e, e);
		}
		return scoreExchangeListResult;
	}

	public Long transStringToLong(String str) {
		if (str != null && !str.equals("")) {
			if (str.indexOf(".") == -1) {
				return Long.valueOf(str);
			} else {
				return Long.valueOf(str.substring(0, str.indexOf(".")));
			}
		}
		return Long.valueOf(0);
	}

	/**
	 * 把查询出来的受理历史按时间排序
	 * 
	 * @param detailList
	 *            受理历史列表
	 * @param isAsc
	 *            是否升序
	 * @return
	 */
	private class SortScoreDetail implements Comparator {

		private boolean isAsc = false;

		public void srotDetailList(List<ScoreExchange> scoreList, boolean isAsc) {
			this.isAsc = isAsc;
			if (scoreList != null && scoreList.size() > 0) {
				Collections.sort(scoreList, this);
			}
		}

		public int compare(Object o1, Object o2) {
			int result = 0;
			String date1 = ((ScoreExchange) o1).getExchangeTime();
			String date2 = ((ScoreExchange) o2).getExchangeTime();
			if (date1 != null && date2 == null) {
				result = 1;
			} else if (date1 == null && date2 != null) {
				result = -1;
			} else if (date1 != null && date2 != null) {
				result = date1.compareTo(date2);
			}

			result = isAsc ? result : result * -1;

			return result;
		}

	}

	class ScoreExchangeExt {
		private String exchangeTime;

		private String exchangeType;

		private long exchangeScore;

		private long exchangeFee;

		private String exchangeChannel;

		private String redeemType;

		public String getExchangeTime() {
			return exchangeTime;
		}

		public void setExchangeTime(String exchangeTime) {
			this.exchangeTime = exchangeTime;
		}

		public String getExchangeType() {
			return exchangeType;
		}

		public void setExchangeType(String exchangeType) {
			this.exchangeType = exchangeType;
		}

		public long getExchangeScore() {
			return exchangeScore;
		}

		public void setExchangeScore(long exchangeScore) {
			this.exchangeScore = exchangeScore;
		}

		public long getExchangeFee() {
			return exchangeFee;
		}

		public void setExchangeFee(long exchangeFee) {
			this.exchangeFee = exchangeFee;
		}

		public String getExchangeChannel() {
			return exchangeChannel;
		}

		public void setExchangeChannel(String exchangeChannel) {
			this.exchangeChannel = exchangeChannel;
		}

		public String getRedeemType() {
			return redeemType;
		}

		public void setRedeemType(String redeemType) {
			this.redeemType = redeemType;
		}
	}

}
