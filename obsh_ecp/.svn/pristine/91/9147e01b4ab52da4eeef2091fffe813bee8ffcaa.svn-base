package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
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
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY050001Result;
import com.xwtech.xwecp.service.logic.pojo.RelationNum;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryFN4SMSInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(QueryFN4SMSInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Map<String, String> map;

	private Map<String, String> mapCode;

	private ParseXmlConfig config;

	/**
	 * 最低消费30元
	 */
	private static final String RELATION_30 = "QQHMZHKT_ZDXF30Y";
	/**
	 * 最低消费50元
	 */
	private static final String RELATION_50 = "QQHMZHKT_ZDXF50Y";
	/**
	 * 1元功能费
	 */
	private static final String RELATION_1 = "QQHMZHKT_1YGNF";

	public QueryFN4SMSInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx
				.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx
				.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();

		if (this.map == null) {
			this.map = new HashMap<String, String>();
			this.map.put("4778", "最低消费30元");
			this.map.put("4779", "最低消费50元");
			this.map.put("4907", "1元功能费");

		}
		if (this.mapCode == null) {
			this.mapCode = new HashMap<String, String>();
			this.mapCode.put("4778", RELATION_30);
			this.mapCode.put("4779", RELATION_50);
			this.mapCode.put("4907", RELATION_1);
		}
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY050001Result res = new QRY050001Result();
		try {
			setPkgDetail(accessId, config, params, res);

			res = this.queryRelationGroup(accessId, config, params, res);

		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	private void setPkgDetail(String accessId, ServiceConfig config,
			List<RequestParameter> params, QRY050001Result res) {
		List<PkgDetail> groupList = this.getRelationGroupPckInfo(accessId,
				config, params, res);
		// 套餐类型编码
		if (null != groupList && groupList.size() > 0) {
			for (PkgDetail packDt : groupList) {
				String relationName = "";
				String pkgageCode = packDt.getPkgType();
				relationName = map.get(pkgageCode);
				packDt.setPkgName(relationName);
				List<PkgDetail> list = new ArrayList();
				list.add(packDt);
				res.setPkgDetail(list);
				break;
			}
		}
	}

	/**
	 * 查询亲情号码组合
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public QRY050001Result queryRelationGroup(String accessId,
			ServiceConfig config, List<RequestParameter> params,
			QRY050001Result res) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		RelationNum dt = null;
		RequestParameter r = null;
		List<RelationNum> list = null;
		ErrorMapping errDt = null;

		try {

			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetmsninfo_445",
					params);
			logger.debug(" ====== 查询亲情号码组合 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, "cc_cgetmsninfo_445", super
								.generateCity(params)));
				logger.debug(" ====== 查询亲情号码组合 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(
						root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050001",
							"cc_cgetmsninfo_445", resp_code);
					String errMessage = this.config.getChildText(this.config
							.getElement(root, "response"), "resp_desc");
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(resp_code);
					res.setErrorMessage(errMessage);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				list = new ArrayList();
				List memberTypeList = root.getChild("content").getChildren(
						"msn_member__type");
				if (null != memberTypeList && memberTypeList.size() > 0) {
					for (int i = 0; i < memberTypeList.size(); i++) {
						Element cplanpackagedt = ((Element) memberTypeList
								.get(i)).getChild("msn_member_dt");

						dt = new RelationNum();
						dt.setUserName(getNodeValueByNodeName(cplanpackagedt,
								"msn_member__gsm_user_id"));
						dt.setPhoneNum(getNodeValueByNodeName(cplanpackagedt,
								"msn_member__phone_number"));
						dt.setBeginDate(this.config.dateToString(this.config
								.stringToDate(
										getNodeValueByNodeName(cplanpackagedt,
												"msn_member__join_date"),
										"yyyyMMddHHmmss"), "yyyyMMdd"));
						dt.setEndDate(this.config.dateToString(this.config
								.stringToDate(
										getNodeValueByNodeName(cplanpackagedt,
												"msn_member__end_date"),
										"yyyyMMddHHmmss"), "yyyyMMdd"));
						if (null == dt.getEndDate()
								|| "".equals(dt.getEndDate())) {
							dt.setFlag(1); // 在用
						} else {
							dt.setFlag(0);
						}
						list.add(dt);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		res.setRelationNum(list);
		if (res.getRelationNum() == null || res.getRelationNum().size() <= 0) {
			res.setErrorMessage("用户未加入任何亲情号码组！");
		}
		return res;
	}

	private List getRelationGroupPckInfo(String accessId, ServiceConfig config,
			List<RequestParameter> params, QRY050001Result res) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		PkgDetail dt = null;
		List<PkgDetail> list = null;
		Set<String> pkgSet = new HashSet<String>();
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(
					"cc_cgetmsngsubpack_352", params);
			logger.debug(" ====== 查询用户加入的亲情组合套餐信息 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, "cc_cgetmsngsubpack_352", super
								.generateCity(params)));
				logger.debug(" ====== 查询用户加入的亲情组合套餐信息 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(
						root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY040031",
							"cc_cgetmsngsubpack_352", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List userList = this.config.getContentList(root,
						"package_user_id");
				if (null != userList && userList.size() > 0) {
					list = new ArrayList<PkgDetail>();
					for (int i = 0; i < userList.size(); i++) {
						Element packDt = this.config.getElement(
								(Element) userList.get(i), "cplanpackagedt");
						if (null != packDt) {
							if ("1035".equals(this.config.getChildText(packDt,
									"package_type"))) {
								String package_code = this.config.getChildText(
										packDt, "package_code");
								if ("4778".equals(package_code)
										|| "4779".equals(package_code)
										|| "4907".equals(package_code)) {
									if (pkgSet.contains(this.config
											.getChildText(packDt,
													"package_code")))
										continue;
									dt = new PkgDetail();
									dt.setPackageType(this.config.getChildText(
											packDt, "package_type"));
									dt.setPkgType(this.config.getChildText(
											packDt, "package_code"));
									logger.info("date :"
											+ this.config.getChildText(packDt,
													"package_use_date"));
									dt
											.setBeginDate(this.config
													.dateToString(
															this.config
																	.stringToDate(
																			this.config
																					.getChildText(
																							packDt,
																							"package_use_date"),
																			"yyyyMMdd"),
															"yyyyMMdd"));
									logger.info("setBeginDate :"
											+ dt.getBeginDate());

									dt
											.setEndDate(this.config
													.dateToString(
															this.config
																	.stringToDate(
																			this.config
																					.getChildText(
																							packDt,
																							"package_end_date"),
																			"yyyyMMdd"),
															"yyyyMMdd"));
									dt.setPkgState(this.config.getChildText(
											packDt, "package_state"));

									list.add(dt);
									pkgSet.add(dt.getPkgType());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		if (null == list || list.size() <= 0) {
			res.setResultCode(LOGIC_ERROR);
			res.setErrorCode(LOGIC_ERROR);
			res.setErrorMessage("用户未加入任何亲情号码组!");
		}
		return list;
	}

	/**
	 * 返回 年月日小时分秒
	 * 
	 * @return
	 */
	public static String getTodayChar14() {
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * 比对两个时间间隔
	 * 
	 * @param startDateTime
	 *            开始时间
	 * @param endDateTime
	 *            结束时间
	 * @param distanceType
	 *            计算间隔类型 天d 小时h 分钟m 秒s
	 * @return
	 */
	public static String getDistanceDT(String startDateTime,
			String endDateTime, String distanceType) {
		String strResult = "";
		long lngDistancVal = 0;
		try {
			SimpleDateFormat tempDateFormat = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			Date startDate = tempDateFormat.parse(startDateTime);
			Date endDate = tempDateFormat.parse(endDateTime);
			if (distanceType.equals("d")) {
				lngDistancVal = (endDate.getTime() - startDate.getTime())
						/ (1000 * 60 * 60 * 24);
			} else if (distanceType.equals("h")) {
				lngDistancVal = (endDate.getTime() - startDate.getTime())
						/ (1000 * 60 * 60);
			} else if (distanceType.equals("m")) {
				lngDistancVal = (endDate.getTime() - startDate.getTime())
						/ (1000 * 60);
			} else if (distanceType.equals("s")) {
				lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000);
			}
			strResult = String.valueOf(lngDistancVal);
		} catch (Exception e) {
			strResult = "0";
		}
		return strResult;
	}
}
