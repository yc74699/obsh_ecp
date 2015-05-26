package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY040021Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 固定电话和宽带账号登陆
 * @author Tkk
 *
 */
public class QueryNetUserInfoOrFixedPhoneUserInfoInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(QueryNetUserInfoOrFixedPhoneUserInfoInvocation.class);

	/**
	 * 组装报文
	 */
	private BossTeletextUtil bossTeletextUtil = null;

	/**
	 * 调用Boss接口
	 */
	private IRemote remote = null;
	
	/**
	 * 宽带账号登陆
	 */
	private static String NET_USER_LOGIN = "cc_cchkinternetuser_410";
	
	/**
	 * 固定电话登陆
	 */
	private static String FIXED_USER_LOGIN = "cc_cqryteluser_406";
	
	private WellFormedDAO wellFormedDAO;
	
	public QueryNetUserInfoOrFixedPhoneUserInfoInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		QRY040021Result result = new QRY040021Result();
		
		//先获取地市
		String cityName = null;
		for(RequestParameter param : params){
			if("city".equals(param.getParameterName())){
				cityName = (String) param.getParameterValue();
				break;
			}
		}
		
		//地市转换为boss编码
		String city = wellFormedDAO.getBossCity(cityName);
		for(RequestParameter param : params){
			if("fixed_ddr_city".equals(param.getParameterName())){
				param.setParameterValue(city);
				break;
			}
		}
		
		//判断是宽带登陆还是固话登陆
		boolean isNetUserLogin = true;
		for(RequestParameter param : params){
			if("loginType".equals(param.getParameterName())){
				if("1".equals(param.getParameterValue())){
					isNetUserLogin = true;
				}
				else{
					isNetUserLogin = false;
				}
				break;
			}
		}
		
		//宽带登陆发送报文
		try {
			if(isNetUserLogin){
				String rsp = (String)this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(NET_USER_LOGIN, params), accessId, NET_USER_LOGIN, this.generateCity(params)));
				getNetUserInfo(result, rsp);
			}
			else{
				String rsp = (String)this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(FIXED_USER_LOGIN, params), accessId, FIXED_USER_LOGIN, this.generateCity(params)));
				getFixPhoneUserInfo(result, rsp);
			}
			
			//转换地址
			switchCityAndCountyName(result);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}
	
	/**
	 * 转换地址
	 * @param result
	 */
	private void switchCityAndCountyName(QRY040021Result result) {
		//获取地市
		String cityBossNum = result.getCity();
		
		//获取县
		String countyBossNum = result.getCounty();
		
		if(!"".equals(cityBossNum) && !"".equals(countyBossNum)){
			//本地保存的值
			String [] cityNameAndNum = wellFormedDAO.getCityNameAndNum(cityBossNum).split(",");
			
			String [] countyNameAndNum = wellFormedDAO.getCityNameAndNum(countyBossNum).split(",");
			
			//进行转换
			result.setCityName(cityNameAndNum[0]);
			result.setCounty(countyNameAndNum[0]);
			result.setCity(cityNameAndNum[1]);
			//result.setCounty(countyNameAndNum[1]);
		}
	}

	/**
	 * 获取固定电话信息
	 * @param result
	 * @param rsp
	 */
	private void getFixPhoneUserInfo(QRY040021Result result, String rsp) {
		try {
			Document doc = DocumentHelper.parseText(rsp);
			String errorCode = doc.selectSingleNode("/operation_out/response/resp_code").getText();
			String errorMsg = doc.selectSingleNode("/operation_out/response/resp_desc").getText();
			if("0000".equals(errorCode)){
				result.setResultCode(LOGIC_SUCESS);
				
				//用户Id
				Node node = doc.selectSingleNode("/operation_out/content/cteluserdt/teluser_tel_user_id");
				result.setUserId(getNodeText(node));
				
				//用户名称
				node = doc.selectSingleNode("/operation_out/content/cpersonalcustomerdt/cust_name");
				result.setUserName(getNodeText(node));
				
				//使用开始日期
				node = doc.selectSingleNode("/operation_out/content/cpersonalcustomerdt/cust_create_time");
				result.setApplyDate(getNodeText(node));

				//地市
				node = doc.selectSingleNode("/operation_out/content/cpersonalcustomerdt/cust_city_id");
				result.setCity(getNodeText(node));
				
				//显市
				node = doc.selectSingleNode("/operation_out/content/cpersonalcustomerdt/cust_county_id");
				result.setCounty(getNodeText(node));
			}
			else
			{
				result.setResultCode(LOGIC_ERROR);
				result.setErrorMessage(errorMsg);
			}
		} catch (DocumentException e) {
			logger.error(e, e);
		}
		
	}

	/**
	 * 获取宽带账号信息
	 * @param result
	 * @param rsp
	 */
	private void getNetUserInfo(QRY040021Result result, String rsp) {
		try {
			Document doc = DocumentHelper.parseText(rsp);
			String errorCode = doc.selectSingleNode("/operation_out/response/resp_code").getText();
			String errorMsg = doc.selectSingleNode("/operation_out/response/resp_desc").getText();
			if("0000".equals(errorCode)){
				result.setResultCode(LOGIC_SUCESS);
				
				//用户Id
				Node node = doc.selectSingleNode("/operation_out/content/internetuser_limit_bandwidth/cinternetuserdt/internet_user_id");
				result.setUserId(getNodeText(node));
				
				//用户名称
				node = doc.selectSingleNode("/operation_out/content/internetuser_limit_bandwidth/cinternetuserdt/customer_name");
				result.setUserName(getNodeText(node));
				
				//使用开始日期
				node = doc.selectSingleNode("/operation_out/content/internetuser_limit_bandwidth/cinternetuserdt/apply_date");
				result.setApplyDate(getNodeText(node));

				//使用结束日期
				node = doc.selectSingleNode("/operation_out/content/internetuser_limit_bandwidth/cinternetuserdt/suspend_date");
				result.setSuspendDate(getNodeText(node));
				
				//地市
				node = doc.selectSingleNode("/operation_out/content/internetuser_limit_bandwidth/cinternetuserdt/city_id");
				result.setCity(getNodeText(node));
				
				//显市
				node = doc.selectSingleNode("/operation_out/content/internetuser_limit_bandwidth/cinternetuserdt/county_id");
				result.setCounty(getNodeText(node));
				
				//地址
				node = doc.selectSingleNode("/operation_out/content/internetuser_limit_bandwidth/cinternetuserdt/address");
				result.setAddress(getNodeText(node));
			}
			else
			{
				result.setResultCode(LOGIC_ERROR);
				result.setErrorMessage(errorMsg);
			}
		} catch (DocumentException e) {
			logger.error(e, e);
		}
	}
	
	
	private String getNodeText(Node node){
		return node == null ? "" : node.getText();
	}

}
