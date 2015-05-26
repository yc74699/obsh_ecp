package com.xwtech.xwecp.service.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathFunctionResolver;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.tree.DefaultCDATA;

import com.xwtech.xwecp.AppConfig;
import com.xwtech.xwecp.communication.CommunicateException;
import com.xwtech.xwecp.communication.IStreamableMessage;
import com.xwtech.xwecp.communication.RemoteCallContext;
import com.xwtech.xwecp.log.PerformanceTracer;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.IService;
import com.xwtech.xwecp.service.ITeletextResolver;
import com.xwtech.xwecp.service.IllegalServiceInputException;
import com.xwtech.xwecp.service.ServiceException;
import com.xwtech.xwecp.service.ServiceExecutor;
import com.xwtech.xwecp.service.config.BeanFieldInfo;
import com.xwtech.xwecp.service.config.DataTypeConstants;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.config.ServiceInputParameter;
import com.xwtech.xwecp.service.config.ServiceOutputField;
import com.xwtech.xwecp.service.config.ServiceResultMapping;
import com.xwtech.xwecp.service.config.TeletextMapping;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.teletext.IExternalFunctionExecutor;
import com.xwtech.xwecp.util.SavePortUtil;

public class DefaultServiceImpl extends BaseServerServiceImpl implements IService {
	private static final Logger logger = Logger.getLogger(DefaultServiceImpl.class);

	protected ServiceConfig serviceConfig;

	protected List<RequestParameter> params;

	protected BossTeletextUtil teletextUtil;

	protected XPathFunctionResolver functionResolver;

	protected IBossResponseValidator bossResponseValidator;

	protected Map<String, String> expressionEvaluateBuffer = new HashMap<String, String>();

	private Map<String, IExternalFunctionExecutor> externalFunctionMap = new HashMap<String, IExternalFunctionExecutor>();

	private static final String PARSEFAIL = "对应的业务解析方式失败";
	
	//所需過濾的ECP接口   掌廳搶流量紅包接口
	private static StringBuffer cmds = new StringBuffer();
	//掌廳集團探測 端口  
	private static String WAP_JTTC_PORT = "10009";
	//网厅集团探测服务IP、端口
	private static String OBSH_JTTC_URL = "10.32.122.166:10005";
	
	static 
	{
		cmds.append("QRY030011").append("QRY040020").append("QRY040041").append("QRY040048")
		    .append("QRY040065").append("QRY040070").append("QRY010003").append("QRY010028")
		    .append("QRY040047").append("QRY010018").append("QRY040075").append("QRY040002");
		
	}
	
	public IBossResponseValidator getBossResponseValidator() {
		return bossResponseValidator;
	}

	public void setBossResponseValidator(IBossResponseValidator bossResponseValidator) {
		this.bossResponseValidator = bossResponseValidator;
	}

	public XPathFunctionResolver getFunctionResolver() {
		return functionResolver;
	}

	public void setFunctionResolver(XPathFunctionResolver functionResolver) {
		this.functionResolver = functionResolver;
	}

	public List<RequestParameter> getParams() {
		return params;
	}

	public void setParams(List<RequestParameter> params) {
		this.params = params;
	}

	public BossTeletextUtil getTeletextUtil() {
		return teletextUtil;
	}

	public void setTeletextUtil(BossTeletextUtil teletextUtil) {
		this.teletextUtil = teletextUtil;
	}

	public ServiceConfig getServiceConfig() {
		return serviceConfig;
	}

	public void setServiceConfig(ServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}
	

	protected BaseServiceInvocationResult executeByConfig(String accessId) throws ServiceException {
		PerformanceTracer pt = PerformanceTracer.getInstance();
		long n = 0;
		// 获取命令字
		String cmd = this.getServiceConfig().getCmd();
		// 根据正则表达式获取相匹配报文模板
		n = pt.addBreakPoint();
		TeletextMapping tm = this.getBossTeletextMapping();
		pt.trace("getBossTeletextMapping...", n);
		if ( tm == null ) { throw new ServiceException("找不到匹配的BOSS报文模板!"); }
		// 根据报文模板初始化逻辑接口实现类
		if ( tm.getDirectImplClass() != null && tm.getDirectImplClass().trim().length() > 0 ) {
			String implClass = tm.getDirectImplClass();
			Class clazz = null;
			ILogicalService service = null;
			try {
				n = pt.addBreakPoint();
				clazz = Class.forName(implClass);
				service = (ILogicalService) clazz.newInstance();
				pt.trace("实例化逻辑接口实现类...", n);
			}
			catch (ClassNotFoundException e) {
				logger.error("逻辑接口实现类[" + implClass + "]没有找到!");
				throw new ServiceException(e);
			}
			catch (InstantiationException e) {
				logger.error("逻辑接口实现类[" + implClass + "]实例化失败!");
				throw new ServiceException(e);
			}
			catch (IllegalAccessException e) {
				logger.error("逻辑接口实现类[" + implClass + "]实例化失败!");
				throw new ServiceException(e);
			}
			catch (Exception e) {
				throw new ServiceException(e);
			}
			n = pt.addBreakPoint();
			BaseServiceInvocationResult ret = service.executeService(accessId, this.serviceConfig, this.params);
			pt.trace("执行逻辑接口实现类[" + implClass + "]...", n);
			return ret;
		}
		// 组BOSS报文
		n = pt.addBreakPoint();
		String teletext = teletextUtil.mergeTeletext(tm.getTeletextTemplate(), this.params);
		pt.trace("组BOSS报文...", n);
		if ( teletext != null ) {
			logger.debug("准备请求BOSS：\n" + teletext);
			String bossResponse = null;
			try {
				// 2010年5月17日修改, BOSS分地市割接,
				// 需要将ServiceMessage.head中的userCity穿透到报文发送处,取固有参数fixed_ddr_city
				// 传入到remote call, 并且修改StringTeletext,增加扩展上下文参数
				RequestParameter userCityParam = this.getParameter(ServiceExecutor.ServiceConstants.USER_CITY);
				String userCity = userCityParam != null ? userCityParam.getParameterValue().toString() : "";
				RemoteCallContext remoteCallContext = new RemoteCallContext();
				remoteCallContext.setUserCity(userCity);
				// 修改结束(BOSS分地市割接)
				// 2010年7月28日修改, 掌厅接入, 根据来源渠道不同, 需要将来源渠道传递到remote call
				RequestParameter invokeChannelParam = this.getParameter(ServiceExecutor.ServiceConstants.INVOKE_CHANNEL);
				String invokeChannel = invokeChannelParam != null ? invokeChannelParam.getParameterValue().toString() : "";
				remoteCallContext.setInvokeChannel(invokeChannel);
				//增加traceId
				String traceId = "";
				if(null != params && 0 < params.size())
				{
					for(RequestParameter parameter : params)
					{
						String paramName = parameter.getParameterName();
						if("context_traceId".equals(paramName))
						{
							traceId = (String) parameter.getParameterValue();
						}
					}
				}
				traceId =  traceId == null ? "" : traceId ;
				remoteCallContext.setTraceId(traceId);
				// 修改结束,掌厅接入的来源渠道传递
				// 发送BOSS请求
				n = pt.addBreakPoint();
				bossResponse = (String) this.remote.callRemote(new StringTeletext(teletext, accessId, tm.getTeletextTemplate(), remoteCallContext));
				pt.trace("请求BOSS...", n);
				logger.debug("BOSS响应: \n" + bossResponse);
			}
			catch (CommunicateException e) {
				logger.error("请求BOSS失败[" + e.getMessage() + "]!");
				throw new ServiceException(e);
			}

			// 根据Boss返回报文，组装返回对象
			if ( bossResponse != null && bossResponse.trim().length() > 0 ) {
				bossResponse = bossResponse.trim();
				// 创建返回对象
				String serviceResultRealClass = this.getResultClassName();
				Class retClazz = null;
				BaseServiceInvocationResult result = null;
				
				if(bossResponse.endsWith(PARSEFAIL))
				{
					result = new BaseServiceInvocationResult();
					result.setResultCode("1");
					result.setErrorCode("-99");
					result.setErrorMessage("解析失败：BOSS接口不存在或渠道不支持该接口");
					return result;
				}
				try {
					n = pt.addBreakPoint();
					retClazz = Class.forName(serviceResultRealClass);
					result = (BaseServiceInvocationResult) (retClazz.newInstance());
					pt.trace("实例化返回对象...", n);
				}
				catch (ClassNotFoundException e) {
					logger.error("命令字[" + cmd + "]逻辑接口返回类[" + serviceResultRealClass + "], 没有找到!");
					throw new ServiceException(e);
				}
				catch (InstantiationException e) {
					logger.error("命令字[" + cmd + "]逻辑接口返回类[" + serviceResultRealClass + "], 实例化失败!");
					throw new ServiceException(e);
				}
				catch (IllegalAccessException e) {
					logger.error("命令字[" + cmd + "]逻辑接口返回类[" + serviceResultRealClass + "], 实例化失败!");
					throw new ServiceException(e);
				}
				catch (Exception e) {
					throw new ServiceException(e);
				}
				// 根据配置, 为返回对象设置默认值
				n = pt.addBreakPoint();
				this.setDefaultReturnValue(result);
				pt.trace("设置返回对象默认值...", n);
				try {
					// 根据配置的result mapping, 往返回对象里填值
					n = pt.addBreakPoint();

					/**
					 * 2011-11-15 maofw 修改如果存在result-mapping-id
					 * 则将其作为id从map中获得resultMapping
					 */
					String mapKey = (tm.getResultMappingId() == null || "".equals(tm.getResultMappingId().trim())) ? tm.getTeletextTemplate() : tm.getResultMappingId();
					/**
					 * end
					 */
					this.evaluateResult(cmd, tm.getTeletextTemplate(), bossResponse, result, mapKey);
					pt.trace("装配...", n);
				}
				catch (Exception e) {
					throw new ServiceException(e);
				}
				// 如果配置了teletext resover, 再调用那个resolver往里面填值
				String resolverClassName = tm.getResolverClass();
				if ( resolverClassName != null && resolverClassName.trim().length() > 0 ) {
					n = pt.addBreakPoint();
					Class resolverClass = null;
					ITeletextResolver resolver = null;
					try {
						resolverClass = Class.forName(resolverClassName);
						resolver = (ITeletextResolver) (resolverClass.newInstance());
					}
					catch (ClassNotFoundException e) {
						logger.error("命令字[" + cmd + "]报文手动装配类[" + resolverClassName + "], 没有找到!");
						throw new ServiceException(e);
					}
					catch (InstantiationException e) {
						logger.error("命令字[" + cmd + "]逻辑接口返回类[" + resolverClassName + "], 实例化失败!");
						throw new ServiceException(e);
					}
					catch (IllegalAccessException e) {
						logger.error("命令字[" + cmd + "]逻辑接口返回类[" + resolverClassName + "], 实例化失败!");
						throw new ServiceException(e);
					}
					catch (Exception e) {
						throw new ServiceException(e);
					}
					try {
						resolver.resolve(result, bossResponse, this.params);
					}
					catch (Exception e) {
						throw new ServiceException(e);
					}
					pt.trace("调用resolver...", n);
				}
				return result;
			}
			else {
				throw new ServiceException("BOSS空响应!");
			}
		}
		return null;
	}

	/**
	 * 根据配置, 为返回对象设置默认值
	 * 
	 * @param result
	 * @throws ServiceException
	 */
	private void setDefaultReturnValue(BaseServiceInvocationResult result) throws ServiceException {
		for (int i = 0; i < this.serviceConfig.getOutput().getOutputFields().size(); i++) {
			ServiceOutputField outputField = this.serviceConfig.getOutput().getOutputFields().get(i);
			String fieldName = outputField.getName();
			String defValue = outputField.getDefaultValue();
			if ( defValue != null&&defValue!="" ) {
				String dataType = outputField.getDataType();
				Object realVal = null;
				if ( DataTypeConstants.STRING.equalsIgnoreCase(dataType) ) {
					realVal = defValue;
				}
				else if ( DataTypeConstants.INT.equalsIgnoreCase(dataType) ) {
					defValue = defValue.trim();
					realVal = Integer.parseInt(defValue);
				}
				else if ( DataTypeConstants.DOUBLE.equalsIgnoreCase(dataType) ) {
					defValue = defValue.trim();
					realVal = Double.parseDouble(defValue);
				}
				else if ( DataTypeConstants.LONG.equalsIgnoreCase(dataType) ) {
					defValue = defValue.trim();
					realVal = Long.parseLong(defValue);
				}
				else if ( DataTypeConstants.LIST.equalsIgnoreCase(dataType) ) {
					realVal = new ArrayList();
				}
				else if ( DataTypeConstants.CLASS.equalsIgnoreCase(dataType) ) {
					realVal = new Object();
				}
				else {
					throw new ServiceException("数据类型不正确[" + dataType + "]!");
				}
				try {
					callSetter(result, fieldName, realVal, dataType);
				}
				catch (Exception e) {
					logger.error("设置默认值失败!");
					throw new ServiceException(e);
				}
			}
		}
	}

	private static Object callGetter(Object instance, String fieldName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		char firstLetter = fieldName.charAt(0);
		firstLetter = Character.toUpperCase(firstLetter);
		String getterMethodName = "get" + firstLetter + fieldName.substring(1);
		Method m = instance.getClass().getMethod(getterMethodName, new Class[] {});
		return m.invoke(instance, new Object[] {});
	}

	private static void callSetter(Object instance, String fieldName, Object value, String dataType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		char firstLetter = fieldName.charAt(0);
		firstLetter = Character.toUpperCase(firstLetter);
		String setterMethodName = "set" + firstLetter + fieldName.substring(1);
		Class[] params = new Class[1];
		if ( DataTypeConstants.STRING.equalsIgnoreCase(dataType) ) {
			params[0] = String.class;
			Method m = instance.getClass().getMethod(setterMethodName, params);
			m.invoke(instance, value.toString());
		}
		else if ( DataTypeConstants.INT.equalsIgnoreCase(dataType) ) {
			params[0] = Integer.TYPE;
			Method m = instance.getClass().getMethod(setterMethodName, params);
			if(value !=""){
				m.invoke(instance, Integer.valueOf(""+value));
			}
		}
		else if ( DataTypeConstants.DOUBLE.equalsIgnoreCase(dataType) ) {
			params[0] = Double.TYPE;
			Method m = instance.getClass().getMethod(setterMethodName, params);
			m.invoke(instance, Double.valueOf("" + value));
		}
		else if ( DataTypeConstants.LONG.equalsIgnoreCase(dataType) ) {
			params[0] = Long.TYPE;
			Method m = instance.getClass().getMethod(setterMethodName, params);
			// 邵琪修改-20100115，截取小数点前的数据
			if ( value != null && value.toString().indexOf(".") > 0 ) {
				value = (value.toString()).substring(0, (value.toString()).indexOf("."));
			}
			// 邵琪修改-20100309，为空时不赋值
			if ( ("" + value).trim().length() > 0 )
				m.invoke(instance, Long.valueOf("" + value));
		}
		else if ( DataTypeConstants.LIST.equalsIgnoreCase(dataType) ) {
			params[0] = List.class;
			Method m = instance.getClass().getMethod(setterMethodName, params);
			m.invoke(instance, (List) value);
		}
		else if ( DataTypeConstants.CLASS.equalsIgnoreCase(dataType) ) {
			params[0] = value.getClass();
			Method m = instance.getClass().getMethod(setterMethodName, params);
			m.invoke(instance, value);
		}
		else {
			throw new IllegalArgumentException("数据类型不支持[" + dataType + "]!");
		}
	}

	/**
	 * 解析BOSS报文
	 * 
	 * @param bossResponse
	 *            boss返回报文
	 * @param result
	 *            返回对象
	 * @param bossTemplateId
	 *            模板命令字
	 * @throws Exception
	 */
	private void evaluateResult(String cmd, String bossFunctionId, String bossResponse, BaseServiceInvocationResult result, String bossTemplateId) throws Exception {
		PerformanceTracer pt = PerformanceTracer.getInstance();
		long n = 0;
		n = pt.addBreakPoint();
		// Dom4j实现的XPATH
		Document doc = DocumentHelper.parseText(bossResponse);
		pt.trace("Dom4j parse...", n);
		org.jaxen.XPath xpath = new org.jaxen.dom4j.Dom4jXPath("/");
		Object root = xpath.evaluate(doc);
		// 调用BossResponseValidator处理通用的BOSS报文解析
		if ( this.bossResponseValidator != null ) {
			if ( !this.bossResponseValidator.validate(cmd, bossFunctionId, root, result) ) {
				logger.info("BOSS报文验证失败, 不作evaluate处理!");
				return;
			}
		}
		Stack objStack = new Stack();
		List<ServiceResultMapping> resultMapping = this.getServiceConfig().getImpl().getResultMapping(bossTemplateId);
		this.evaluateResult(root, result, resultMapping, objStack);
	}

	/**
	 * 根据meta模板，解析boss报文与mapping对应信息
	 * 
	 * @param item
	 *            报文节点
	 * @param obj
	 *            返回对象
	 * @param resultMapping
	 *            配置mapping
	 * @param objStack
	 * @throws Exception
	 */
	private void evaluateResult(Object item, Object obj, List<ServiceResultMapping> resultMapping, Stack objStack) throws Exception {
		if ( resultMapping == null || resultMapping.size() == 0 ) { return; }
		for (int i = 0; i < resultMapping.size(); i++) {
			ServiceResultMapping resultMap = resultMapping.get(i);
			String expression = resultMap.getExpression().trim();
			String evaluateExpression[] = this.getEvaluateExpression(expression);
			objStack.push(resultMap.getName());
			BeanFieldInfo outputField = this.getBeanFieldInfo(objStack);
			if ( "xpath".equalsIgnoreCase(evaluateExpression[0]) ) {
				String xpathExpression = evaluateExpression[1];
				xpathExpression = xpathExpression.trim();
				if ( resultMap.getChildResultMapping() != null && resultMap.getChildResultMapping().size() > 0 ) {
					// 如果有子mapping, 表示其数据类型是list或是class
					if ( DataTypeConstants.LIST.equalsIgnoreCase(outputField.getDataType()) ) {
						org.jaxen.XPath xpath = new org.jaxen.dom4j.Dom4jXPath(xpathExpression);
						Object newItem = null;
						try {
							newItem = xpath.selectNodes(item);
						}
						catch (Exception e) {
							logger.error("不能被正确执行的赋值表达式[" + xpathExpression + "][" + e.getMessage() + "]!!");
							continue;
						}
						if ( newItem instanceof List ) {
							List nl = (List) (newItem);
							for (int j = 0; j < nl.size(); j++) {
								Element node = (Element) nl.get(j);
								List listObj = (List) (callGetter(obj, resultMap.getName()));
								if ( listObj == null ) {
									listObj = new ArrayList();
									callSetter(obj, resultMap.getName(), listObj, DataTypeConstants.LIST);
								}
								String extensionClassName = this.getExtensionClassName(outputField.getClassName());
								Class extensionClazz = Class.forName(extensionClassName);
								Object newObject = extensionClazz.newInstance();
								listObj.add(newObject);
								this.evaluateResult(node, newObject, resultMap.getChildResultMapping(), objStack);
							}
						}
						else {
							throw new Exception("未知的 XPathConstants.NODE 获取类型[" + newItem.getClass().getName() + "]!");
						}
					}
					else if ( DataTypeConstants.CLASS.equalsIgnoreCase(outputField.getDataType()) ) {
						org.jaxen.XPath xpath = new org.jaxen.dom4j.Dom4jXPath(xpathExpression);
						Object newItem = null;
						try {
							newItem = xpath.selectSingleNode(item);
						}
						catch (Exception e) {
							logger.error("不能被正确执行的赋值表达式[" + xpathExpression + "][" + e.getMessage() + "]!!");
							continue;
						}
						String extensionClassName = this.getExtensionClassName(outputField.getClassName());
						Class extensionClazz = Class.forName(extensionClassName);
						Object newObject = extensionClazz.newInstance();
						this.evaluateResult(newItem, newObject, resultMap.getChildResultMapping(), objStack);
						callSetter(obj, resultMap.getName(), newObject, DataTypeConstants.CLASS);
					}
				}
				else {
					// 无子mapping, 直接取字串
					String value = "";
					try {
						org.jaxen.XPath xpath = new org.jaxen.dom4j.Dom4jXPath(xpathExpression);
						Object val = xpath.evaluate(item);
						if ( val != null ) {
							if ( val instanceof List ) {
								List l = (List) val;
								if ( l.size() > 0 ) {
									Object o = l.get(0);
									if ( o instanceof Text )
									{
										value = ((Text) o).getText();
									}
									else if ( o instanceof Attribute )
									{
										value = ((Attribute) o).getValue();
									}
									else if ( o instanceof Element )
									{
										value = ((Element) o).getText();
									}
									//增加匹配类型 CDATA
									else if (o instanceof DefaultCDATA)
									{
										value = ((DefaultCDATA) o).getText();
									}
								}
							}
							else
							{
								value = val.toString();
							}
						}
					}
					catch (Exception e) {
						logger.error("不能被正确执行的赋值表达式[" + xpathExpression + "][" + e.getMessage() + "]!!");
						continue;
					}
					if ( resultMap.getValueMapStr() != null && resultMap.getValueMapStr().trim().length() > 0 ) {
						// 做值转换
						Map<String, String> valueMap = resultMap.getValueMap();
						value = this.getMapedValue(valueMap, value);
					}
					callSetter(obj, resultMap.getName(), value, outputField.getDataType());
					// logger.debug("evalute
					// result:["+xpathExpression+"="+value+"]");
				}
			}
			else {
				logger.error("未知赋值表达式[" + expression + "]!");
			}
			objStack.pop();
		}
	}

	/**
	 * 转换值 value-map="平台值:BOSS值;"
	 * 
	 * @param valueMap
	 * @param value
	 * @return
	 */
	private String getMapedValue(Map<String, String> valueMap, String value) {
		if ( value == null || value.trim().length() == 0 ) { return value; }
		String s = valueMap.get(value);
		// 如果没有配置相关的
		if ( s == null ) {
			// 看看有没有匹配"*"的
			s = valueMap.get("*");
			if ( s != null ) { return s; }
		}
		else {
			return s;
		}
		return value;
	}

	private String getBeanFieldExpression(Stack objStack) {
		StringBuffer expression = new StringBuffer();
		for (int i = 0; i < objStack.size(); i++) {
			String name = (String) objStack.get(i);
			expression.append(name).append(".");
		}
		if ( expression.length() > 0 ) {
			return expression.substring(0, expression.length() - 1);
		}
		else {
			return expression.toString();
		}
	}

	private BeanFieldInfo getBeanFieldInfo(Stack objStack) {
		String expression = this.getBeanFieldExpression(objStack);
		return this.getServiceConfig().getFieldInfoMap().get(expression);
	}

	private String getExtensionClassName(String oName) {
		// 返回扩展类包名.类名", 例"com.xwtech.xwecp.service.logic.pojo.XXXX
		String pojoPackage = AppConfig.getConfigValue("service_impl", "extensionClassPackage");
		pojoPackage = pojoPackage == null ? "com.xwtech.xwecp.service.logic.pojo" : pojoPackage;
		return pojoPackage + "." + oName;
	}

	private String[] getEvaluateExpression(String expression) {
		if ( expression.toLowerCase().startsWith("xpath(") ) {
			expression = expression.substring(6, expression.length() - 1);
			return new String[] { "xpath", expression };
		}
		return new String[] { "UNKNOWN", "" };
	}

	private String getResultClassName() {
		// 返回逻辑接口包名.命令字 + "Result",
		// 例"com.xwtech.xwecp.service.logic.pojo.UserInfoQueryResult
		String pojoPackage = AppConfig.getConfigValue("service_impl", "pojoPackage");
		pojoPackage = pojoPackage == null ? "com.xwtech.xwecp.service.logic.pojo" : pojoPackage;
		String cmd = this.getServiceConfig().getCmd();
		return pojoPackage + "." + cmd + "Result";
	}

	/**
	 * 根据正则表达式获取相匹配报文模板
	 * 
	 * @return
	 */
	private TeletextMapping getBossTeletextMapping() {
		/**
		 * 2011-11-15 maofw 修改 获得渠道信息
		 */
		ServiceMessage requestServiceMessage = this.getServiceInfo().getServiceMessage();
		String channel = (requestServiceMessage == null || requestServiceMessage.getHead() == null) ? "" : requestServiceMessage.getHead().getChannel();
		/**
		 * end
		 */

		for (int i = 0; i < this.serviceConfig.getImpl().getTeletextMapping().size(); i++) {
			TeletextMapping m = this.serviceConfig.getImpl().getTeletextMapping().get(i);
			String paramName = m.getParamName();
			String match = m.getMatch();
			if (paramName == null || paramName.trim().length() == 0
					|| match == null || match.trim().length() == 0
					|| "*".equals(paramName.trim()) || "*".equals(match.trim())) {
				return m;
			}
			
	    	

			/**
			 * 2011-11-15 maofw 修改 获得渠道信息
			 */
	    	
	        if ("$sys_channel_param".equals(paramName) && channel.equals(match)) 
	        {
	        	String port = SavePortUtil.getInstance().port;
	        	String url = SavePortUtil.getInstance().url;
		    	String cmd = requestServiceMessage.getHead().getCmd();
		    	
		    	//若网厅集团探测的时候，将本机的端口换成10009 便于下面逻辑判断
		    	if(OBSH_JTTC_URL.equals(url))
		    	{
		    		port = WAP_JTTC_PORT;
		    	}
	        	//不是wap的服務器端口的時候
				if(!WAP_JTTC_PORT.equals(port) )
				{
					return m;
				}
				//是集團探測的時候 需要判斷下搶流量紅包的幾個接口
				else if(cmds.indexOf(cmd) != -1)
				{
					continue;
				}
				//是集團探測和不是搶流量紅包割接的接口返回
				else
				{
					return m;
				}
			}
//			if ("$sys_channel_param".equals(paramName) && channel.equals(match)) {
//				
//				return m;
//			}
			/**
			 * end
			 */

			/*
			 * RequestParameter parameter = this.getParameter(paramName);
			 * if(parameter != null) { String value =
			 * parameter.getParameterValue().toString();
			 * if(value.matches(match)) { return m; } }
			 */
			// 加入函数支持
			String paramValue = "";
			try {
				StringBuffer valBuf = new StringBuffer();
				boolean b = this.evaluateFunctions(paramName, valBuf);
				if ( !b ) {
					RequestParameter parameter = this.getParameter(paramName);
					if ( parameter != null ) {
						paramValue = parameter.getParameterValue().toString();
					}
				}
				else {
					paramValue = valBuf.toString();
				}
			}
			catch (Exception e) {
				logger.error("装配执行函数解析出错!");
				logger.error(e, e);
			}
			// 将函数返回结果再和正则表达式作匹配
			if ( paramValue.matches(match) ) { return m; }
		}
		return null;
	}

	private boolean evaluateFunctions(String in, StringBuffer retBuf) throws Exception {
		if ( expressionEvaluateBuffer.get(in) != null ) {
			retBuf.append(expressionEvaluateBuffer.get(in));
			return true;
		}
		String pStr = "\\%.{2,200}[\\(.*\\)]++";
		String groupStr = "\\%(.*?\\()(.*)?\\)";
		Pattern groupPattern = Pattern.compile(groupStr);
		Pattern p = Pattern.compile(pStr, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(in);
		StringBuffer buf = new StringBuffer();
		boolean flag = false;
		while (matcher.find()) {
			String tmp = matcher.group();
			Matcher m = groupPattern.matcher(tmp);
			if ( m.find() ) {
				if ( m.groupCount() == 2 ) {
					flag = true;
					String functionName = m.group(1);
					functionName = functionName.substring(0, functionName.length() - 1); // 去掉结尾的"("
					String parameter = m.group(2);
					String value = "";
					if ( parameter.startsWith("\"") && parameter.endsWith("\"") ) // 如果直接是字串
					{
						parameter = parameter.substring(1, parameter.length() - 1); // 去掉两端的引号
						// 执行函数
						value = this.executeExternalFunction(functionName, parameter);
					}
					else if ( parameter.matches(pStr) ) // 如果参数本身又是一个函数
					{
						value = this.recursiveEvaluateFunction(functionName, parameter);
					}
					else // 如果括号里不是固定字串也不是函数, 则把这个当成输入变量处理
					{
						if ( parameter.indexOf('|') > 0 ) {// 多个参数拼接
							String[] parameters = parameter.split("\\|");
							StringBuffer strBuf = new StringBuffer();
							for (int i = 0; i < parameters.length; i++) {
								RequestParameter rp = this.getParameter(parameters[i]);
								if ( rp.getParameterValue() != null ) {
									if ( strBuf.length() != 0 )
										strBuf.append("|");
									strBuf.append(rp.getParameterValue());
								}
								// parameter = rp.getParameterValue() == null ?
								// null : rp.getParameterValue().toString();
							}
							parameter = strBuf.toString();
						}
						else {// 唯一参数
							RequestParameter rp = this.getParameter(parameter);
							parameter = rp.getParameterValue() == null ? null : rp.getParameterValue().toString();
						}
						value = this.executeExternalFunction(functionName, parameter);
					}
					matcher.appendReplacement(buf, value);
				}
			}
		}
		matcher.appendTail(buf);
		retBuf.delete(0, retBuf.length());
		retBuf.append(buf);
		if ( flag ) {
			expressionEvaluateBuffer.put(in, retBuf.toString());
		}
		// return ret.replaceAll("\\\\$", "$");
		return flag;
	}

	private String recursiveEvaluateFunction(String functionName, String parameter) {
		if ( parameter.startsWith("\"") && parameter.endsWith("\"") ) {
			parameter = parameter.substring(1, parameter.length() - 1); // 去掉两端的引号
			return this.executeExternalFunction(functionName, parameter);
		}
		else {
			String groupStr = "\\%(.*?\\()(.*)?\\)";
			Pattern groupPattern = Pattern.compile(groupStr);
			Matcher m = groupPattern.matcher(parameter);
			if ( m.find() ) {
				if ( m.groupCount() == 2 ) {
					String subName = m.group(1);
					subName = subName.substring(0, subName.length() - 1); // 去掉结尾的"("
					String subParam = m.group(2);
					return this.executeExternalFunction(functionName, recursiveEvaluateFunction(subName, subParam));
				}
			}
			return functionName + "(" + parameter + ")";
		}
	}

	private String executeExternalFunction(String functionName, String parameter) {
		IExternalFunctionExecutor functionExecutor = this.externalFunctionMap.get(functionName);
		if ( functionExecutor != null ) { return functionExecutor.execute(parameter); }
		logger.warn("未配置函数[" + functionName + "]!");
		return functionName + "(" + parameter + ")";
	}

	private RequestParameter getParameter(String name) {
		for (int i = 0; i < this.params.size(); i++) {
			RequestParameter param = this.params.get(i);
			if ( param.getParameterName().equals(name) ) { return param; }
		}
		return null;
	}

	private ServiceOutputField getOutputFieldInfo(String name) {
		for (int i = 0; i < this.getServiceConfig().getOutput().getOutputFields().size(); i++) {
			if ( name.equals(this.getServiceConfig().getOutput().getOutputFields().get(i).getName()) ) { return this.getServiceConfig().getOutput().getOutputFields().get(i); }
		}
		return null;
	}

	protected BaseServiceInvocationResult executeDirectly(String accessId) throws ServiceException {
		String implClass = this.serviceConfig.getImpl().getImplClass();
		System.out.println("==[WARN]==>直接写代码实现 :" + implClass);
		Class clazz = null;
		ILogicalService service = null;
		try {
			PerformanceTracer pt = PerformanceTracer.getInstance();
			long n = pt.addBreakPoint();
			clazz = Class.forName(implClass);
			service = (ILogicalService) clazz.newInstance();
			pt.trace("实例化逻辑接口实现类...", n);
		}
		catch (ClassNotFoundException e) {
			logger.error("逻辑接口实现类[" + implClass + "]没有找到!");
			throw new ServiceException(e);
		}
		catch (InstantiationException e) {
			logger.error("逻辑接口实现类[" + implClass + "]实例化失败!");
			throw new ServiceException(e);
		}
		catch (IllegalAccessException e) {
			logger.error("逻辑接口实现类[" + implClass + "]实例化失败!");
			throw new ServiceException(e);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		return service.executeService(accessId, this.serviceConfig, this.params);
	}

	public BaseServiceInvocationResult execute(String accessId) throws ServiceException {
		PerformanceTracer pt = PerformanceTracer.getInstance();
		long n = 0;
		BaseServiceInvocationResult ret = null;
		if ( this.serviceConfig != null ) {
			if ( serviceConfig.getImpl() != null && serviceConfig.getImpl().isDirectImpl() ) {
				n = pt.addBreakPoint();
				ret = this.executeDirectly(accessId);
				pt.trace("直接执行实现的java类...", n);
				return ret;
			}
			else if ( serviceConfig.getImpl() != null && (!serviceConfig.getImpl().isDirectImpl()) ) {
				n = pt.addBreakPoint();
				ret = this.executeByConfig(accessId);
				pt.trace("根据配置实现...", n);
				return ret;
			}
			else {
				throw new ServiceException("接口配置错误！");
			}
		}
		return null;
	}

	public void initialize(ServiceConfig config, List<RequestParameter> params) throws ServiceException {
		this.serviceConfig = config;
		this.params = params;
		// 根据配置的校验表达式, 校验参数
		this.validateInput();
	}

	protected void validateInput() throws IllegalServiceInputException {
		for (int i = 0; i < this.serviceConfig.getInput().getParams().size(); i++) {
			ServiceInputParameter input = this.serviceConfig.getInput().getParams().get(i);
			String regular = input.getRegular();
			if ( regular != null && regular.trim().length() > 0 ) {
				RequestParameter param = this.getParameter(input.getName());
				if ( param != null ) {
					Object val = param.getParameterValue();
					if ( val != null ) {
						String sVal = val.toString();
						if ( !sVal.matches(regular) ) {
							logger.error("参数校验失败!");
							throw new IllegalServiceInputException("请求参数校验失败, 命令字[" + this.getServiceConfig().getCmd() + "],输入参数名[" + input.getName() + "], 输入值[" + val + "],验证方式[" + regular + "]!");
						}
					}
				}
				else {
					throw new IllegalServiceInputException("请求参数校验失败, 命令字[" + this.getServiceConfig().getCmd() + "],输入参数名[" + input.getName() + "], 输入值[(NULL)],验证方式[" + regular + "]!");
				}
			}
		}
	}

	public static class StringTeletext implements IStreamableMessage {
		protected String teletext;
		protected String accessId;
		protected String identity;
		protected RemoteCallContext remoteCallContext;

		public StringTeletext(String s, String accessId, String identity, RemoteCallContext remoteCallContext) {
			this.teletext = s;
			this.accessId = accessId;
			this.identity = identity;
			this.remoteCallContext = remoteCallContext;
		}

		protected RemoteCallContext getRemoteCallContext() {
			return remoteCallContext;
		}

		protected void setRemoteCallContext(RemoteCallContext remoteCallContext) {
			this.remoteCallContext = remoteCallContext;
		}

		public String getIdentity() {
			return identity;
		}

		public void setIdentity(String identity) {
			this.identity = identity;
		}

		public RemoteCallContext getContext() {
			return this.remoteCallContext;
		}

		public String toMessage() {
			return this.teletext;
		}

		public String getAccessId() {
			return accessId;
		}

		public void setAccessId(String accessId) {
			this.accessId = accessId;
		}

		public String getTeletext() {
			return teletext;
		}

		public void setTeletext(String teletext) {
			this.teletext = teletext;
		}

	}

	public Map<String, IExternalFunctionExecutor> getExternalFunctionMap() {
		return externalFunctionMap;
	}

	public void setExternalFunctionMap(Map<String, IExternalFunctionExecutor> externalFunctionMap) {
		this.externalFunctionMap = externalFunctionMap;
	}
}