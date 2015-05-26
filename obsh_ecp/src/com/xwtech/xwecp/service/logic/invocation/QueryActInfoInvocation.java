package com.xwtech.xwecp.service.logic.invocation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ActivityBean;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050032Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 查询用户已参加的推荐活动
 * @author Tkk
 *
 */
public class QueryActInfoInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(QueryActInfoInvocation.class);
	
	/**
	 * Boss接口
	 */
	private static final String BOSS_INTERFACE = "cc_emktServices_12";
	
	/**
	 * 调用Boss接口
	 */
	private IRemote remote = null;
	
	/**
	 * 组装报文
	 */
	private BossTeletextUtil bossTeletextUtil = null;
	
	private WellFormedDAO wellFormedDAO = null;
	
	public QueryActInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		QRY050032Result result = new QRY050032Result();
		
		//1.组装报文需要的参数
		RequestParameter requestTime = new RequestParameter();
		requestTime.setParameterName("request_time");
		requestTime.setParameterValue(DateTimeUtil.getTodayChar14());
		RequestParameter statDate = new RequestParameter();
		statDate.setParameterName("stat_date");
		statDate.setParameterValue(DateTimeUtil.getTodayChar8());
		params.add(requestTime);
		params.add(statDate);
		
		//2组装请求报文
		String reqXml = bossTeletextUtil.mergeTeletext(BOSS_INTERFACE, params);
		
		//调用Boss接口
		try {
			String rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, BOSS_INTERFACE, this.generateCity(params)));
			
			parseXmlConversionObj(rspXml, result);
		
		} catch (CommunicateException e) {
			logger.error(e, e);
		} catch (Exception e) {
			logger.error(e, e);
		}
		
		return result;
	}
	
	/**
	 * 解析boss返回的报文
	 * @param rspXml
	 * @param result
	 */
	private void parseXmlConversionObj(String rspXml, QRY050032Result result) {
		StringReader stringReader = new StringReader(rspXml);
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Document doc = builder.build(new InputSource(stringReader));
			Element root = doc.getRootElement();
			
			//获取返回信息
			Element responseEl = root.getChild("response");
			if(responseEl != null){
				setResponseInfo(responseEl, result);
			}
			
			//获取推荐活动详情
			List<Element> actInfoEl = root.getChild("content").getChildren("actinfolist");
			if(actInfoEl != null){
				setActInfo(actInfoEl, result);
			}
			
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	/**
	 * 从boss报文中获取推荐活动
	 * @param actInfoEl
	 * @param result
	 */
	private void setActInfo(List<Element> actInfoList, QRY050032Result result) {
		List<ActivityBean> activityBeanList = new ArrayList<ActivityBean>();
		
		//遍历
		for(Element actInfo : actInfoList){
			Element actInfoElement = actInfo.getChild("actlist"); 
			ActivityBean activityBean = new ActivityBean();
			activityBean.setActId(actInfoElement.getChildTextTrim("activity_id"));
			activityBean.setActName(actInfoElement.getChildTextTrim("act_name"));
			
			//获取业务Id,并转换本地保持的bizNum
			String bizId = actInfoElement.getChildTextTrim("cust_biz_id");
			Map<String,String> bizMap = wellFormedDAO.getBizNum4BizId(bizId);
			if(bizMap != null){
				activityBean.setBizName(bizMap.get("F_NEW_BIZ_NAME"));
				activityBean.setBizId(bizMap.get("F_NEW_BIZ_NUM"));
				activityBean.setActUrl('#' + bizMap.get("F_NEW_BIZ_URL"));
			}
			else
			{
				activityBean.setBizId(actInfoElement.getChildTextTrim("biz_id"));
				activityBean.setBizName(actInfoElement.getChildTextTrim("biz_name"));
				activityBean.setActUrl(actInfoElement.getChildTextTrim("act_url"));
			}
			
			activityBean.setCustBizId(actInfoElement.getChildTextTrim("cust_biz_id"));
			activityBean.setActAbstract(actInfoElement.getChildTextTrim("act_abstract"));
			activityBean.setActContent(actInfoElement.getChildTextTrim("act_content"));
			activityBeanList.add(activityBean);
		}
		
		result.setActivityBean(activityBeanList);
	}

	/**
	 * 从Boss报文获取响应状态插入逻辑接口中s
	 * @param responseEl
	 * @param result
	 */
	private void setResponseInfo(Element responseEl, QRY050032Result result) {
		String respCode = responseEl.getChildText("resp_code");
		result.setResultCode(BOSS_SUCCESS.equals(respCode)?LOGIC_SUCESS:LOGIC_ERROR);
		String respDec = responseEl.getChildText("resp_desc");
		
		ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY040002", BOSS_INTERFACE, respCode);
		if (null != errDt)
		{
			respCode = errDt.getLiErrCode();
			respDec = errDt.getLiErrMsg();
		}
		result.setErrorCode(respCode);
		result.setErrorMessage(respDec);
	}

}
