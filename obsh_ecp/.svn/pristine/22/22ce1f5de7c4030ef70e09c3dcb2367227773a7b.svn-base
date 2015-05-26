package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;

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
import com.xwtech.xwecp.service.logic.pojo.FamilyNumber;
import com.xwtech.xwecp.service.logic.pojo.ProPackAgeInfo;
import com.xwtech.xwecp.service.logic.pojo.QRY020009Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 省内亲情号码查询
 * 
 * @author yuantao 2010-01-21
 */
public class QueryProAffectionInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryPkgUseStateInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	/**
	 * 新大陆提供的密钥，需要每两位转成1个字节
	 */
	private static byte[] BOSS_SECRET_KEY = { 0x0b, 0x33, (byte) 0xe7, (byte) 0xb2, 0x51, 0x0d, 0x75, (byte) 0xc3, 0x4e, (byte) 0xdd, (byte) 0x3b, (byte) 0x51, 0x24, 0x36,
			(byte) 0xa8, (byte) 0x28, 0x0b, 0x33, (byte) 0xe7, (byte) 0xb2, 0x51, 0x0d, 0x75, (byte) 0xc3 };

	public QueryProAffectionInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY020009Result res = new QRY020009Result();
		res.setResultCode("0"); // 成功

		try {
			for (RequestParameter param : params) {
				if ("context_login_msisdn".equals(param.getParameterName())) {
					RequestParameter p = new RequestParameter();
					p.setParameterName("login_msisdn");
					p.setParameterValue(param.getParameterValue());
					params.add(p);
					break;
				}
			}

			// 需要用两个循环否则会抛出java.util.ConcurrentModificationException异常
			for (RequestParameter param : params) {
				if ("usePwd".equals(param.getParameterName())) {
					String parameterValue = (String) param.getParameterValue();

					RequestParameter p = new RequestParameter();
					p.setParameterName("pwd_10086");
					p.setParameterValue(parameterValue);
					params.add(p);
					break;
				}
			}

			// 省内亲情号码查询
			this.queryProFamailyMsisdn(accessId, config, params, res);
			// 存在亲情号码信息
			if (null != res.getFamilyNumber() && res.getFamilyNumber().size() > 0) {
				boolean isUserd = false; // 使用状况
				for (FamilyNumber dt : res.getFamilyNumber()) {
					// 对比开始时间
					String distanc = this.config.getDistanceDT(this.config.getTodayChar14(), dt.getBeginDate(), "s");
					if (Long.parseLong(distanc) <= 0) {
						isUserd = true;
						break;
					}
				}
				// 正在使用
				if (isUserd) {
					// 当前日期
					String queryDate = this.config.getTodayChar6();
					// 优惠信息查询
					this.doAcAGetFreeItem(accessId, config, params, queryDate, "1840", res);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
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
	public void doAcAGetFreeItem(String accessId, ServiceConfig config, List<RequestParameter> params, String queryDate, String packageCode, QRY020009Result res) {
		String reqXml = "";
		String rspXml = "";
		RequestParameter par = null;
		ProPackAgeInfo proDt = null;

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
				Element root = this.config.getElement(rspXml);
				String resp_code = root.getChild("response").getChildText("resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cqryprovfamily_419");
				// 成功
				if (null != resp_code && "0000".equals(resp_code)) {
					List<Element> freeitemList = this.config.getContentList(root, "freeitem_dt");
					if (null != freeitemList && freeitemList.size() > 0) {
						Element e = (Element) freeitemList.get(0);
						proDt = new ProPackAgeInfo();
						// 优惠金额/分钟
						proDt.setTotal(this.config.getChildText(e, "a_freeitem_total_value"));
						// 已经优惠金额/分钟
						proDt.setUse(this.config.getChildText(e, "a_freeitem_value"));
						// 剩余优惠金额/分钟
						proDt.setLeft(String.valueOf(Long.parseLong(proDt.getTotal()) - Long.parseLong(proDt.getUse())));
						res.setProPackAgeInfo(proDt);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 省内亲情号码查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void queryProFamailyMsisdn(String accessId, ServiceConfig config, List<RequestParameter> params, QRY020009Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		String resp_code = ""; // 返回码
		FamilyNumber dt = null; // 亲情号码信息
		List<FamilyNumber> list = new ArrayList(); // 亲情号码列表

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryprovfamily_419", params);
			logger.debug(" ====== 查询用户在用/预约的所有省内亲情 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqryprovfamily_419", super.generateCity(params)));
				logger.debug(" ====== 查询用户在用/预约的所有省内亲情 接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cqryprovfamily_419");
				// 成功
				if ("0000".equals(resp_code)) {
					List<Element> familyList = this.config.getContentList(root, "family_dt");
					if (null != familyList && familyList.size() > 0) {
						for (Element e : familyList) {
							dt = new FamilyNumber();
							dt.setName("国内亲情号码组合"); // 套餐名称
							// 对方号码
							dt.setFamilyMsisdn(this.config.getChildText(e, "family_msisdn"));
							// 开始日期
							dt.setBeginDate(this.config.getChildText(e, "family_Start_date"));
							// 结束日期
							dt.setEndDate(this.config.getChildText(e, "family_End_date"));
							list.add(dt);
						}
					}
				}
			}
			res.setFamilyNumber(list);
		} catch (Exception e) {
			logger.error(e, e);
		}
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
	public void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY020009Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY020009", xmlName, resp_code);
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
