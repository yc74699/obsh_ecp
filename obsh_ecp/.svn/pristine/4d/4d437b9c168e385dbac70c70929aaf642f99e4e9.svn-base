package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QurUserPwdLmtInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(GetMusicInfoInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	// private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	// private ParseXmlConfig config;

	/**
	 * 查询客户资料接口
	 */
	private static String USER_INFO_BOSS_INTERFACE = "cc_cgetusercust_69";

	/**
	 * 密码重置保护接口
	 */
	private static String MMCZBH_BOSS_INTERFACE = "cc_cquruserpwdlmt_553";

	public QurUserPwdLmtInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx
				.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx
				.getBean("wellFormedDAO"));
		// this.config = new ParseXmlConfig();

	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		QRY020001Result result = new QRY020001Result();
		result.setResultCode(LOGIC_SUCESS);

		try {
			// 查询客户资料接口,获取用户
			String rspXml = (String) this.remote.callRemote(new StringTeletext(
					this.bossTeletextUtil.mergeTeletext(
							USER_INFO_BOSS_INTERFACE, params), accessId,
					USER_INFO_BOSS_INTERFACE, this.generateCity(params)));
			String userid = getUserId4Xml(rspXml,
					"/operation_out/content/user_info/user_id");
			String city = getUserId4Xml(rspXml,
					"/operation_out/content/user_info/user_city");

			// 加参数到报文体
			RequestParameter rp = new RequestParameter();
			rp.setParameterName("context_user_id");
			rp.setParameterValue(userid);
			params.add(rp);

			rp = new RequestParameter();
			rp.setParameterName(ServiceExecutor.ServiceConstants.USER_CITY
					+ "222");
			rp.setParameterValue(city);
			params.add(rp);

			// 加参数到报文头：params当中已经有该参数只能修改它的值
			this.setParameter(params,
					ServiceExecutor.ServiceConstants.USER_CITY, city);

			// 开始调用,
			String reqXML = this.bossTeletextUtil.mergeTeletext(
					MMCZBH_BOSS_INTERFACE, params);
			logger.debug(" ====== 查询密码重置保护信息请求报文 ======\n" + reqXML);
			rspXml = (String) this.remote
					.callRemote(new StringTeletext(reqXML, accessId,
							MMCZBH_BOSS_INTERFACE, this.generateCity(params)));
			logger.debug(" ====== 查询密码重置保护信息响应报文 ======\n" + rspXml);

			Document doc = DocumentHelper.parseText(rspXml);
			String errorCode = doc.selectSingleNode(
					"/operation_out/response/resp_code").getText();
			String errorMessage = doc.selectSingleNode(
					"/operation_out/response/resp_desc").getText();
			result.setErrorCode(errorCode);
			result.setErrorMessage(errorMessage);
			Element root = this.getElement(rspXml.getBytes());
			if (BOSS_SUCCESS.equals(errorCode)) {
				result.setResultCode(LOGIC_SUCESS);
				XPath xpath = XPath
						.newInstance("/operation_out/content/cuserpwdlimitdt");
				List<Element> list = (List<Element>) xpath.selectNodes(root);
				List<GommonBusiness> reLists = new ArrayList();
				for (Element element : list) {

					GommonBusiness busBean = new GommonBusiness();
					busBean.setBeginDate(element
							.getChildTextTrim("userpwdlimit_join_date"));
					busBean.setId(element
							.getChildTextTrim("userpwdlimit_operator_id"));
					reLists.add(busBean);

				}
				result.setGommonBusiness(reLists);
				//处理WAP返回resultCode为0
			} else if(LOGIC_SUCESS.equals(errorCode)){
				result.setResultCode(LOGIC_SUCESS);
				result.setErrorCode(BOSS_SUCCESS);
				XPath xpath = XPath
						.newInstance("/operation_out/content/cuserpwdlimitdt");
				List<Element> list = (List<Element>) xpath.selectNodes(root);
				List<GommonBusiness> reLists = new ArrayList();
				for (Element element : list) {

					GommonBusiness busBean = new GommonBusiness();
					busBean.setBeginDate(element
							.getChildTextTrim("userpwdlimit_join_date"));
					busBean.setId(element
							.getChildTextTrim("userpwdlimit_operator_id"));
					reLists.add(busBean);

				}
				result.setGommonBusiness(reLists);
			}
			else {
				ErrorMapping errDt = this.wellFormedDAO.transBossErrCode(
						"QRY020001", MMCZBH_BOSS_INTERFACE, errorCode);
				if (null != errDt) {
					result.setErrorCode(errDt.getLiErrCode());
					result.setErrorCode(errDt.getLiErrMsg());
				} else {
					result.setResultCode(LOGIC_ERROR);
				}
			}

			// 设置响应
			GommonBusiness dt = null;
			List<GommonBusiness> list = null;
			List<GommonBusiness> reList = new ArrayList();
			list = result.getGommonBusiness();
			// 已开通
			if (null != list && list.size() > 0) {
				dt = (GommonBusiness) list.get(0);
				dt.setId("MMFW_MMCZBH"); // 密码修改白名单查询
				dt.setName("密码重置保护"); // 业务名称
				dt.setState(2); // 已开通
			} else // 未开通
			{
				dt = new GommonBusiness();
				dt.setId("MMFW_MMCZBH"); // 密码修改白名单查询
				dt.setName("密码重置保护"); // 业务名称
				dt.setState(1); // 未开通
			}
			reList.add(dt);
			result.setGommonBusiness(reList); // 设置返回参数

		} catch (Exception e) {
			logger.error(e, e);
		}

		return result;
	}

	/**
	 * 获取用户ID
	 * 
	 * @param rspXml
	 * @return
	 * @throws DocumentException
	 */
	private String getUserId4Xml(String rspXml, String nodes)
			throws DocumentException {
		String city = null;
		Document doc = DocumentHelper.parseText(rspXml);
		List<Node> nodeList = doc.selectNodes(nodes);
		for (Node node : nodeList) {
			city = node.getText();
		}
		return city;
	}
}
