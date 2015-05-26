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
import com.xwtech.xwecp.service.logic.pojo.PkgUseState;
import com.xwtech.xwecp.service.logic.pojo.QRY050021Result;
import com.xwtech.xwecp.service.logic.pojo.User17202;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 17202上网直通车 QRY050021
 * 
 * @author yuantao 2010-01-21
 */
public class QueryUser17202Invocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryPkgUseStateInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryUser17202Invocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY050021Result res = new QRY050021Result();
		RequestParameter r = null; // 新增参数
		res.setResultCode("0"); // 成功
		List<PkgUseState> reList = null;

		try {
			// 查询17202上网直通车
			this.ccCGetUser172(accessId, config, params, res);
			// 存在信息
			if (null != res.getUser17202() && res.getUser17202().size() > 0) {
				// 新增查询方式
				r = new RequestParameter();
				r.setParameterName("biz_pkg_qry_scope"); // 查询方式
				r.setParameterValue("0"); // 在用和预约套餐
				params.add(r);
				reList = new ArrayList();
				// 查询个人套餐信息
				this.queryPersonalPackage(accessId, config, params, "1024", reList, res);
				if (null != reList && reList.size() > 0) {
					// 业务编码
					String pkgCode = "";
					for (PkgUseState pkgDt : reList) {
						if ("0".equals(pkgDt.getPkgDesc())) {
							pkgCode = pkgDt.getPkgId();
							break;
						}
					}
					// 存在业务编码
					if (!"".equals(pkgCode)) {
						for (User17202 userDt : res.getUser17202()) {
							// 设置业务编码
							userDt.setPkgCode(pkgCode);
							// 设置套餐类型
							userDt.setPkgType("3006".equals(userDt.getPkgCode()) ? "1" : ("4151".equals(userDt.getPkgCode()) ? "2" : "3"));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
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
	public void queryPersonalPackage(String accessId, ServiceConfig config, List<RequestParameter> params, String package_type, List<PkgUseState> reList, QRY050021Result res) {
		String reqXml = "";
		String rspXml = "";
		RequestParameter par = null;
		PkgUseState dt = null;

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
				Element root = this.config.getElement(rspXml);
				String resp_code = root.getChild("response").getChildText("resp_code");
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				if (null != resp_code && "0000".equals(resp_code)) {
					List packList = this.config.getContentList(root, "package_code");
					if (null != packList && packList.size() > 0) {
						for (int i = 0; i < packList.size(); i++) {
							Element packageDt = this.config.getElement((Element) packList.get(i), "cplanpackagedt");
							if (null != packageDt) {
								dt = new PkgUseState();
								// 套餐id
								dt.setPkgId(this.config.getChildText(packageDt, "package_code"));
								// 套餐名称
								dt.setPkgName(this.config.getChildText(packageDt, "package_name"));
								// 套餐描述 (状态)
								dt.setPkgDesc(this.config.getChildText(packageDt, "package_state"));
								// 套餐类型 this.getChildText(packageDt, "package_type")
								dt.setPkgType(Integer.parseInt(this.config.getChildText(packageDt, "package_type")));
								// dt.setPkgType(0);
								// 开始日期
								dt.setBeginDate(this.config.getChildText(packageDt, "package_use_date"));
								// 结束日期
								dt.setEndDate(this.config.getChildText(packageDt, "package_end_date"));
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
	 * 查询17202上网直通车
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void ccCGetUser172(String accessId, ServiceConfig config, List<RequestParameter> params, QRY050021Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		String resp_code = ""; // 返回码
		User17202 dt = null; // 套餐信息
		List<User17202> list = new ArrayList(); // 套餐列表

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetuser172_707", params);
			logger.debug(" ====== 17202上网直通车查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetuser172_707", super.generateCity(params)));
				logger.debug(" ====== 17202上网直通车查询 接收报文 ====== \n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, "cc_cgetuser172_707");
				// 成功
				if ("0000".equals(resp_code)) {
					List<Element> cuser172dt = this.config.getContentList(root, "cuser172dt");
					if (null != cuser172dt && cuser172dt.size() > 0) {
						for (Element e : cuser172dt) {
							dt = new User17202();
							// 业务编码
							dt.setPkgCode(this.config.getChildText(e, "user172_package_code"));
							// 用户密码
							dt.setPwd(this.config.getChildText(e, "user172_password"));
							// 套餐类型
							dt.setPkgType("");
							// 唯一性认证
							dt.setLimitFlag(this.config.getChildText(e, "user172_limit_flag"));
							// 状态
							dt.setStatus(this.config.getChildText(e, "user172_state"));
							list.add(dt);
						}
					}
				}
			}
			res.setUser17202(list);
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
	public void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY050021Result res, String resp_code, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("QRY050021", xmlName, resp_code);
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
