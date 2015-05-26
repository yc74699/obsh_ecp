package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.xwtech.xwecp.service.logic.pojo.MExchange;
import com.xwtech.xwecp.service.logic.pojo.QRY030005Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryMExchangeDetailInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryMExchangeDetailInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryMExchangeDetailInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {

		QRY030005Result res = new QRY030005Result();

		List<MExchange> mExchangeList = null;

		// 渠道信息
		List<ChannelInfo> channelInfoList = null;

		try {
			mExchangeList = queryMExchangeHistoryInfo(accessId, config, params, res);

			// Object obj = wellFormedDAO.getCache().get("COMM_DICTIONARY");
			// if(obj != null && obj instanceof List)
			// {
			// channelInfoList = (List<ChannelInfo>)obj;
			// }
			// else
			// {
			RequestParameter p = new RequestParameter("dict_type", "10");
			params.add(p);
			channelInfoList = queryOprChannel(accessId, params, res);
			// wellFormedDAO.getCache().add("COMM_DICTIONARY", channelInfoList, 0);
			// }
			Map channelMap = getDictMapFromList(channelInfoList);

			if (mExchangeList != null && mExchangeList.size() > 0) {
				Iterator it = mExchangeList.iterator();
				while (it.hasNext()) {
					MExchange mExchange = (MExchange) it.next();
					if (mExchange.getType().equals("")) {
						it.remove();
					} else {
						if (channelMap != null && channelMap.get(mExchange.getChannel()) != null) {
							mExchange.setChannel((String) channelMap.get(mExchange.getChannel()));
						}
					}
				}
			}

			res.setMExchange(mExchangeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 查询办理渠道
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<ChannelInfo> queryOprChannel(String accessId, List<RequestParameter> params, QRY030005Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<ChannelInfo> channelInfoList = null;
		ChannelInfo channelInfo = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_get_dictlist_78", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_get_dictlist_78", super.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY040004", "cc_cquerydetail_309", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List countList = this.config.getContentList(root, "list_size");
				if (null != countList && countList.size() > 0) {
					channelInfoList = new ArrayList<ChannelInfo>(countList.size());
					for (int i = 0; i < countList.size(); i++) {
						channelInfo = new ChannelInfo();
						channelInfo.setChannelId(this.config.getChildText((Element) countList.get(i), "dict_code"));
						channelInfo.setChannelName(this.config.getChildText((Element) countList.get(i), "dict_code_desc"));
						channelInfoList.add(channelInfo);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return channelInfoList;
	}

	/**
	 * 查询M值兑换历史记录
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<MExchange> queryMExchangeHistoryInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY030005Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<MExchange> mExchangeListResult = null;
		MExchange mExchange = null;
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryzonem_66_1", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqryzonem_66_1", super.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY030005", "cc_cqryzonem_66_1", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {

				List mExchangeList = this.config.getContentList(root, "zonemmodi_operating_srl");

				mExchangeListResult = new ArrayList<MExchange>(mExchangeList.size());

				if (null != mExchangeList && mExchangeList.size() > 0) {
					for (int i = 0; i < mExchangeList.size(); i++) {

						mExchange = new MExchange();
						Element mExchangeDetailInfo = this.config.getElement((Element) mExchangeList.get(i), "czonemmodidt");
						mExchange.setExchangeDesc(this.config.getChildText(mExchangeDetailInfo, "zonemmodi_operating_remark"));
						mExchange.setExchangeTime(this.config.getChildText(mExchangeDetailInfo, "zonemmodi_modi_date"));
						mExchange.setChannel(this.config.getChildText(mExchangeDetailInfo, "zonemmodi_operating_source"));

						String bossType = this.config.getChildText(mExchangeDetailInfo, "zonemmodi_modi_reason");
						String type = "";
						if (bossType != null) {
							// 兑换话费
							if ("7".equals(bossType)) {
								type = "1";
							}// 兑换业务
							else if ("6".equals(bossType)) {
								type = "2";
							}// 兑换实物
							else if ("2".equals(bossType)) {
								type = "3";
							}
						}
						mExchange.setType(type);
						mExchangeListResult.add(mExchange);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return mExchangeListResult;
	}

	/**
	 * @desc 将字典查询出的list转换成以字典Code为key的map
	 * @return
	 */
	private Map<String, String> getDictMapFromList(List<ChannelInfo> list) {
		Map<String, String> map = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			int size = list.size();
			ChannelInfo channelInfo = null;
			for (int i = 0; i < size; i++) {
				channelInfo = list.get(i);
				map.put(channelInfo.getChannelId(), channelInfo.getChannelName());
			}
		}
		return map;
	}

	class ChannelInfo {
		private String channelId;

		private String channelName;

		public String getChannelId() {
			return channelId;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

		public String getChannelName() {
			return channelName;
		}

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}
	}

}
