package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GetMsngSubPackDT;
import com.xwtech.xwecp.service.logic.pojo.PkgDetail;
import com.xwtech.xwecp.service.logic.pojo.PkgUse;
import com.xwtech.xwecp.service.logic.pojo.QRY050001Result;
import com.xwtech.xwecp.service.logic.pojo.RelationNum;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryRelationNumInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryRelationNumInvocation.class);

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

	public QueryRelationNumInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();

		if (this.map == null || this.mapCode == null) {
			this.map = new HashMap<String, String>();
			this.map.put("4778", "最低消费30元");
			this.map.put("4779", "最低消费50元");
			this.map.put("4907", "1元功能费");

			this.mapCode = new HashMap<String, String>();
			this.mapCode.put("4778", RELATION_30);
			this.mapCode.put("4779", RELATION_50);
			this.mapCode.put("4907", RELATION_1);
		}
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY050001Result res = new QRY050001Result();
		String phoneNum = "";
		res.setResultCode("0");
		res.setErrorMessage("");

		try {
			for (RequestParameter par : params) {
				if ("phoneNum".equals(par.getParameterName())) {
					phoneNum = String.valueOf(par.getParameterValue());
				}
			}

			// 查询用户加入的亲情组合套餐信息
//			List<GetMsngSubPackDT> groupList = this.getRelationGroupPckInfo(accessId, config, params, res);
			List<PkgDetail> pkgList = this.getGroupackage(accessId, config, params, res);
			res.setPkgDetail(pkgList);//添加到result
			if (null != pkgList && pkgList.size() > 0) {
				// 套餐类型编码
				String relationCode = RELATION_1;

				for (PkgDetail packDt : pkgList) {
					String relationName = "";
					String pkgageCode = packDt.getPkgId();
					if (this.map.get(pkgageCode) == null) {
						relationName = "1元功能费";
					} else {
						relationName = map.get(pkgageCode);
						relationCode = mapCode.get(pkgageCode);
					}
					if (Long.parseLong(this.config.getDistanceDT(packDt.getBeginDate(), this.config.getTodayChar14(), "s")) >= 0) {
						// 查询亲情号码优惠情况
						this.getAcAGetZoneInfo(accessId, config, params, res, packDt.getPkgId(), relationName);
					} else {

						PkgUse pkg = new PkgUse();
						pkg.setBossId(packDt.getPkgId());
						pkg.setName(relationName);
						pkg.setFlag(9);
//						pkg.setUse(0);
//						pkg.setRemain(this.config.getChildText(content, "acctbkitem_invprn_flag"));
						pkg.setTotal("-1");
						List<PkgUse> list = new ArrayList();
						list.add(pkg);
						res.setPkgUse(list);
					}
				}

				// 查询亲情号码组合
				this.queryRelationGroup(accessId, config, params, res, "3", relationCode);

				// 判断用户是否预约关闭亲情号码组合
				if (null != res.getRelationNum() && null != res.getPkgUse()) {
					// 查询预约另一个亲情号码组合
					if (this.isBookOpenRelationNumber(phoneNum, res.getRelationNum())) {
						// 查询亲情号码组合
						this.queryRelationGroup(accessId, config, params, res, "0", relationCode);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		// 过滤号码和日期重复的信息
		mobileFilter(res);

		return res;
	}

	/**
	 * 过滤号码和日期重复的信息
	 * 
	 * @param res
	 * @return
	 */
	private void mobileFilter(QRY050001Result res) {
		List<RelationNum> relationList = res.getRelationNum();
		List<RelationNum> newRelationList = new ArrayList<RelationNum>();
		if (relationList != null && relationList.size() > 0) {
			for (RelationNum relation : relationList) {
				String beginDate = relation.getBeginDate() == null ? "" : relation.getBeginDate();
				String endDate = relation.getEndDate() == null ? "" : relation.getEndDate();
				String phoneNum = relation.getPhoneNum();

				boolean isAdd = true;
				for (RelationNum newRelation : newRelationList) {
					String newBeginDate = newRelation.getBeginDate() == null ? "" : newRelation.getBeginDate();
					String newEndDate = newRelation.getEndDate() == null ? "" : newRelation.getEndDate();
					String newPhoneNum = newRelation.getPhoneNum();

					if (newBeginDate.equals(beginDate) && newPhoneNum.equals(phoneNum) && endDate.equals(newEndDate)) {
						isAdd = false;
					}
				}

				if (isAdd) {
					newRelationList.add(relation);
				}
			}
		}

		res.setRelationNum(newRelationList);
	}

	/**
	 * 判断用户是否预约关闭亲情号码组合
	 * 
	 * @param number
	 * @param list
	 * @return
	 */
	public boolean isBookOpenRelationNumber(String number, List list) {
		try {
			if (null != list && list.size() > 0) {
				for (Object obj : list) {
					if (obj instanceof RelationNum) {
						RelationNum dt = (RelationNum) obj;
						if (number.equals(dt.getPhoneNum()) && (null == dt.getEndDate() || "".equals(dt.getEndDate()))) {
							return true;
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex, ex);
			return false;
		}

		return false;
	}

	/**
	 * 动感套餐优惠查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param pkgName
	 */
	public void getAcAGetZoneInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY050001Result res, String acrelation_revision, String pkgName) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		Element content = null;
		RequestParameter r = null;
		PkgUse pkg = null;
		List<PkgUse> list = null;
		ErrorMapping errDt = null;

		try {
			r = new RequestParameter();
			r.setParameterName("dbi_month_pr_number");
			r.setParameterValue(this.config.getTodayChar6());
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("cdr_reduce_total");
			r.setParameterValue("1");
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("a_bg_bill_month");
			r.setParameterValue("0");
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("acrelation_revision");
			r.setParameterValue(acrelation_revision);
			params.add(r);

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetzoneinfo_518", params);
			logger.debug(" ====== 动感套餐优惠查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetzoneinfo_518", super.generateCity(params)));
				logger.debug(" ====== 动感套餐优惠查询 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050001", "ac_agetzoneinfo_518", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				list = new ArrayList();
				content = this.config.getElement(root, "content");
				// 短信
				if (!"".equals(this.config.getChildText(content, "arecord_count"))) {
					pkg = new PkgUse();
					pkg.setBossId(acrelation_revision);
					pkg.setName(pkgName);
					pkg.setFlag(2);
					pkg.setUse(this.config.getChildText(content, "arecord_count"));
					pkg.setRemain(this.config.getChildText(content, "mms_len"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse()) + Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
				// IP电话
				if (!"".equals(this.config.getChildText(content, "data_down"))) {
					pkg = new PkgUse();
					pkg.setBossId(acrelation_revision);
					pkg.setName(pkgName);
					pkg.setFlag(10);
					pkg.setUse(this.config.getChildText(content, "data_down"));
					pkg.setRemain(this.config.getChildText(content, "fax_time"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse()) + Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
				// 本地通话
				if (!"".equals(this.config.getChildText(content, "data_up"))) {
					pkg = new PkgUse();
					pkg.setBossId(acrelation_revision);
					pkg.setName(pkgName);
					pkg.setFlag(9);
					pkg.setUse(this.config.getChildText(content, "data_up"));
					pkg.setRemain(this.config.getChildText(content, "rec_time"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse()) + Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
				// 彩信
				if (!"".equals(this.config.getChildText(content, "acctbkitem_istransferable"))) {
					pkg = new PkgUse();
					pkg.setBossId(acrelation_revision);
					pkg.setName(pkgName);
					pkg.setFlag(9);
					pkg.setUse(this.config.getChildText(content, "acctbkitem_istransferable"));
					pkg.setRemain(this.config.getChildText(content, "acctbkitem_usage_type"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse()) + Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
				// GPRS
				if (!"".equals(this.config.getChildText(content, "acctbkitem_default_flag"))) {
					pkg = new PkgUse();
					pkg.setBossId(acrelation_revision);
					pkg.setName(pkgName);
					pkg.setFlag(9);
					pkg.setUse(this.config.getChildText(content, "acctbkitem_default_flag"));
					pkg.setRemain(this.config.getChildText(content, "acctbkitem_invprn_flag"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse()) + Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
			}
			res.setPkgUse(list);
		} catch (Exception e) {
			logger.error(e, e);
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
	public void queryRelationGroup(String accessId, ServiceConfig config, List<RequestParameter> params, QRY050001Result res, String id_type, String relationCode) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		RelationNum dt = null;
		RequestParameter r = null;
		List<RelationNum> list = null;
		boolean idType = true;
		ErrorMapping errDt = null;

		try {
			for (RequestParameter p : params) {
				if ("id_type".equals(p.getParameterName())) {
					idType = false;
					p.setParameterValue(id_type);
				}
			}
			if (idType) {
				r = new RequestParameter();
				r.setParameterName("id_type");
				r.setParameterValue(id_type);
				params.add(r);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetrelationlst_550", params);
			logger.debug(" ====== 查询亲情号码组合 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetrelationlst_550", super.generateCity(params)));
				logger.debug(" ====== 查询亲情号码组合 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050001", "cc_cgetrelationlst_550", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				if (null != res.getRelationNum() && res.getRelationNum().size() > 0) {
					list = res.getRelationNum();
				} else {
					list = new ArrayList();
				}

				RelationNum specialDt = null;
				if ("0".equals(id_type)) {
					// 插入分割标记，用来判断预约关闭后又预约开通的号码
					specialDt = new RelationNum();
					specialDt.setPhoneNum("0");
					list.add(specialDt);
				}

				List list_size = this.config.getContentList(root, "list_size");
				if (null != list_size && list_size.size() > 0) {
					for (int i = 0; i < list_size.size(); i++) {
						dt = new RelationNum();
						dt.setUserName(this.config.getChildText((Element) list_size.get(i), "msnuser_main_user_id"));
						dt.setPhoneNum(this.config.getChildText((Element) list_size.get(i), "msnuser_sub_msisdn"));
						dt.setBeginDate(this.config.dateToString(this.config
								.stringToDate(this.config.getChildText((Element) list_size.get(i), "user_apply_date"), "yyyyMMddHHmmss"), "yyyyMMdd"));
						dt.setEndDate(this.config.dateToString(this.config.stringToDate(this.config.getChildText((Element) list_size.get(i), "end_date"), "yyyyMMddHHmmss"),
								"yyyyMMdd"));
						if ("3".equals(id_type)) {
							dt.setFlag(1); // 在用
						} else {
							dt.setFlag(0); // 预约
						}
						dt.setOfferType(relationCode);
						list.add(dt);
					}
				} else {
					list.remove(specialDt);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		res.setRelationNum(list);
	}

	/**
	 * 查询用户加入的亲情组合套餐信息 cc_cgetmsngsubpack_352
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public List getRelationGroupPckInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY050001Result res) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		GetMsngSubPackDT dt = null;
		List<GetMsngSubPackDT> list = null;
		String usingPkgCode = "";
		String useState = "";
		String canOpenPkgCode = "";
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetmsngsubpack_352", params);
			logger.debug(" ====== 查询用户加入的亲情组合套餐信息 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetmsngsubpack_352", super.generateCity(params)));
				logger.debug(" ====== 查询用户加入的亲情组合套餐信息 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050001", "cc_cgetmsngsubpack_352", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List userList = this.config.getContentList(root, "package_user_id");
				if (null != userList && userList.size() > 0) {
					list = new ArrayList();
					for (int i = 0; i < userList.size(); i++) {
						Element packDt = this.config.getElement((Element) userList.get(i), "cplanpackagedt");
						if (null != packDt) {
							if ("1035".equals(this.config.getChildText(packDt, "package_type"))) {
								String package_code = this.config.getChildText(packDt, "package_code");
								if ("4778".equals(package_code) || "4779".equals(package_code) || "4907".equals(package_code)) {
									dt = new GetMsngSubPackDT();
									dt.setPackage_user_id(this.config.getChildText(packDt, "package_user_id"));
									dt.setPackage_type(this.config.getChildText(packDt, "package_type"));
									dt.setPackage_code(this.config.getChildText(packDt, "package_code"));
									dt.setPackage_use_date(this.config.dateToString(this.config
											.stringToDate(this.config.getChildText(packDt, "package_use_date"), "yyyyMMddHHmmss"), "yyyyMMdd"));
									dt.setPackage_end_date(this.config.dateToString(this.config
											.stringToDate(this.config.getChildText(packDt, "package_end_date"), "yyyyMMddHHmmss"), "yyyyMMdd"));
									dt.setPackage_state(this.config.getChildText(packDt, "package_state"));
									dt.setPackage_change_date(this.config.getChildText(packDt, "package_change_date"));
									dt.setPackage_history_srl(this.config.getChildText(packDt, "package_history_srl"));
									dt.setPackage_apply_date(this.config.getChildText(packDt, "package_apply_date"));
									dt.setPackage_level(this.config.getChildText(packDt, "package_level"));

									// 正在使用亲情号码组合
									if (Long.parseLong(this.config.getDistanceDT(dt.getPackage_use_date(), this.config.getTodayChar14(), "s")) >= 0) {
										usingPkgCode = dt.getPackage_code();
										if (dt.getPackage_end_date().length() == 0) {
											canOpenPkgCode = dt.getPackage_code();
											useState = "10";
										} else {
											useState = useState == "13" ? "12" : "11";
										}
									} else {
										// 预约亲情号码组合
										canOpenPkgCode = dt.getPackage_code();
										useState = useState == "11" ? "12" : "13";
									}

									list.add(dt);
								}
							}
						}
					}
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setUsingPkgCode(usingPkgCode);
						list.get(i).setCanOpenPkgCode(canOpenPkgCode);
						list.get(i).setUseState(useState);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return list;
	}
	
	/**
	 * 查询集团信息
	 * @param accessId
	 * @param config
	 * @param params
	 */
	public List<PkgDetail> getGroupackage (String accessId, ServiceConfig config, 
			List<RequestParameter> params, QRY050001Result res)
	{
		PkgDetail dt = null;
		String rspXml = "";
		List<PkgDetail> pkgList = null;
		List<BossParmDT> parList = null;
		BossParmDT bossDt = null;
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		ErrorMapping errDt = null;
		
		try
		{
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_cgetgroupackage_552", params), 
					 accessId, "cc_cgetgroupackage_552", this.generateCity(params)));
			logger.debug(" ====== 集团套餐返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				errDt = this.wellFormedDAO.transBossErrCode("QRY040011", "cc_cgetgroupackage_552", resp_code);
				if (null != errDt)
				{
					resp_code = errDt.getLiErrCode();
					resp_desc = errDt.getLiErrMsg();
				}
				String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
				
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (null != resp_code && ("0000".equals(resp_code)))//邵琪 2010-03-09 修改
				{
					res.setResultCode("0");
					List userList = root.getChild("content").getChildren("package_user_id");
					List nameList = root.getChild("content").getChildren("cust_name");
					
					if (null != userList && userList.size() > 0 && null != nameList && nameList.size() > 0)
					{
						pkgList = new ArrayList();
						parList = new ArrayList();
						
						for (int i = 0; i < userList.size(); i++)
						{
							if (i <= nameList.size())
							{
								Element cplanpackagedt = ((Element)userList.get(i)).getChild("cplanpackagedt");
								String mapName = ((Element)nameList.get(i)).getChildText("cust_name");
								
								if (null != cplanpackagedt && null != mapName)
								{
									if ("1035".equals(p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll(""))) {
										String package_code = p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll("");
										if ("4778".equals(package_code) || "4779".equals(package_code) || "4907".equals(package_code)) {
											dt = new PkgDetail();
											dt.setPkgId(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
											bossDt = new BossParmDT();
											long disDay = Long.parseLong(this.getDistanceDT(this.getTodayChar14(), 
													p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll("")
													, "d"));
											dt.setPkgState(disDay > 0?"预约套餐":"当前套餐");
											dt.setPkgType(mapName.trim());
											dt.setPkgName(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
											dt.setBeginDate(p.matcher(cplanpackagedt.getChildText("package_use_date")).replaceAll(""));
											dt.setEndDate(p.matcher(cplanpackagedt.getChildText("package_end_date")).replaceAll(""));
											bossDt.setParm1(p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll(""));
											parList.add(bossDt);
											pkgList.add(dt);
										}
									}
									
								}
							}
						}
						
						if (null != parList && parList.size() > 0)
						{
							this.getPackByCode(accessId, config, params, parList, pkgList, res);
						}
					}
				}
				if("-2652".equals(resp_code)){//邵琪 2010-03-09 修改
					res.setResultCode("0");
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return pkgList;
	}
	
	/**
	 * 根据套餐代码查询套餐配置信息取
	 * @param accessId
	 * @param config
	 * @param params
	 * @param parList
	 * @param pkgList
	 */
	public void getPackByCode (String accessId, ServiceConfig config, 
			List<RequestParameter> params, List<BossParmDT> parList, 
			List<PkgDetail> pkgList, QRY050001Result res)
	{
		String rspXml = "";
		String pkgCode = "";
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		ErrorMapping errDt = null;
		
		try
		{
			if (null != parList && parList.size() > 0)
			{
				RequestParameter par = new RequestParameter();
				par.setParameterName("codeCount");
				par.setParameterValue(parList);
				params.add(par);
			}
			
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(
					 this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605", params), 
					 accessId, "cc_cgetpackbycode_605", this.generateCity(params)));
			logger.debug(" ====== 查询套餐配置信息发送报文 ======\n" + this.bossTeletextUtil.mergeTeletext("cc_cgetpackbycode_605", params));
			logger.debug(" ====== 查询套餐配置信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				errDt = this.wellFormedDAO.transBossErrCode("QRY040011", "cc_cgetpackbycode_605", resp_code);
				if (null != errDt)
				{
					resp_code = errDt.getLiErrCode();
					resp_desc = errDt.getLiErrMsg();
				}
				String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
				
				res.setResultCode(resultCode);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				
				if (null != resp_code && "0000".equals(resp_code))
				{
					List packList = root.getChild("content").getChildren("productbusinesspackage_package_code");
					List idList = root.getChild("content").getChildren("productbusiness_business_id");
					
					if (null != packList && packList.size() > 0)
					{
						for (int i = 0; i < packList.size(); i++)
						{
							Element pDt = ((Element)packList.get(i)).getChild("cproductbusinesspackagedt");
							if (null != pDt)
							{
								pkgCode = p.matcher(pDt.getChildText("productbusinesspackage_package_code")).replaceAll("");
								for (PkgDetail pkg : pkgList)
								{
									if (pkgCode.equals(pkg.getPkgId()))
									{
										pkg.setPkgName(p.matcher(pDt.getChildText("productbusinesspackage_package_name")).replaceAll(""));
										pkg.setFeeDesc(p.matcher(pDt.getChildText("productbusinesspackage_package_desc")).replaceAll(""));
									}
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	    
    /**
	 * 返回 年月日小时分秒
	 * @return
	 */
	public static String getTodayChar14(){
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
	}
	/**
	 * 比对两个时间间隔
	 * @param startDateTime 开始时间
	 * @param endDateTime 结束时间
	 * @param distanceType 计算间隔类型 天d 小时h 分钟m 秒s
	 * @return
	 */
    public static String getDistanceDT(String startDateTime,String endDateTime,String distanceType){
        String strResult="";
        long lngDistancVal=0;
        try{
            SimpleDateFormat tempDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date startDate=tempDateFormat.parse(startDateTime);
            Date endDate=tempDateFormat.parse(endDateTime);
            if(distanceType.equals("d")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            }else if(distanceType.equals("h")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
            }else if(distanceType.equals("m")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000 * 60);
            }else if(distanceType.equals("s")){
            	lngDistancVal = (endDate.getTime() - startDate.getTime()) / (1000);
            }
            strResult=String.valueOf(lngDistancVal);
        }catch(Exception e){
        	strResult="0";
        }
        return strResult;
    }
}
