package com.xwtech.xwecp.service.logic.invocation;

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
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 个人业务办理 情侣短信套餐 DEL010001
 * 
 * @author 袁涛 2010-01-27
 */
public class UserPkgInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(UserPkgInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public UserPkgInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL010001Result res = new DEL010001Result();
		String oprType = ""; // 操作类型
		String chooseFlag = ""; // 生效方式
		String reserve1 = ""; // 情侣号码

		try {
			for (RequestParameter p : params) {
				if ("oprType".equals(p.getParameterName())) {
					oprType = String.valueOf(p.getParameterValue());
				}
				if ("chooseFlag".equals(p.getParameterName())) {
					chooseFlag = String.valueOf(p.getParameterValue());
				}
				if ("reserve1".equals(p.getParameterName())) {
					reserve1 = String.valueOf(p.getParameterValue());
				}
			}

			// 办理个人业务
			this.operateUserPackageBusiness(accessId, config, params, res, oprType, chooseFlag, reserve1);
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}

	/**
	 * 办理个人业务
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param oprType
	 * @param chooseFlag
	 * @param reserve1
	 */
	public void operateUserPackageBusiness(String accessId, ServiceConfig config, List<RequestParameter> params, DEL010001Result res, String oprType, String chooseFlag,
			String reserve1) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		String resp_code = ""; // 返回错误编码
		RequestParameter par = null; // 参数

		try {
			if ("2".equals(oprType)) // 关闭
			{
				par = new RequestParameter();
				par.setParameterName("type"); // 操作类型
				par.setParameterValue("1");
				params.add(par);
				par = new RequestParameter();
				par.setParameterName("package_inure_mode"); // 生效方式
				par.setParameterValue("3");
				params.add(par);
				par = new RequestParameter();
				par.setParameterName("package_level");
				par.setParameterValue("");
				params.add(par);
			} else if ("1".equals(oprType)) // 开通
			{
				par = new RequestParameter();
				par.setParameterName("type"); // 操作类型
				par.setParameterValue("0");
				params.add(par);
				par = new RequestParameter();
				par.setParameterName("package_inure_mode"); // 生效方式
				par.setParameterValue(chooseFlag);
				params.add(par);
				par = new RequestParameter();
				par.setParameterName("reserve");
				par.setParameterValue(reserve1);
				params.add(par);
				par = new RequestParameter();
				par.setParameterName("package_level");
				par.setParameterValue("3");
				params.add(par);
			}

			// 组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_userpkg_818", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				// 发送并接收报文
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_userpkg_818", super.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 错误描述
				String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");

				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_userpkg_818");
			}
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
	public void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, DEL010001Result res, String resp_code, String resp_desc, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				errDt = this.wellFormedDAO.transBossErrCode("DEL010001", xmlName, resp_code);
				if (null != errDt) {
					resp_code = errDt.getLiErrCode(); // 设置错误编码
					resp_desc = errDt.getLiErrMsg(); // 设置错误信息
				}
			}
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");
			res.setErrorCode(resp_code); // 设置错误编码
			res.setErrorMessage(resp_desc); // 设置错误信息
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}