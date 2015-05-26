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
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.QRY020011Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 情侣短信套餐查询 QRY020001
 * 
 * @author yuantao 2010-01-27
 */
public class QueryLoverSMSPackageInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(QueryLoverSMSPackageInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private ParseXmlConfig config;

	public QueryLoverSMSPackageInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY020011Result res = new QRY020011Result();

		try {
			// 查询用户在使用的情侣短信套餐
			res.setOpenedBusiness(this.queryLoverSMSPackage(accessId, config, params, res, "1"));
			
			// 查询用户预约的情侣短信套餐
			res.setBookingBusiness(this.queryLoverSMSPackage(accessId, config, params, res, "3"));
			
			// 查询用户已关闭的套餐
			res.setCloseBusiness(this.queryLoverSMSPackage(accessId, config, params, res, "2"));
			
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}

	public List<GommonBusiness> queryLoverSMSPackage(String accessId, ServiceConfig config, List<RequestParameter> params, QRY020011Result res, String userspecialpackage_query_flag) {
		// 发送报文
		String reqXml = ""; 
		
		// 接收报文
		String rspXml = ""; 
		
		// 返回错误编码
		String resp_code = ""; 
		
		// 新增参数标识
		boolean flagBoolean = true; 
		
		// 参数
		RequestParameter par = null; 
		
		// 业务信息
		GommonBusiness gDt = null; 
		
		// 业务列表
		List<GommonBusiness> list = new ArrayList<GommonBusiness>(); 

		try {
			if (null != params && params.size() > 0) {
				for (RequestParameter p : params) {
					if ("userspecialpackage_query_flag".equals(p.getParameterName())) {
						flagBoolean = false;
						p.setParameterValue(userspecialpackage_query_flag); // 设置参数 查询类型
					}
				}
			}
			if (flagBoolean) {
				// 新增参数 查询类型
				par = new RequestParameter();
				par.setParameterName("userspecialpackage_query_flag");
				par.setParameterValue(userspecialpackage_query_flag);
				params.add(par);
			}
			// 组装发送报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqueryloversms_819", params);
			if (null != reqXml && !"".equals(reqXml)) {
				
				// 发送并接收报文
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cqueryloversms_819", super.generateCity(params)));
			}

			// 解析BOSS报文
			if (null != rspXml && !"".equals(rspXml)) {
				
				// 解析报文 根节点
				Element root = this.config.getElement(rspXml);
				
				if("1".equals(userspecialpackage_query_flag)){
					// 获取错误编码
					resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
					
					// 错误描述
					String resp_desc = this.config.getChildText(this.config.getElement(root, "response"), "resp_desc");
					
					// 设置结果信息
					this.getErrInfo(accessId, config, params, res, resp_code, resp_desc, "cc_cqueryloversms_819");
				}
				
				// 成功
				if (null != resp_code && "0000".equals(resp_code)) {
					
					List<Element> userList = this.config.getContentList(root, "userspecialpackage_main_user_id");
					if (null != userList && userList.size() > 0) {
						for (Element element : userList) {
							Element e = this.config.getElement(element, "cuserspecialpackagedt");
							if (null != e) {
								gDt = new GommonBusiness();
								// 开始时间
								gDt.setBeginDate(this.config.getChildText(e, "userspecialpackage_use_date"));
								// 结束时间
								gDt.setEndDate(this.config.getChildText(e, "userspecialpackage_end_date"));
								// 手机号码
								gDt.setReserve1(this.config.getChildText(e, "userspecialpackage_msisdn"));
								// 在用套餐
								if ("1".equals(userspecialpackage_query_flag)) {
									gDt.setState(2); // 已开通
								} else if ("3".equals(userspecialpackage_query_flag)) // 预约套餐
								{
									gDt.setState(3); // 预约开通
								} else if ("2".equals(userspecialpackage_query_flag)) // 已关闭套餐
								{
									gDt.setState(1); // 未开通
								}
							}
							list.add(gDt);
						}
					}
				}
			}
			return list;
		} catch (Exception e) {
			logger.error(e, e);
		}
		return list;
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
	public void getErrInfo(String accessId, ServiceConfig config, List<RequestParameter> params, QRY020011Result res, String resp_code, String resp_desc, String xmlName) {
		ErrorMapping errDt = null; // 错误编码解析

		try {
			// 设置结果编码 0：成功 1：失败
			res.setResultCode("0000".equals(resp_code) ? "0" : "1");

			// 失败
			if (!"0000".equals(resp_code)) {
				res.setErrorCode(resp_code); // 设置错误编码
				res.setErrorMessage(resp_desc); // 设置错误信息
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
