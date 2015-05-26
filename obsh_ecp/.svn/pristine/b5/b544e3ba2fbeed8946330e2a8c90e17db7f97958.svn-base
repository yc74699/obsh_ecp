package com.xwtech.xwecp.service.logic.invocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mpi.client.exception.PayException;
import org.apache.log4j.Logger;
import com.hisun.iposm.HiiposmUtil;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL040054Result;
import com.xwtech.xwecp.service.logic.pojo.RequestData;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.StringUtil;
import com.xwtech.xwecp.util.XMLUtil;

/**
 * 手机支付充值
 * @author chenxm
 * 2013-03-18
 */
public class PhonePayInvocation extends BaseInvocation implements ILogicalService {
	private static final Logger logger = Logger.getLogger(PhonePayInvocation.class);

	private final String signType = "MD5";
	
	//编码格式默认00：GBK
	private final String characterSet = "00";
	
	//手机支付请求平台地址
//	private final String req_url ="https://211.138.236.208/ips/cmpayService";
	
	private final String req_url = "https://ipos.10086.cn/ips/cmpayService";
	
	//测试地址前置机URL
//	private final String notifyUrl = "http://221.178.251.151/jsmobilepay/servlet/JsMobilepayRtn";
	
	//现网地址
	private final String notifyUrl = "http://221.178.251.158/jsmobilepay/servlet/JsMobilepayRtn";
	
	//回调地址网厅页面
	private final String callbackUrl = "http://service.js.10086.cn/wscz.jsp#WSCZYL";

	//访问ip
	private final String ipAddress = "211.138.196.125";
	
	//商户号
	private String merchantId = "";
	
	//地市key
	private String dataKey = "";
	
	//地市商户号
	private Map<String, String> dicPid = null;
	
	//地市商户key
	private Map<String, String> dicKey = null;
	
	private String userCity = "";
	
	private String requestId = "";
	
	public PhonePayInvocation() 
	{
		//各个地市pid
		dicPid = new HashMap<String, String>();
		dicPid.put("11", "888009948140113");
		dicPid.put("12", "888009948140113");
//		dicPid.put("12", "888009941110054"); //测试pid
		dicPid.put("13", "888009948140113");
		dicPid.put("14", "888009948140113");
		dicPid.put("15", "888009948140113");
		dicPid.put("16", "888009948140113");
		dicPid.put("17", "888009948140113");
		dicPid.put("18", "888009948140113");
		dicPid.put("19", "888009948140113");
		dicPid.put("20", "888009948140113");
		dicPid.put("21", "888009948140113");
		dicPid.put("22", "888009948140113");
		dicPid.put("23", "888009948140113");
		
		
		//各个地市key
		dicKey = new HashMap<String, String>();
		dicKey.put("11", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("12", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
//		dicKey.put("12", "xWUVkqhFY6IWm4bO3N0ILWUhDzyQ6UCiuTGQqADKgUzTZmSmZDcrGBBYFgbxDf5d"); //测试key
		dicKey.put("13", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("14", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("15", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("16", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("17", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("18", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("19", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("20", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("21", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("22", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
		dicKey.put("23", "cBl25FMi2meDvrIAj0pQpeNKD6UM80rqaNX0LhoesbqmZf9qibzvy6hceHxrr10l");
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params) {
		DEL040054Result res = new DEL040054Result();
		try {
			res.setResultCode(LOGIC_ERROR);
			res.setErrorMessage("");
			// 1. 生成订单流水号
			generateOrderDoneCode(accessId, config, params, res, "cc_bugetcltsrl_357");
			if (LOGIC_SUCESS.equals(res.getResultCode())) 
			{
				// 2. 充值回调
				payOrderBack(accessId, config, params, res,"cc_phonepaycheckdeal_250");
				if (LOGIC_SUCESS.equals(res.getResultCode())) 
				{
					dataKey = dicKey.get(userCity);
					merchantId = dicPid.get(userCity);
					// 3. 设置返回tran值
					setTranValue(res, params);
				}
			} 
		} catch (Exception e) 
		{
			logger.error(e, e);
		}
		return res;
	}

	/**
	 * 设置返回的Tran值
	 * @param res
	 * @param userCity
	 * @param unionPaySrl
	 * @param amount
	 * @param bossDate
	 * @throws PayException
	 * @throws Exception
	 */
	protected void setTranValue(DEL040054Result res, List<RequestParameter> params)throws PayException, Exception 
	{
		String flag = this.getParameters(params,"flag").toString();
		//即时到账(双向确认)
		if ("0".equals(flag)) 
		{
			setTranValue4Dodirect(res, params);
		} 
		//直接支付(银行网关)
		else 
		{
			setTranValue4GWdirect(res, params);
		}
	}

	private void setTranValue4GWdirect(DEL040054Result res,
			List<RequestParameter> params)
	{
		String signData = generateSignData(res,params,"GWDirectPay");
		//数据签名，hmac为签名后的消息摘要
		res.setRequestData(buildRequestData(signData,res,params,"GWDirectPay"));
		res.setRequestAction(req_url);

	}

	private RequestData buildRequestData(String signData, DEL040054Result res,
			List<RequestParameter> params, String type) 
	{
		HiiposmUtil util = new HiiposmUtil();
		String hmac = util.MD5Sign(signData, dataKey);	
		
		RequestData data = new RequestData();
		data.setAmount((String) this.getParameters(params, "amount"));
		data.setBankAbbr((String) this.getParameters(params, "bankcode"));
		data.setCurrency("00");
		data.setOrderDate(DateTimeUtil.getTodayChar8());
		data.setOrderId(res.getOperStr());
		data.setPeriod("7");
		data.setPeriodUnit("02");
		data.setMerchantAbbr("");//商户展示名称
		data.setProductDesc("");//商户描述
		data.setProductId("");//商品编号
		data.setProductName("话费");//商品名称
		data.setProductNum("");//商品数量
//		phoneNum
		data.setReserved1((String) this.getParameters(params, "phoneNum"));
		data.setReserved2("");
		data.setUserToken((String) this.getParameters(params, "phoneNum"));
		data.setShowUrl("");
		data.setCouponsFlag("00");
		data.setVersion("2.0.0");
		data.setRequestId(requestId);
		data.setMerAcDate(DateTimeUtil.getTodayChar8());
		data.setCharacterSet(characterSet);
		data.setCallbackUrl(callbackUrl);
		data.setNotifyUrl(notifyUrl);
		data.setIpAddress(ipAddress);
		data.setMerchantId(merchantId);
		data.setSignType(signType);
		data.setType(type);
		data.setHmac(hmac);
		return data;
	}

	private void setTranValue4Dodirect(DEL040054Result res,
			List<RequestParameter> params)
	{
		try
		{
			//签名报文
			String signData = generateSignData(res,params,"DirectPayConfirm");
			
			// 数据签名
			HiiposmUtil util = new HiiposmUtil();
			String hmac = util.MD5Sign(signData, dataKey);
			// -- 请求数据
			String requestData = generaterequestData(res,params,"DirectPayConfirm");
			// -- 带上消息摘要
			requestData = "hmac=" + hmac + "&" + requestData;
			logger.info("请求数据requestData：" + requestData);
			
			res.setRequestSignData(requestData);
			res.setRequestAction(req_url);
//			// 发起http请求，并获取响应报文
//			String resp = util.sendAndRecv(req_url, requestData, characterSet);
//			logger.info("响应报文：" + resp);
//			
//			// 响应码
//			String code = util.getValue(resp, "returnCode");
//			// 下单交易成功
//			if (!code.equals("000000")) 
//			{
//				logger.info("下单错误:"+ code+ URLDecoder.decode(util.getValue(resp, "message"),"UTF-8"));
//				setCommonResult(res, "1", code+ URLDecoder.decode(util.getValue(resp, "message")));
//				return;
//			}
//
//			// 获得手机支付平台的消息摘要，用于验签,
//			String hmac1 = util.getValue(resp, "hmac");
//			String vfsign = util.getValue(resp, "merchantId")
//							+ util.getValue(resp, "requestId")
//							+ util.getValue(resp, "signType")
//							+ util.getValue(resp, "type")
//							+ util.getValue(resp, "version")
//							+ util.getValue(resp, "returnCode")
//							+ URLDecoder.decode(util.getValue(resp, "message"), "UTF-8")
//							+ util.getValue(resp, "payUrl");
//			
//			// -- 验证签名
//			boolean flag = false;
//			flag = util.MD5Verify(vfsign, hmac1, dataKey);
//			if (!flag) 
//			{
//				logger.info("验签失败");
//				setCommonResult(res, "1", "验签失败");
//				return;
//			}
//			String payUrl = util.getValue(resp, "payUrl");
//			String submit_url = util.getRedirectUrl(payUrl);
//			logger.info("submit_url:" + submit_url);
//			res.setRequestAction(submit_url);
		} catch (Exception e) 
		{
			logger.error(e, e);
			setCommonResult(res, "-100002", "手机支付签名验证失败");
		}		
	}

	private String generaterequestData(DEL040054Result res, List<RequestParameter> params,String type)
	{
		String amount = (String) this.getParameters(params, "amount");
		String bankAbbr = (String) this.getParameters(params, "bankcode");
		String currency = "00";
		String orderDate = DateTimeUtil.getTodayChar8();
		String orderId = res.getOperStr();
		String period = "7";
		String periodUnit = "02";
		String merchantAbbr = "";//商户展示名称
		String productDesc = "";//商户描述
		String productId = "";//商品编号
		String productName = "话费";//商品名称
		String productNum = "";//商品数量
		String reserved1 = (String) this.getParameters(params, "phoneNum");
		String reserved2 = "";
		String userToken = (String) this.getParameters(params, "phoneNum");
		String showUrl = "";
		String couponsFlag = "00";
		
		String version = "2.0.0";
		String merAcDate = DateTimeUtil.getTodayChar8();
		
		String requestData = "characterSet=" + characterSet + "&callbackUrl="
							+ callbackUrl + "&notifyUrl=" + notifyUrl + "&ipAddress="
							+ ipAddress + "&merchantId=" + merchantId + "&requestId="
							+ requestId + "&signType=" + signType + "&type=" + type
							+ "&version=" + version + "&amount=" + amount
							+ "&bankAbbr=" + bankAbbr + "&currency=" + currency
							+ "&orderDate=" + orderDate + "&orderId=" + orderId
							+ "&merAcDate=" + merAcDate + "&period=" + period
							+ "&periodUnit=" + periodUnit + "&merchantAbbr="
							+ merchantAbbr + "&productDesc=" + productDesc
							+ "&productId=" + productId + "&productName=" + productName
							+ "&productNum=" + productNum + "" + "&reserved1="
							+ reserved1 + "&reserved2=" + reserved2 + "&userToken="
							+ userToken + "&showUrl=" + showUrl + "&couponsFlag="+ couponsFlag;
		return requestData;
	}

	private String generateSignData(DEL040054Result res, List<RequestParameter> params, String type) {
		String amount = (String) this.getParameters(params, "amount");
		String bankAbbr = (String) this.getParameters(params, "bankcode");
		String currency = "00";
		String orderDate = DateTimeUtil.getTodayChar8();
		String orderId = res.getOperStr();
		String period = "7";
		String periodUnit = "02";
		String merchantAbbr = "";//商户展示名称
		String productDesc = "";//商户描述
		String productId = "";//商品编号
		String productName = "话费";//商品名称
		String productNum = "";//商品数量
		String reserved1 = (String) this.getParameters(params, "phoneNum");
		String reserved2 = "";
		String userToken = (String) this.getParameters(params, "phoneNum");
		String showUrl = "";
		String couponsFlag = "00";
		
		String version = "2.0.0";
		requestId = String.valueOf(System.currentTimeMillis());
		String merAcDate = DateTimeUtil.getTodayChar8();
		//数据签名报文
		String signData = characterSet + callbackUrl + notifyUrl
						+ ipAddress + merchantId + requestId + signType + type
						+ version + amount + bankAbbr + currency + orderDate
						+ orderId + merAcDate + period + periodUnit + merchantAbbr
						+ productDesc + productId + productName + productNum
						+ reserved1 + reserved2 + userToken + showUrl + couponsFlag;
		return signData;
	}

	/**
	 * 生成订单流水号
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected void generateOrderDoneCode(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final DEL040054Result result, final String bossTemplate) {
		String reqXml = "";
		String rspXml = "";
		String unionPaySrl = "0";
		String bossDate = "";
		try {
			userCity = (String)this.getParameters(params, "context_ddr_city");
			this.setParameter(params, "cityId", userCity);
			this.setParameter(params, "payFlag", "");
			this.setParameter(params, "bankType", "");
			//组装报文
			reqXml = mergeReqXML2Boss(params,bossTemplate);
			if (!StringUtil.isNull(reqXml))
			{
				//发送请求报文
				rspXml = sendReqXML2BOSS(accessId, params, reqXml,
						bossTemplate);			
				if (!StringUtil.isNull(rspXml))
				{		
					String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
					String resp_desc = XMLUtil.getChildText(rspXml,"response","resp_desc");
					setCommonResult(result, resp_code, resp_desc);
					if (BOSS_SUCCESS.equals(resp_code)) {
						unionPaySrl = XMLUtil.getChildText(rspXml,"content","clt_operating_srl");
						bossDate = XMLUtil.getChildText(rspXml,"content","buoperatingdetail_clt_sys_date");
						result.setOperDate(bossDate);
						result.setOperStr(unionPaySrl);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 手机充值回调
	 * @param accessId
	 * @param config
	 * @param params
	 */
	protected void payOrderBack(final String accessId, final ServiceConfig config, final List<RequestParameter> params,
			final DEL040054Result res, final String bossTemplate) {
		String reqXml = "";
		String rspXml = "";
		try {
			this.setParameter(params, "operStr", res.getOperStr());
			reqXml = mergeReqXML2Boss(params,bossTemplate);
			if (!StringUtil.isNull(reqXml))
			{
				//发送请求报文
				rspXml = sendReqXML2BOSS(accessId, params, reqXml,bossTemplate);
				if (!StringUtil.isNull(rspXml))
				{
					String resp_code = XMLUtil.getChildText(rspXml,"response","resp_code");
					String resp_desc = XMLUtil.getChildText(rspXml,"response","resp_desc");
					setCommonResult(res, resp_code, resp_desc);
				}
				else
				{
					setCommonResult(res, "1", "响应为空");
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			setCommonResult(res, "1", "充值回调失败");
		}
	}
}