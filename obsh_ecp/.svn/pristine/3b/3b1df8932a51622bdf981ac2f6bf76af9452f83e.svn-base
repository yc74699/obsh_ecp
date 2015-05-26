package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.xwtech.xwecp.service.logic.pojo.MTribe;
import com.xwtech.xwecp.service.logic.pojo.QRY050011Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DictQueryUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryUserTribeInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryUserEPackageInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig configInfo;

	public QueryUserTribeInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.configInfo = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {

		QRY050011Result res = new QRY050011Result();
		List<MTribe> mTribeList = null;
		List<ChannelInfo> channelList = null;
		MTribe mTribe = null;
		// 用户已经加入的动感部落
		Map<String, String> joinedMap = null;
		try {
			RequestParameter p = new RequestParameter("dict_type", 152);
			params.add(p);
			channelList = DictQueryUtil.queryOprChannel(accessId, params, res, bossTeletextUtil, remote, wellFormedDAO, configInfo);
			if (channelList != null && channelList.size() > 0) {
				joinedMap = queryJoinedTribe(accessId, params, res);
				mTribeList = new ArrayList<MTribe>(channelList.size());
				for (int i = 0; i < channelList.size(); i++) {
					if (!channelList.get(i).getChannelId().equals("53")) {
						mTribe = new MTribe();
						mTribe.setTribeId(channelList.get(i).getChannelId());
						mTribe.setTribeName(channelList.get(i).getChannelName());
						if (joinedMap != null && joinedMap.get(channelList.get(i).getChannelId()) != null) {
							mTribe.setState("1");
						} else {
							mTribe.setState("2");
						}
						mTribeList.add(mTribe);
					}
				}

				res.setMTribeList(mTribeList);
			}
		} catch (Exception e) {
			res.setResultCode("1");
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 查询用户已经加入的动感部落
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public Map<String, String> queryJoinedTribe(String accessId, List<RequestParameter> params, QRY050011Result res) {
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		ErrorMapping errDt = null;
		Map<String, String> joinedMap = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgettribeinfo_561", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgettribeinfo_561", super.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.configInfo.getElement(rspXml);
				resp_code = this.configInfo.getChildText(this.configInfo.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY040014", "cc_cgettribeinfo_561", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List tribeList = this.configInfo.getContentList(root, "mzonetribeinfo_operating_srl");
				if (null != tribeList && tribeList.size() > 0) {
					joinedMap = new HashMap<String, String>(tribeList.size());
					for (int i = 0; i < tribeList.size(); i++) {
						Element tribeInfo = this.configInfo.getElement((Element) tribeList.get(i), "cmzonetribeinfodt");
						String tribeType = this.configInfo.getChildText(tribeInfo, "mzonetribeinfo_tribe_type");
						String startDate = this.configInfo.getChildText(tribeInfo, "mzonetribeinfo_start_date");
						joinedMap.put(tribeType, startDate);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return joinedMap;
	}

}
