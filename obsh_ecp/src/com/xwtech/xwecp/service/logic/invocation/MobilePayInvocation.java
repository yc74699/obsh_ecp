package com.xwtech.xwecp.service.logic.invocation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mpi.client.exception.PayException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.springframework.context.ApplicationContext;

import chinapay.PrivateKey;
import chinapay.SecureLink;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY060066Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ConfigurationRead;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * 银联卡充值
 * 
 * @author 邵琪
 * 
 */
public class MobilePayInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(MobilePayInvocation.class);
	
	private static String merUrl = ConfigurationRead.getInstance().getValue("unionpay_pageRetUrl");
	//回跳地址
	private static String bgRetUrl_18 =  ConfigurationRead.getInstance().getValue("unionpay_bgRetUrl_18");

	//数字签名文件路径
	private static String signFilePath = ConfigurationRead.getInstance().getValue("signFilePath");
	
	private static String extenName= ".key";

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;

	private Map<String, String> dic = null;

	public MobilePayInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
		dic = new HashMap<String, String>();
		//新商户号
		dic.put("11", "808080123100009");
		dic.put("12", "808080123100007");
		dic.put("13", "808080123100002");
		dic.put("14", "808080123100001");
		dic.put("15", "808080123100010");
		dic.put("16", "808080123100012");
		dic.put("17", "808080123100003");
		dic.put("18", "808080123100011");
		dic.put("19", "808080123100005");
		dic.put("20", "808080123100013");
		dic.put("21", "808080123100006");
		dic.put("22", "808080123100008");
		dic.put("23", "808080123100004");

	}

	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		QRY060066Result res = new QRY060066Result();
		String userCity = null;
		String userId = null;
		//String orderId =null;
		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			userCity = (String)this.getParameters(params, "context_ddr_city");
			userId = (String)this.getParameters(params, "context_user_id");
			String toNumber = (String) getParameters(params, "phoneNum");
			if (userCity == null || "".equals(userCity) || userId == null || "".equals(userId)) {
				BaseResult getCityResult = this.getCity(accessId, config, params);
				if (LOGIC_SUCESS.equals(getCityResult.getResultCode())) {
					Map map = (Map)getCityResult.getReObj();
					userCity = (String)map.get("city");
					userId = (String)map.get("userId");
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(getCityResult.getErrorCode());
					res.setErrorMessage(getCityResult.getErrorMessage());
					return res;
				}
			}
			
			this.setParameter(params, "context_ddr_city", userCity);
			this.setParameter(params, "fixed_ddr_city", userCity);
			this.setParameter(params, "context_user_id", userId);
			this.setParameter(params, "qryMonth", DateTimeUtil.getTodayChar6());
		
			params.add(new RequestParameter("cityId", userCity));
			
			// 生成流水
			BaseResult generateResult = this.generateOrder(accessId, config, params, res, "cc_createbanktaskfornet");
			if (LOGIC_SUCESS.equals(generateResult.getResultCode())) {
				//TODO
				//String bossDate = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssS");
				String bossDate = res.getBossDate();
				String unionPaySrl = res.getUnionPaySrl();
				int amount = Integer.parseInt((String) getParameters(params, "amount"));// 订单金额
				amount *= 100;
				String checkSrl = null;
				//待业务检查的订单号
				checkSrl = unionPaySrl;
				
				// 设置校验参数
				params.add(new RequestParameter("checkSrl", checkSrl));
				params.add(new RequestParameter("bossDate", bossDate));
				params.add(new RequestParameter("amount", amount));
				
				BaseResult checkOrderResult = null;
				
				//业务检查依然用以前的接口
				String bossCheckTemplate = "cc_bucheckdeal_356_hw";
				this.setParameter(params, "source", "1");
				/**增加支付宝方式   2014年10月11日 **/
				int payType = (Integer) getParameter(params, "payType").getParameterValue();
				if(1 == payType)
				{
					this.setParameter(params, "payType", "15");
				}
				else
				{
					this.setParameter(params, "payType", "5"); //5：银联 15：支付宝
				}
				
				//业务检查
				checkOrderResult = this.checkOrder(accessId, config, params, res, bossCheckTemplate);
				if (LOGIC_SUCESS.equals(checkOrderResult.getResultCode())) {
					// 设置tran
					setTranValue(res, userCity, unionPaySrl, amount, bossDate,toNumber,payType);
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(checkOrderResult.getErrorCode());
					res.setErrorMessage(checkOrderResult.getErrorMessage());
				}
			} else {
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(generateResult.getErrorCode());
				res.setErrorMessage(generateResult.getErrorMessage());
			}
			
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}



	/**
	 * 设置返回的Tran值
	 * 
	 * @param res
	 * @param userCity
	 * @param unionPaySrl
	 * @param amount
	 * @param bossDate
	 * @throws PayException
	 * @throws Exception
	 */
	protected void setTranValue(QRY060066Result res, String userCity, String unionPaySrl, int amount, String bossDate,String toNumber,int payType)
			throws PayException, Exception {
	
		String curyId = "156";
		String transType = "0001";
		//增加支付宝的返回
		if( 1 == payType)
		{
		
			res.setOrdId(unionPaySrl);
			String strAmount = formatUnionPayAmount(amount);
			res.setTransAmt(strAmount);
			res.setCuryId(curyId);
			res.setTransDate(bossDate.substring(0, 8));
			res.setOrdId(unionPaySrl);
			return;
		}
		//华为用回调地址 
		String merId = dic.get(userCity);
		
		try {
			PrivateKey key=new PrivateKey();
			boolean flag;
			flag = key.buildKey(merId, 0, signFilePath + merId + extenName);
			String strAmount = formatUnionPayAmount(amount);
			String checkValue = "";
			if (flag){
				SecureLink t = new chinapay.SecureLink (key);
				String signMsg = "";
				//bossdata  TODO
				signMsg = merId +  unionPaySrl + strAmount + curyId + bossDate.substring(0, 8) + transType + bossDate + "|" + toNumber;
				checkValue = t.Sign(signMsg);
				res.setVersion("20070129");
				res.setChkValue(checkValue);
				res.setBgRetUrl(bgRetUrl_18);
				res.setPageRetUrl(merUrl);
				res.setMerId(merId);
				res.setOrdId(unionPaySrl);
				res.setTransAmt(strAmount);
				res.setCuryId(curyId);
				res.setTransDate(bossDate.substring(0, 8));
				res.setTransType(transType);
				res.setPriv1(bossDate + "|" + toNumber);
				res.setUnionPaySrl(unionPaySrl);
				}else{
					res.setErrorCode("-100001"); // TODO
					res.setErrorMessage("生成数字签名失败");
				}
		} catch (Exception e) {
			res.setErrorCode("-100003"); // TODO
			res.setErrorMessage("电话支付充值失败");
		}
	}

	/**
	 * 查询用户信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult getCity(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69", params);

			logger.debug(" ====== 查询用户信息请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetusercust_69", super.generateCity(params)));
			logger.debug(" ====== 查询用户信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY060066", "cc_cgetusercust_69", errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					Map retMap = new HashMap();
					
					XPath xpath = XPath.newInstance("/operation_out/content/user_info/user_city");
					String city = ((Element) xpath.selectSingleNode(root)).getText();
					xpath = XPath.newInstance("/operation_out/content/user_info/user_id");
					String userId = ((Element) xpath.selectSingleNode(root)).getText();
					retMap.put("city", city);
					retMap.put("userId", userId);
					res.setReObj(retMap);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 创建订单
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult generateOrder(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final QRY060066Result result, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		String unionPaySrl = "0";
		String bossDate = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

			logger.debug(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate, super.generateCity(params)));
			logger.debug(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY060066", bossTemplate, errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
					XPath xpath = XPath.newInstance("/operation_out/content/buoperatingdetail_clt_sys_date");
					bossDate = ((Element) xpath.selectSingleNode(root)).getText();
					xpath = XPath.newInstance("/operation_out/content/taskoid");
					unionPaySrl = ((Element) xpath.selectSingleNode(root)).getText();
					result.setBossDate(bossDate);
					result.setUnionPaySrl(unionPaySrl);

				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * check订单
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult checkOrder(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final QRY060066Result result, final String bossTemplate) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(bossTemplate, params);

			logger.info(" ====== 创建订单请求报文 ======\n" + reqXml);

			rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, bossTemplate, super.generateCity(params)));
			logger.info(" ====== 创建订单返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml)) {
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");

				if (!BOSS_SUCCESS.equals(errCode)) {
					ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY060066", bossTemplate, errCode);
					if (null != errDt) {
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode) ? LOGIC_SUCESS : LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}

	private String formatUnionPayAmount(int amount){
		String strAmount = String.valueOf(amount);
		if(!StringUtils.isBlank(strAmount)){
			while(strAmount.length() < 12){
				strAmount = "0" + strAmount;
			}
			
		}
		return strAmount;
	}
	
}