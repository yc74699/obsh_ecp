package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

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
import com.xwtech.xwecp.service.logic.pojo.BaseResult;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY050025Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 产品互转 用户校验
 * 
 * @author 吴宗德
 *
 */
public class CheckUserInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(CheckUserInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public CheckUserInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		
	}
	
	@SuppressWarnings("unchecked")
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY050025Result res = new QRY050025Result();
		int retCode = -1;
		try
		{
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");
			
			int oldPayMode = 0;
			int payMode = 0;
			for (RequestParameter param:params) {
				if ("oldPayMode".equals(param.getParameterName())) {
					oldPayMode = ((Integer)param.getParameterValue()).intValue();
				}
				if ("payMode".equals(param.getParameterName())) {
					payMode = ((Integer)param.getParameterValue()).intValue();
				}
			}
			
			if (oldPayMode != payMode) {
				BaseResult result1 = this.chkPayWayChange(accessId, config, params);
//				if (LOGIC_SUCESS.equals(result1.getResultCode())) {
					boolean bln1 = ((Boolean)result1.getReObj()).booleanValue();
					
					if (bln1) {
						retCode = 1;
						res.setRetCode(retCode);
						return res;
					}
					
//				} else {
//					res.setResultCode(LOGIC_ERROR);
//					res.setErrorCode(result1.getErrorCode());
//					res.setErrorMessage(result1.getErrorMessage());
//					res.setRetCode(retCode);
//					return res;
//				}
				
				if (oldPayMode == 2) {
					BaseResult result2 = this.chkPayWayNoChange(accessId, config, params);
//					if (LOGIC_SUCESS.equals(result2.getResultCode())) {
						boolean bln2 = ((Boolean)result2.getReObj()).booleanValue();
						
						if (bln2) {
							retCode = 2;
							res.setRetCode(retCode);
							return res;
						}
						
//					} else {
//						res.setResultCode(LOGIC_ERROR);
//						res.setErrorCode(result2.getErrorCode());
//						res.setErrorMessage(result2.getErrorMessage());
//						res.setRetCode(retCode);
//						return res;
//					}
				}
			}
			
			if (oldPayMode == 1) {
				BaseResult result3 = this.chkAfterAndComPayment(accessId, config, params);
//				if (LOGIC_SUCESS.equals(result3.getResultCode())) {
					boolean bln3 = ((Boolean)result3.getReObj()).booleanValue();
					
					if (bln3) {
						retCode = 3;
						res.setRetCode(retCode);
						return res;
					}
					
//				} else {
//					res.setResultCode(LOGIC_ERROR);
//					res.setErrorCode(result3.getErrorCode());
//					res.setErrorMessage(result3.getErrorMessage());
//					res.setRetCode(retCode);
//					return res;
//				}
			}
			
			BaseResult result4 = this.chkTargetProduct(accessId, config, params);
//			if (LOGIC_SUCESS.equals(result4.getResultCode())) {
				boolean bln4 = ((Boolean)result4.getReObj()).booleanValue();
				
				if (!bln4) {
					retCode = 4;
					res.setRetCode(retCode);
					return res;
				}
				
//			} else {
//				res.setResultCode(LOGIC_ERROR);
//				res.setErrorCode(result4.getErrorCode());
//				res.setErrorMessage(result4.getErrorMessage());
//				res.setRetCode(retCode);
//				return res;
//			}
			
			BaseResult result5 = this.checkUser(accessId, config, params);
//			if (LOGIC_SUCESS.equals(result5.getResultCode())) {
				boolean bln5 = ((Boolean)result5.getReObj()).booleanValue();
				
				if (!bln5) {
					retCode = 5;
					res.setRetCode(retCode);
					return res;
				}
				
//			} else {
//				res.setResultCode(LOGIC_ERROR);
//				res.setErrorCode(result5.getErrorCode());
//				res.setErrorMessage(result5.getErrorMessage());
//				res.setRetCode(retCode);
//				return res;
//			}
			retCode = 0;
			res.setRetCode(retCode);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 付费方式变化时校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult chkPayWayChange(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_LIST_";
		boolean blResult = false;
		
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proList = (List<CWebProductOpenCfgDtBean>)obj;
//			} else {
				BaseResult accountInfo = getAccountId(accessId, config, params);
				if (LOGIC_SUCESS.equals(accountInfo.getResultCode())) {
					String accountId = (String)accountInfo.getReObj();
					
					List<RequestParameter> paramNew = copyParam(params);
					paramNew.add(new RequestParameter("accountId", accountId));
					
					reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetarsbyacct_340", params);
					
					logger.debug(" ====== 查询 产品互转 付费方式变化时校验 请求报文 ======\n" + reqXml);
					
					rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetarsbyacct_340", this.generateCity(params)));
					logger.debug(" ====== 查询 产品互转 付费方式变化时校验 返回报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						String errCode = root.getChild("response").getChildText("resp_code");
						String errDesc = root.getChild("response").getChildText("resp_desc");
						
						if (!BOSS_SUCCESS.equals(errCode))
						{
							errDt = this.wellFormedDAO.transBossErrCode("QRY050025", "ac_agetarsbyacct_340", errCode);
							if (null != errDt)
							{
								errCode = errDt.getLiErrCode();
								errDesc = errDt.getLiErrMsg();
							}
						}
						res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
						res.setErrorCode(errCode);
						res.setErrorMessage(errDesc);
						
						if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
							List arecord_count = null;
							try
							{
								arecord_count = root.getChild("content").getChildren("arecord_count");
							}
							catch (Exception e)
							{
								arecord_count = null;
							}
							if (null != arecord_count && arecord_count.size() > 1) {
								blResult = true;
							}
						}
					}
					res.setReObj(blResult);
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(accountInfo.getErrorCode());
					res.setErrorMessage(accountInfo.getErrorMessage());
				}
//			}
//			res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 后付费&合并缴费校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult chkAfterAndComPayment(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_LIST_";
		boolean blResult = false;
		
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proList = (List<CWebProductOpenCfgDtBean>)obj;
//			} else {
					
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetcompayment_338", params);
				
				logger.debug(" ====== 查询 产品互转 后付费&合并缴费校验 请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cgetcompayment_338", this.generateCity(params)));
				logger.debug(" ====== 查询 产品互转 后付费&合并缴费校验 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050025", "cc_cgetcompayment_338", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						List payment_com_user_id = null;
						try
						{
							payment_com_user_id = root.getChild("content").getChildren("payment_com_user_id");
						}
						catch (Exception e)
						{
							payment_com_user_id = null;
						}
						if (null != payment_com_user_id && payment_com_user_id.size() > 0) {
							blResult = true;
						}
					}
				}
				res.setReObj(blResult);
//			}
//			res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 未发生付费方式变化时校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult chkPayWayNoChange(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_LIST_";
		boolean blResult = false;
		
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proList = (List<CWebProductOpenCfgDtBean>)obj;
//			} else {
				BaseResult accountInfo = getAccountId(accessId, config, params);
				if (LOGIC_SUCESS.equals(accountInfo.getResultCode())) {
					String accountId = (String)accountInfo.getReObj();
					
					List<RequestParameter> paramNew = copyParam(params);
					paramNew.add(new RequestParameter("accountId", accountId));
					
					reqXml = this.bossTeletextUtil.mergeTeletext("cc_bqryaram_335", params);
					
					logger.debug(" ====== 查询 产品互转 未发生付费方式变化时校验 请求报文 ======\n" + reqXml);
					
					rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_bqryaram_335", this.generateCity(params)));
					logger.debug(" ====== 查询 产品互转 未发生付费方式变化时校验 返回报文 ======\n" + rspXml);
					if (null != rspXml && !"".equals(rspXml))
					{
						Element root = this.getElement(rspXml.getBytes());
						String errCode = root.getChild("response").getChildText("resp_code");
						String errDesc = root.getChild("response").getChildText("resp_desc");
						
						if (!BOSS_SUCCESS.equals(errCode))
						{
							errDt = this.wellFormedDAO.transBossErrCode("QRY050025", "cc_bqryaram_335", errCode);
							if (null != errDt)
							{
								errCode = errDt.getLiErrCode();
								errDesc = errDt.getLiErrMsg();
							}
						}
						res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
						res.setErrorCode(errCode);
						res.setErrorMessage(errDesc);
						
						if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
							List bacctrelamodi_revision = null;
							try
							{
								bacctrelamodi_revision = root.getChild("content").getChildren("bacctrelamodi_revision");
							}
							catch (Exception e)
							{
								bacctrelamodi_revision = null;
							}
							if (null != bacctrelamodi_revision && bacctrelamodi_revision.size() > 0) {
								for (int i = 0; i < bacctrelamodi_revision.size(); i++)
								{
									Element cbacctrelamodidt = ((Element)bacctrelamodi_revision.get(i)).getChild("cbacctrelamodidt");
									
									if (null != cbacctrelamodidt) {
										String type = p.matcher(cbacctrelamodidt.getChildText("bacctrelamodi_type")).replaceAll("");
										String endDate = p.matcher(cbacctrelamodidt.getChildText("bacctrelamodi_end_date")).replaceAll("");
										
										if (type != null && "6" != type && "7" != type) {
											Calendar cal = Calendar.getInstance();
											cal.add(Calendar.MONTH, 1);
											cal.set(Calendar.DAY_OF_MONTH, 1);
											cal.add(Calendar.DAY_OF_MONTH, -1);
											
											String date = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
											
											if (endDate == null || endDate.equals("") || endDate.substring(0,8).equals(date)) {
												blResult = true;
											}
										}
									}
								}
							}
						}
					}
					res.setReObj(blResult);
				} else {
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(accountInfo.getErrorCode());
					res.setErrorMessage(accountInfo.getErrorMessage());
				}
//			}
//			res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 目标产品校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult chkTargetProduct(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_LIST_";
		boolean blResult = false;
		
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proList = (List<CWebProductOpenCfgDtBean>)obj;
//			} else {
					
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchkproductcfg_343", params);
				
				logger.debug(" ====== 查询 产品互转 目标产品校验 请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchkproductcfg_343", this.generateCity(params)));
				logger.debug(" ====== 查询 产品互转 目标产品校验 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050025", "cc_cchkproductcfg_343", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						blResult = true;
					}
				}
				res.setReObj(blResult);
//			}
//			res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 用户校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected BaseResult checkUser(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_LIST_";
		boolean blResult = false;
		
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proList = (List<CWebProductOpenCfgDtBean>)obj;
//			} else {
					
				reqXml = this.bossTeletextUtil.mergeTeletext("cc_cchkproductchg_374", params);
				
				logger.debug(" ====== 查询 产品互转 用户校验 请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_cchkproductchg_374", this.generateCity(params)));
				logger.debug(" ====== 查询 产品互转 用户校验 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050025", "cc_cchkproductchg_374", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						blResult = true;
					}
				}
				res.setReObj(blResult);
//			}
//			res.setReObj(proList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 获取用户默认帐户ID
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected BaseResult getAccountId(final String accessId, final ServiceConfig config, final List<RequestParameter> params) {
		BaseResult res = new BaseResult();
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
//		String key = "PRO_CFG_LIST_";
		String accountId = "";
		try {
//			for (RequestParameter param:params) {
//				if ("city".equals(param.getParameterName())) {
//					key += param.getParameterValue();
//				}
//			}
//			
//			Object obj = this.wellFormedDAO.getCache().get(key);
//			if(obj != null && obj instanceof List)
//			{
//				proCfgList = (List<CcCGetProByCityBean>)obj;
//			} else {
				reqXml = this.bossTeletextUtil.mergeTeletext("ac_getrelbyuser_608", params);
				
				logger.debug(" ====== 查询账务信息请求报文 ======\n" + reqXml);
				
				rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_getrelbyuser_608", this.generateCity(params)));
//				logger.debug(" ====== 查询账务信息返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element root = this.getElement(rspXml.getBytes());
					String errCode = root.getChild("response").getChildText("resp_code");
					String errDesc = root.getChild("response").getChildText("resp_desc");
					
					if (!BOSS_SUCCESS.equals(errCode))
					{
						errDt = this.wellFormedDAO.transBossErrCode("QRY050025", "ac_getrelbyuser_608", errCode);
						if (null != errDt)
						{
							errCode = errDt.getLiErrCode();
							errDesc = errDt.getLiErrMsg();
						}
					}
					res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
					res.setErrorCode(errCode);
					res.setErrorMessage(errDesc);
					
					if (null != errCode && (BOSS_SUCCESS.equals(errCode))) {
						List acordCount = null;
						try
						{
							acordCount = root.getChild("content").getChildren("arecord_count");
						}
						catch (Exception e)
						{
							acordCount = null;
						}
						if (null != acordCount && acordCount.size() > 0) {
							for (int i = 0; i < acordCount.size(); i++)
							{
								Element accountDt = ((Element)acordCount.get(i)).getChild("caccountingrelationdt");
								if (null != accountDt)
								{
									String isDefault = p.matcher(accountDt.getChildText("acrelation_isdefault")).replaceAll("");
									
									if (isDefault != null && "1".equals(isDefault)) {
										accountId = p.matcher(accountDt.getChildText("account_id")).replaceAll("");
										res.setReObj(accountId);
									}
								}
							}
						}
					}
				}
//			}
//			res.setReObj(proCfgList);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return res;
	}
	
}