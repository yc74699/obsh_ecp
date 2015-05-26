package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.PkgUse;
import com.xwtech.xwecp.service.logic.pojo.PkgUseState;
import com.xwtech.xwecp.service.logic.pojo.QRY020003Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 套餐使用情况查询 QRY020003 (语音套餐|动感地带套餐使用情况查询)
 * 
 * @author yuantao 2010-1-18
 */
public class QueryPkgUseStateInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryPkgUseStateInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Map<String,Integer> map;

	private ParseXmlConfig config;

	// 新全包大类1049(含旧大类)
	private static final String[] GPRS_PACKAGE_TYPES_NEW = { "1049", "1046", "1047", "1048" };

	// 新欢乐送子代码
	private static final String[] NEW_HAPPY_PRESENT_1 = { "1604" };

	public QueryPkgUseStateInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();

		if (null == this.map) {
			this.map = new HashMap<String,Integer>();
			this.map.put("390", 7);
			this.map.put("787", 4);
			this.map.put("788", 8);
		}
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY020003Result res = new QRY020003Result();
		List<PkgUseState> reList = new ArrayList<PkgUseState>();
		RequestParameter r = null;
		Date now = new Date();
		String pkgId = "";

		try {
			res.setResultCode("0");
			res.setErrorMessage("");

			for (RequestParameter par : params) {
				// 获取业务编码
				if ("pkgId".equals(par.getParameterName())) {
					pkgId = String.valueOf(par.getParameterValue());
				}
			}
			// 动感地带套餐使用情况查询
			if ("TCSYQK_DGDDTCSYQKCX".equalsIgnoreCase(pkgId)) {
				// 新增查询方式
				r = new RequestParameter();
				r.setParameterName("biz_pkg_qry_scope"); // 查询方式
				r.setParameterValue("1"); // 在用套餐
				params.add(r);

				// 查询动感地带必选套餐信息
				this.queryPersonalPackage(accessId, config, params, "1018", reList, res);
				// 查询动感地带可选套餐信息
				this.queryPersonalPackage(accessId, config, params, "1019", reList, res);
				// 动感融合无限50元B套餐
				this.queryPersonalPackage(accessId, config, params, "1063", reList, res);

				if (null != reList && reList.size() > 0) {
					// 新增参数
					r = new RequestParameter();
					r.setParameterName("dbi_month_pr_number"); // 月份
					r.setParameterValue(this.config.getTodayChar6()); // 在用套餐
					params.add(r);
					r = new RequestParameter();
					r.setParameterName("cdr_reduce_total");
					r.setParameterValue("1");
					params.add(r);
					r = new RequestParameter();
					r.setParameterName("a_bg_bill_month"); // 半月标志
					r.setParameterValue("1");
					params.add(r);

					for (PkgUseState pkgUseState : reList) {
						// 获取套餐优惠信息
						this.getAcAGetZoneInfo(accessId, config, params, pkgUseState.getPkgId(), pkgUseState, pkgUseState.getPkgName(), res);

					}
				}
			} else if ("TCSYQK_YYTCCX".equalsIgnoreCase(pkgId)) // 语音套餐
			{
				// 新增查询方式
				r = new RequestParameter();
				r.setParameterName("biz_pkg_qry_scope"); // 查询方式
				r.setParameterValue("5"); // 在用和预约套餐
				params.add(r);

				// 套餐大类1022
				this.queryPersonalPackage(accessId, config, params, "1022", reList, res);
				// 套餐大类1031
				this.queryPersonalPackage(accessId, config, params, "1031", reList, res);

				if (null != reList && reList.size() > 0) {
					for (PkgUseState pkg : reList) {
						// 预约套餐
						if (now.before(new SimpleDateFormat("yyyyMM").parse(pkg.getBeginDate().substring(0, 6)))) {
							continue;
						}
						// 语音套餐查看
						this.doAcAGetFreeItem(accessId, config, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), pkg, res);
					}
				}
			} else if ("TCSYQK_DGDDQLTCCX".equals(pkgId)) // 动感地带情侣套餐查询
			{
				// 新增查询方式
				r = new RequestParameter();
				r.setParameterName("biz_pkg_qry_scope"); // 查询方式
				r.setParameterValue("1"); // 在用套餐
				params.add(r);
				// 套餐大类1019
				this.queryPersonalPackage(accessId, config, params, "1019", reList, res);

				// 过滤已办理动感地带情侣套餐
				if (null != reList && reList.size() >= 0) {
					for (int i = reList.size() - 1; i > 0; i--) {
						if (!"4817".equals(((PkgUseState) reList.get(i)).getPkgId())) {
							reList.remove(i);
						}
					}
				}
				if (null != reList && reList.size() >= 0) {
					for (PkgUseState pkg : reList) {
						// 语音套餐查看
						this.doAcAGetFreeItem(accessId, config, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), pkg, res);
					}
				}
			} else if ("TCSYQK_GNYDSJTCCX".equals(pkgId)) // 国内移动数据套餐查询
			{
				// 新增查询方式
				r = new RequestParameter();
				r.setParameterName("biz_pkg_qry_scope"); // 查询方式
				r.setParameterValue("5"); // 在用和预约套餐
				params.add(r);

				// 套餐大类0
				this.queryPersonalPackage(accessId, config, params, "0", reList, res);
				// 筛选套餐
				if (null != reList && reList.size() > 0) {
					for (int i = reList.size() - 1; i >= 0; i--) {
						if (!(ArrayUtils.indexOf(GPRS_PACKAGE_TYPES_NEW, String.valueOf(((PkgUseState) reList.get(i)).getPkgType())) >= 0)) {
							reList.remove(i);
						}
					}
				}
				if (null != reList && reList.size() > 0) {
					for (PkgUseState pkg : reList) {
						// 语音套餐查看
						this.doAcAGetFreeItem(accessId, config, params, new SimpleDateFormat("yyyyMM").format(new Date()), pkg.getPkgId(), pkg.getPkgName(), pkg, res);
					}
				}
			} else if ("TCSYQK_XHLSSYQKCX".equals(pkgId)) // 新欢乐送使用情况查询
			{
				// 新增查询方式
				r = new RequestParameter();
				r.setParameterName("biz_pkg_qry_scope"); // 查询方式
				r.setParameterValue("5"); // 在用和预约套餐
				params.add(r);
				// 套餐大类1013
				this.queryPersonalPackage(accessId, config, params, "1013", reList, res);
				// 筛选套餐 新欢乐送
				if (null != reList && reList.size() > 0) {
					for (int i = reList.size() - 1; i >= 0; i--) {
						if (!(ArrayUtils.indexOf(NEW_HAPPY_PRESENT_1, String.valueOf(((PkgUseState) reList.get(i)).getPkgId())) >= 0)) {
							reList.remove(i);
						}
					}
				}
				if (null != reList && reList.size() > 0) {
					for (PkgUseState pkg : reList) {
						// 查询欢乐送使用情况
						this.doAcAGetFreeItem(accessId, config, params, new SimpleDateFormat("yyyyMM").format(new Date()), "1605", pkg.getPkgName(), pkg, res);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		res.setPkgUseState(reList);
		return res;
	}

	/**
	 * 套餐优惠信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param acrelation_revision
	 * @param res
	 * @param pkgName
	 */
	public void getAcAGetZoneInfo(String accessId, ServiceConfig config, List<RequestParameter> params, String acrelation_revision, PkgUseState pkgUseState, String pkgName,
			QRY020003Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		RequestParameter par = null; // 参数
		String resp_code = ""; // 返回编码
		PkgUse pkg = null; // 套餐使用情况
		List<PkgUse> list = null; // 套餐使用情况列表

		try {
			// 替换业务编码
			boolean codeBoolean = true;
			for (RequestParameter p : params) {
				// 已存在
				if ("acrelation_revision".equals(p.getParameterName())) {
					codeBoolean = false;
					p.setParameterValue(acrelation_revision);
				}
			}
			if (codeBoolean) {
				// 新增参数
				par = new RequestParameter();
				par.setParameterName("acrelation_revision");
				par.setParameterValue(acrelation_revision);
				params.add(par);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetzoneinfo_518", params);
			logger.debug(" ====== 动感套餐优惠查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetzoneinfo_518", super.generateCity(params)));
				logger.debug(" ====== 动感套餐优惠查询 接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "ac_agetzoneinfo_518");

				if ("0000".equals(resp_code)) {
					list = new ArrayList<PkgUse>();
					Element content = this.config.getElement(root, "content");
					// 短信
					if (!"".equals(this.config.getChildText(content, "arecord_count"))) {
						pkg = new PkgUse();
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
						pkg.setName(pkgName);
						pkg.setFlag(9);
						pkg.setUse(this.config.getChildText(content, "acctbkitem_default_flag"));
						pkg.setRemain(this.config.getChildText(content, "acctbkitem_invprn_flag"));
						pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse()) + Integer.parseInt(pkg.getRemain())));
						list.add(pkg);
					}
				}
			}
			pkgUseState.setPkgUse(list);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 语音套餐查看
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param queryDate
	 * @param packageCode
	 */
	public void doAcAGetFreeItem(String accessId, ServiceConfig config, List<RequestParameter> params, String queryDate, String packageCode, String pkgName, PkgUseState pkg,
			QRY020003Result res) {
		String reqXml = "";
		String rspXml = "";
		RequestParameter par = null;
		PkgUse pkgUse = null;
		List<PkgUse> useList = null;
		ErrorMapping errDt = null;

		try {
			boolean booleanDate = true;
			boolean booleanCode = true;
			for (RequestParameter p : params) {
				if ("dbi_month".equals(p.getParameterName())) {
					booleanDate = false;
					p.setParameterValue(queryDate);
				}
				if ("a_package_id".equals(p.getParameterName())) {
					booleanCode = false;
					p.setParameterValue(packageCode);
				}
			}
			if (booleanDate) {
				par = new RequestParameter();
				par.setParameterName("dbi_month");
				par.setParameterValue(queryDate);
				params.add(par);
			}
			if (booleanCode) {
				par = new RequestParameter();
				par.setParameterName("a_package_id");
				par.setParameterValue(packageCode);
				params.add(par);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetfreeitem_517", params);
			logger.debug(" ====== 语音套餐查看 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetfreeitem_517", super.generateCity(params)));
				logger.debug(" ====== 语音套餐查看 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY020003", "ac_agetfreeitem_517", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (null != resp_code && "0000".equals(resp_code)) {
					List freeitemList = this.getContentList(root, "freeitem_dt");
					if (null != freeitemList && freeitemList.size() > 0) {
						useList = new ArrayList<PkgUse>();
						for (int i = 0; i < freeitemList.size(); i++) {
							pkgUse = new PkgUse();
							// 优惠描述
							pkgUse.setName(pkgName);
							// 总量
							String strTotal = this.getChildText((Element) freeitemList.get(i), "a_freeitem_total_value");
							long total = "".equals(strTotal) ? 0 : Long.parseLong(strTotal);
							pkgUse.setTotal(strTotal);
							// 已使用情况
							String strUse = this.getChildText((Element) freeitemList.get(i), "a_freeitem_value");
							long use = "".equals(strUse) ? 0 : Long.parseLong(strUse);
							pkgUse.setUse(strUse);
							// 剩余使用情况
							pkgUse.setRemain(String.valueOf(total - use));
							// 标识位：用以表示使用情况的表示的是时间，还是短信条数等
							String a_freeitem_id = this.getChildText((Element) freeitemList.get(i), "a_freeitem_id");
							pkgUse.setFlag(this.map.get(a_freeitem_id) == null ? 0 : Integer.parseInt(String.valueOf(this.map.get(a_freeitem_id))));
							useList.add(pkgUse);
						}
						pkg.setPkgUse(useList);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 查询语音套餐
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param package_type
	 * @param reList
	 */
	public void queryPersonalPackage(String accessId, ServiceConfig config, List<RequestParameter> params, String package_type, List<PkgUseState> reList, QRY020003Result res) {
		String reqXml = "";
		String rspXml = "";
		RequestParameter par = null;
		PkgUseState dt = null;
		ErrorMapping errDt = null;

		try {
			// 修改套餐大类
			boolean booleanType = true;
			for (RequestParameter p : params) {
				if ("package_type".equals(p.getParameterName())) {
					booleanType = false;
					p.setParameterValue(package_type);
				}
			}
			if (booleanType) {
				par = new RequestParameter();
				par.setParameterName("package_type");
				par.setParameterValue(package_type);
				params.add(par);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_YYTC", params);
			logger.debug(" ====== 语音套餐 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_find_package_62_YYTC", super.generateCity(params)));
				logger.debug(" ====== 语音套餐 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				res.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY020003", "cc_find_package_62_YYTC", resp_code);
					if (null != errDt) {
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
				if (null != resp_code && "0000".equals(resp_code)) {
					List packList = this.getContentList(root, "package_code");
					if (null != packList && packList.size() > 0) {
						for (int i = 0; i < packList.size(); i++) {
							Element packageDt = this.getElement((Element) packList.get(i), "cplanpackagedt");
							if (null != packageDt) {
								dt = new PkgUseState();
								// 套餐id
								dt.setPkgId(this.getChildText(packageDt, "package_code"));
								// 套餐名称
								dt.setPkgName(this.getChildText(packageDt, "package_name"));
								// 套餐描述
								dt.setPkgDesc("");
								// 套餐类型 this.getChildText(packageDt, "package_type")
								dt.setPkgType(Integer.parseInt(this.getChildText(packageDt, "package_type")));
								// dt.setPkgType(0);
								// 开始日期
								dt.setBeginDate(this.getChildText(packageDt, "package_use_date"));
								// 结束日期
								dt.setEndDate(this.getChildText(packageDt, "package_end_date"));
								reList.add(dt);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 获取content下父节点信息
	 * 
	 * @param root
	 * @param name
	 * @return
	 */
	public List getContentList(Element root, String name) {
		List list = null;
		try {
			list = root.getChild("content").getChildren(name);
		} catch (Exception e) {
			list = null;
		}
		return list;
	}

	/**
	 * 获取子节点
	 * 
	 * @param e
	 * @param name
	 * @return
	 */
	public Element getElement(Element e, String name) {
		Element dt = null;
		try {
			dt = e.getChild(name);
		} catch (Exception ex) {
			dt = null;
		}
		return dt;
	}

	/**
	 * 获取子节点信息
	 * 
	 * @param e
	 * @param childName
	 * @return
	 */
	public String getChildText(Element e, String childName) {
		String str = "";

		try {
			str = e.getChildText(childName) == null ? "" : e.getChildText(childName).trim();
		} catch (Exception ex) {
			// logger.error(ex, ex);
			str = "";
		}

		return str;
	}

	/**
	 * 解析报文
	 * 
	 * @param tmp
	 * @return
	 */
	public Element getElement(byte[] tmp) {
		Element root = null;
		try {
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return root;
	}

	/**
	 * 设置结果信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param resp_code
	 */
	public void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY020003Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY020003", xmlName, resp_code);
				if (null != errDt) {
					res.setErrorCode(errDt.getLiErrCode()); // 设置错误编码
					res.setErrorMessage(errDt.getLiErrMsg()); // 设置错误信息
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
