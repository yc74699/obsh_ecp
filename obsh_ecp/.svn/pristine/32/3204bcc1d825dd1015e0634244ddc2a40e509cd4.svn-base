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
import com.xwtech.xwecp.service.logic.pojo.BossParmDT;
import com.xwtech.xwecp.service.logic.pojo.DEL010001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 移动彩讯 DEL010001通用办理类接口
 * 
 * @author yuantao 2010-01-18
 */
public class TransctYdcxInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(TransctYdcxInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public TransctYdcxInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL010001Result res = new DEL010001Result();
		String oprType = ""; // 操作类型 1 开通 2 关闭 3 变更

		try {
			res.setResultCode("0"); // 成功

			for (RequestParameter par : params) {
				// 获取操作类型
				if ("oprType".equals(par.getParameterName())) {
					oprType = String.valueOf(par.getParameterValue());
				}
			}

			// 开通梦网业务（移动彩讯）
			if ("1".equals(oprType)) {
				this.openDealMonternet(accessId, config, params, res);
			} else // 关闭梦网业务（移动彩讯）
			{
				// 新增参数 operatingtointerboss_operating_type 操作编码 7：开通：8 关闭
				RequestParameter par = new RequestParameter();
				par.setParameterName("operating_type");
				par.setParameterValue("8");
				params.add(par);
				// 用户订购关系变更 关闭
				this.closeMonternetBusiness(accessId, config, params, res);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}

	/**
	 * 开通移动彩讯
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void openDealMonternet(String accessId, ServiceConfig config, List<RequestParameter> params, DEL010001Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		String resp_code = ""; // 返回错误编码
		List<BossParmDT> parList = null; // BOSS实现List
		String id = ""; // 业务编码

		try {
			// 根据业务编码获取其子业务所对应的BOSS实现
			for (RequestParameter r : params) {
				if ("id".equals(r.getParameterName())) {
					id = String.valueOf(r.getParameterValue()); // 获取业务编码
					// 根据业务编码获取BOSS实现（包含子业务）
					parList = this.wellFormedDAO.getSubBossParmList(id);
				}
			}

			if (null != parList && parList.size() > 0) {
				// 新增参数List
				RequestParameter par = new RequestParameter();
				par.setParameterName("codeCount");
				par.setParameterValue(parList);
				params.add(par);
			}

			// 组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cdealmonternet_72", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				// 发送并接收报文
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cdealmonternet_72", super.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code,root);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 关闭移动彩讯
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void closeMonternetBusiness(String accessId, ServiceConfig config, List<RequestParameter> params, DEL010001Result res) {
		String reqXml = ""; // 发送报文
		String rspXml = ""; // 接收报文
		String resp_code = ""; // 返回错误编码

		try {
			// 组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchgspuserreg_70_SJB", params);
			logger.debug(" ====== 发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				// 发送并接收报文
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchgspuserreg_70_SJB", super.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				// 获取错误编码
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				// 设置结果信息
				this.getErrInfo(accessId, config, params, res, resp_code,root);
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
	 * @param root 
	 */
	public void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, DEL010001Result res, String resp_code, Element root) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				// 解析错误信息
				res.setErrorCode(root.getChild("response").getChildText("resp_code"));
				res.setErrorMessage(root.getChild("response").getChildText("resp_desc"));
				errDt = this.wellFormedDAO.transBossErrCode("DEL010001", "cc_cdealmonternet_72", resp_code);
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
