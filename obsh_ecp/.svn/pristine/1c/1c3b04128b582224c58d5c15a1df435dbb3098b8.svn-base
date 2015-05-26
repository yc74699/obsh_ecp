package com.xwtech.xwecp.service.logic.invocation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;
import org.xml.sax.InputSource;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceException;
import com.xwtech.xwecp.service.ServiceInfo;
import com.xwtech.xwecp.service.ServiceLocator;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ActivityBean;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050032Result;
import com.xwtech.xwecp.service.logic.pojo.QRY050033Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 查询用户已参加推荐活动
 * 
 * @author Tkk
 * 
 */
public class QueryMktActInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryMktActInvocation.class);

	/**
	 * Boss接口
	 */
	private static final String BOSS_INTERFACE = "cc_emktServices_12_2";

	/**
	 * 调用Boss接口
	 */
	private IRemote remote = null;

	/**
	 * 组装报文
	 */
	private BossTeletextUtil bossTeletextUtil = null;

	private WellFormedDAO wellFormedDAO = null;

	public QueryMktActInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY050033Result result = new QRY050033Result();
		result.setResultCode(LOGIC_SUCESS);
		
		// 1.组装报文需要的参数
		RequestParameter requestTime = new RequestParameter();
		requestTime.setParameterName("request_time");
		requestTime.setParameterValue(DateTimeUtil.getTodayChar14());
		params.add(requestTime);

		// 2组装请求报文
		String reqXml = bossTeletextUtil.mergeTeletext(BOSS_INTERFACE, params);

		// 调用Boss接口
		try {
			String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, BOSS_INTERFACE, super.generateCity(params)));

			parseXmlConversionObj(accessId, rspXml, result, params);

		} catch (CommunicateException e) {
			logger.error(e, e);
		} catch (Exception e) {
			logger.error(e, e);
		}

		return result;
	}

	/**
	 * 解析boss返回的报文
	 * 
	 * @param rspXml
	 * @param result
	 */
	private void parseXmlConversionObj(String accessId, String rspXml, QRY050033Result result, List<RequestParameter> params) {
		StringReader stringReader = new StringReader(rspXml);
		SAXBuilder builder = new SAXBuilder();

		try {
			Document doc = builder.build(new InputSource(stringReader));
			Element root = doc.getRootElement();

			// 获取返回信息
			Element responseEl = root.getChild("response");
			if (responseEl != null) {
				setResponseInfo(responseEl, result);
			}

			// 获取推荐活动详情
			List<Element> actInfoEl = root.getChild("content").getChildren("actlist");
			if (actInfoEl != null) {
				setActInfo(accessId, actInfoEl, result, params);
			}

		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 从boss报文中获取推荐活动
	 * 
	 * @param actInfoEl
	 * @param result
	 */
	private void setActInfo(String accessId, List<Element> actInfoList, QRY050033Result result, List<RequestParameter> params) {
		List<ActivityBean> activityBeanList = new ArrayList<ActivityBean>();
		
		// 获取所有推荐活动
		//调用QRY050032接口
		ServiceLocator sl = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
		List<ActivityBean> allActivityList = null;
		try {
			ServiceInfo si = sl.locate("QRY050032", params);
			QRY050032Result re = (QRY050032Result)si.getServiceInstance().execute(accessId);
			allActivityList = re.getActivityBean();
			
			// 遍历
			for (Element actInfo : actInfoList) {
				String actId = actInfo.getChildTextTrim("activity_id");
				ActivityBean activityBean = new ActivityBean();
				
				for(ActivityBean bean : allActivityList){
					if(bean.getActId().equals(actId)){
						activityBean = bean;
						break;
					}
				}
				activityBeanList.add(activityBean);
			}
		} catch (ServiceException e) {
			logger.error(e, e);
		}
		result.setActivityBean(activityBeanList);
	}

	/**
	 * 从Boss报文获取响应状态插入逻辑接口中s
	 * 
	 * @param responseEl
	 * @param result
	 */
	private void setResponseInfo(Element responseEl, QRY050033Result result) {
		String respCode = responseEl.getChildText("resp_code");
		
		String respDec = responseEl.getChildText("resp_desc");

		ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY050033", BOSS_INTERFACE, respCode);
		if (null != errDt) {
			respCode = errDt.getLiErrCode();
			respDec = errDt.getLiErrMsg();
		}
		result.setErrorCode(respCode);
		result.setErrorMessage(respDec);
	}

}
