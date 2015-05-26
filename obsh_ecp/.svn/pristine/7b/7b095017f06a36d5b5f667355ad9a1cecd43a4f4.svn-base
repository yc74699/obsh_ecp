package com.xwtech.xwecp.service.logic.invocation;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.communication.RemoteCallContext;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ServiceException;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.service.ServiceInfo;
import com.xwtech.xwecp.service.ServiceLocator;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 前置处理基础类
 * @author wuzd
 *
 */
public class BaseInvocation {
	
	private static final String SUCCESS = "0";

	private static final String FAIL = "1";

	private static final String BOSSSUCCESS = "0000";

	private static final Logger logger = Logger.getLogger(BaseInvocation.class);
	
	Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");

	protected BossTeletextUtil bossTeletextUtil;
	
	protected IRemote remote;
	
	protected WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;

	public BaseInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	/**
	 * 组织地市、渠道信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	protected RemoteCallContext generateCity (List<RequestParameter> params) throws Exception {
		
		RequestParameter userCityParam = this.getParameter(params, ServiceExecutor.ServiceConstants.USER_CITY);
		String userCity = userCityParam != null ? userCityParam.getParameterValue().toString() : "";
		RequestParameter userChannelParam = this.getParameter(params, ServiceExecutor.ServiceConstants.INVOKE_CHANNEL);
		String userChannel = userChannelParam != null ? userChannelParam.getParameterValue().toString() : "";
		RemoteCallContext remoteCallContext = new RemoteCallContext();
		remoteCallContext.setUserCity(userCity);
		remoteCallContext.setInvokeChannel(userChannel);
		RequestParameter traceParm = this.getParameter(params, "context_traceId");
		String traceId = (String) (traceParm == null ? "" :traceParm.getParameterValue()) ;
		remoteCallContext.setTraceId(traceId);
		return remoteCallContext;
	}
	
	/**
	 * 地市是否需要割接
	 * @param city
	 * @return
	 */
	protected boolean needCutCity(String city) {
		//return city != null && XWECPApp.NEED_CUT_CITYS.indexOf(city) != -1;
		return city != null && true;
	}
	
	/**
	 * 从Boss返回的报文体中取出String类型值
	 * @param bossMap  boss值Map
	 * @param key 键值
	 * @return
	 */
	protected String getString(Map bossMap, String key) {
		if (bossMap != null) {
			Object o = bossMap.get(key);
			if (o != null) {
				return o.toString().trim();
			}
		}
		return "";
	}
	
	/**
	 * 获取参数值
	 * @param params
	 * @param name
	 * @return
	 */
	protected RequestParameter getParameter(List<RequestParameter> params, String name)
	{
		if (params != null && params.size() > 0) {
			for(int i = 0; i<params.size(); i++)
			{
				RequestParameter param = params.get(i);
				if(param.getParameterName().equals(name))
				{
					return param;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 从参数列表里获取参数值
	 * 
	 * @param params
	 * @param parameterName
	 * @return
	 */
	protected Object getParameters(final List<RequestParameter> params, String parameterName) {
		Object rtnVal = null;
		for (RequestParameter parameter : params) {
			String pName = parameter.getParameterName();
			if (pName.equals(parameterName)) {
				rtnVal = parameter.getParameterValue();
				break;
			}
		}
		return rtnVal;
	}
	
	/**
	 * 设置参数值
	 * @param params
	 * @param name
	 * @return
	 */
	protected void setParameter(List<RequestParameter> params, String name, String value)
	{
		if (params != null && params.size() > 0) {
			boolean flag = true;
			for(int i = 0; i<params.size(); i++)
			{
				RequestParameter param = params.get(i);
				if(param.getParameterName().equals(name))
				{
					flag = false;
					param.setParameterValue(value);
				}
			}
			
			if (flag) {
				params.add(new RequestParameter(name, value));
			}
		}
	}
	
	/**
	 * 请求参数复制
	 * @param params
	 * @return
	 */
	protected List<RequestParameter> copyParam(final List<RequestParameter> params) {
		List<RequestParameter> paramNew = new ArrayList<RequestParameter>();
		
		for (RequestParameter param:params) {
			paramNew.add(new RequestParameter(param.getParameterName(), param.getParameterValue()));
		}
		
		return paramNew;
	}
	
	/**
	 * 解析报文
	 * @param tmp
	 * @return
	 */
	protected Element getElement(byte[] tmp)
	{
		Element root = null;
		try
		{
			ByteArrayInputStream ins = new ByteArrayInputStream(tmp);
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(ins);
			root = doc.getRootElement();
			return root;
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return root;
	}
	
	private static Map<String, String> mapBrandNameDic = null;
	
	/**
	 * 用户品牌字典
	 * @return
	 */
	protected Map<String, String> getBrandNameMap() {
		if (mapBrandNameDic == null) {
			mapBrandNameDic = new HashMap<String, String>();
			mapBrandNameDic.put(SUCCESS, "全球通");
			mapBrandNameDic.put("2", "金卡快捷通");
			mapBrandNameDic.put("3", "快捷通");
			mapBrandNameDic.put("4", "随e卡");
			mapBrandNameDic.put("5", "神州行大众卡");
			mapBrandNameDic.put("6", "动感地带");
			mapBrandNameDic.put("7", "神州行金卡");
			mapBrandNameDic.put("8", "神州行新春卡");
			mapBrandNameDic.put("9", "神州行长途卡");
			mapBrandNameDic.put("10", "神州行家园卡");
			mapBrandNameDic.put("11", "动感地带2");
			mapBrandNameDic.put("12", "神州行标准卡");
			mapBrandNameDic.put("13", "A类行业应用卡");
			mapBrandNameDic.put("14", "B类行业应用卡");
			mapBrandNameDic.put("15", "C类行业应用卡");
			mapBrandNameDic.put("99", "不限品牌");
		}
		
		return mapBrandNameDic;

	}
	
	//根据当前节点下的子节点名字取值
	protected String getNodeValueByNodeName(Element node,String nodeName)
    {
		return pattern.matcher(node.getChildText(nodeName)).replaceAll("");
    }
	
	protected void setErrorResult(BaseServiceInvocationResult ret) {
		ret.setResultCode(SUCCESS);
		ret.setErrorCode("-20000");
		ret.setErrorMessage("接口调用失败 -- CRM空响应或者超时!");
	}
	
	/**
	 * 调用逻辑接口根据cmd接口名
	 * @param cmd
	 * @param params
	 * @param accessId
	 * @return
	 * @throws ServiceException
	 */
	protected Object getInvocation(String cmd,List<RequestParameter> params,String accessId) throws ServiceException {
		ServiceLocator sl = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
		ServiceInfo si = sl.locate(cmd, params);
		Object obj = si.getServiceInstance().execute(accessId);
		return obj;
	}
	
	/**
	 * 组装boss报文(xml格式)
	 * @param params
	 * @param bossTemplate
	 * @return
	 */
	protected String mergeReqXML2Boss(List<RequestParameter> params,String bossTemplate) {
		return this.bossTeletextUtil.mergeTeletext(bossTemplate, params);
	}
	
	/**
	 * 发送请求
	 * @param accessId
	 * @param params
	 * @param reqXml
	 * @param bossTempate
	 * @return
	 * @throws CommunicateException
	 * @throws Exception
	 */
	protected String sendReqXML2BOSS(String accessId,
			List<RequestParameter> params, String reqXml, String bossTempate)
			throws CommunicateException, Exception {
		return (String)this.remote.callRemote(
				 new StringTeletext(reqXml, accessId, bossTempate, this.generateCity(params)));
	}
	
	/**
	 * 设置通用返回结果
	 * @param BaseServiceInvocationResult
	 * @param resp_code
	 * @param resp_desc
	 */
	protected void setCommonResult(BaseServiceInvocationResult res, String resp_code,
			String resp_desc) {
		res.setResultCode(BOSSSUCCESS.equals(resp_code) ? SUCCESS : FAIL);
		res.setErrorCode(resp_code);
		res.setErrorMessage(resp_desc);
	}
	
	protected void removeRequest(List<RequestParameter> params,String key){
		for (int i = 0; i < params.size(); i++) {
			RequestParameter parameter = params.get(i);
			String pName = parameter.getParameterName();
			if (pName.equals(key)) {
				params.remove(i);
				return;
			}
		}
	}
	
	protected String genSMSContent(String formatExpectTime,
			String fmtexpectPeriod, String orderId, String marketOrBusiName,
			String officeName,int optType) {
		String smsContent = "";
		//预约办理
		if(optType == 1)
		{
			smsContent = "您已成功预约办理" + marketOrBusiName + "，"
			+"预约号" + orderId + "，请于" + formatExpectTime  + fmtexpectPeriod + "前往" + officeName + "" +"办理！";
				
		}
		//预约修改
		if(optType == 2)
		{
			smsContent = "您已重新预约办理" + marketOrBusiName + "，"
			+"预约号" + orderId + "，请于" + formatExpectTime  + fmtexpectPeriod + "前往" + officeName + "" +"办理！";
				
		}
		// 预约取消
		if(optType == 3)
		{
			smsContent = "您已成功取消预约办理" + marketOrBusiName + "，"
			+"欢迎您再次办理本活动或其他优惠活动！";
				
		}
		return smsContent;
	}
}
