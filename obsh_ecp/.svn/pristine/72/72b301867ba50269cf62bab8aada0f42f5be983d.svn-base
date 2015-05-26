package com.xwtech.xwecp.service.logic.invocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.ServiceExecutor.ServiceConstants;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.CallFeeAccount;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GroupAccountInfoDT;
import com.xwtech.xwecp.service.logic.pojo.MmsDealDetail;
import com.xwtech.xwecp.service.logic.pojo.NewBizAccount;
import com.xwtech.xwecp.service.logic.pojo.QRY010003Result;
import com.xwtech.xwecp.service.logic.pojo.SepcAccount;
import com.xwtech.xwecp.service.logic.pojo.SpecAccountDetail;
import com.xwtech.xwecp.service.logic.pojo.YearlyPayDetail;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 账户余额及有效期
 * @author yuantao
 *
 */
public class GetUserCusBalanceInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(GetUserCusBalanceInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;
	
	private Map<String,String> groupDic;
	
	//账本字典
	String[] groupAcc = { "2001", "2002", "2003", "2004", "2005", "2006", "2007", 
			"2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016" };
	
	String[][] groupAccs = { { "2001", "2002" }, { "2003", "2004" }, { "2005", "2006" }, 
			{ "2007", "2008" }, { "2009", "2010" }, { "2011", "2012" },
			{ "2013", "2014" }, { "2015", "2016" } };
	
	public GetUserCusBalanceInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
		
		if (null == groupDic)
		{
			this.groupDic = new HashMap<String,String>();
			this.groupDic.put("2001", "彩信包年卡帐本");
			this.groupDic.put("2002", "彩信包年卡优惠帐本");
			this.groupDic.put("2003", "WAP包年卡帐本");
			this.groupDic.put("2004", "WAP包年卡优惠帐本");
			this.groupDic.put("2005", "新闻早晚报包年卡帐本");
			this.groupDic.put("2006", "新闻早晚报包年卡优惠帐本");
			this.groupDic.put("2007", "扬子晚报包年卡帐本");
			this.groupDic.put("2008", "扬子晚报包年卡优惠帐本");
			this.groupDic.put("2009", "南京手机报包年卡帐本");
			this.groupDic.put("2010", "南京手机报包年卡优惠帐本");
			this.groupDic.put("2011", "苏州手机报包年卡帐本");
			this.groupDic.put("2012", "苏州手机报包年卡优惠帐本");
			this.groupDic.put("2013", "无锡手机报包年卡帐本");
			this.groupDic.put("2014", "无锡手机报包年卡优惠帐本");
			this.groupDic.put("2015", "常州手机报包年卡帐本");
			this.groupDic.put("2016", "常州手机报包年卡优惠帐本");
		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY010003Result res =  new QRY010003Result();
		res.setErrorMessage("");
		int type = 0;
		List<MmsDealDetail> mmsList = null;
		List<SpecAccountDetail> accList = null;
		String blanceAmt = "0.0";  //用户余额
		//当月返还金额
		float backAmt = 0.0f;
		SepcAccount accountDt = null;
		StringBuffer str = null;
		NewBizAccount bizAcc = null;
		String accId = "";
		YearlyPayDetail yearDt = null; 
		List<YearlyPayDetail> yearList = null;
		
		try
		{
			if (null != params && params.size() > 0)
			{
				for (RequestParameter par : params)
				{
					if ("type".equals(par.getParameterName()))
					{
						type = Integer.parseInt(String.valueOf(par.getParameterValue()));
					}
				}
			}
			
			//查询话费帐户
			if (0 == type || 1 == type)
			{
				str = new StringBuffer("");
				res.setCallFeeAccount(this.queryFeeAcc(accessId, config, params, str, res));
			}
			
			//查询新业务帐户
			if (0 == type || 2 == type)
			{
				bizAcc = new NewBizAccount();
				//通过查询话费帐户获取
				if (null != str && !"".equals(str.toString()))
				{
					bizAcc.setBalance(str.toString());
				}
				else  //查询新业务帐户
				{
					str = new StringBuffer("");
					this.queryFeeAcc(accessId, config, params, str, res);
					bizAcc.setBalance(str.toString());
				}
			}
			
			//专有帐户
			if (0 == type || 3 == type)
			{
				//查询用户专有帐户明细
				mmsList = this.queryMarketingSolution(accessId, config, params, res);
				if (null != mmsList && mmsList.size() > 0)
				{
					//查询用户余额
					blanceAmt = this.querySpAccBal(accessId, config, params, res);
					if (null != blanceAmt && !"".equals(blanceAmt))
					{
						accountDt = new SepcAccount();
						accList = new ArrayList();
						//专有帐户余额	
						accountDt.setSpecBalance(blanceAmt);
						
						for (MmsDealDetail mmsDt : mmsList)
						{
							//月释放信息
							List<SpecAccountDetail> accountList = this.queryMarketingPlanDetailInfo(
									accessId, config, params, mmsDt.getPlanInfoId(), res);
							if (null != accountList && accountList.size() > 0)
							{
								for (SpecAccountDetail accDt : accountList)
								{
									if ("1".equals(accDt.getAccountName()))
									{
										//营销方案名称
										accDt.setAccountName(mmsDt.getDealName());
										//新业务账户 单位 分
										accDt.setNewBizAccount(String.valueOf(Float.parseFloat(mmsDt.getNewbizAcc())));
										if (new SimpleDateFormat("yyyyMM").format(new Date()).equals(accDt.getReturnTime().substring(0, 6)))
										{
											backAmt += Float.parseFloat(accDt.getCashAccount());
										}
										accList.add(accDt);
									}
								}
							}
						}
						//本月返还金额
						accountDt.setNowReturnFee(String.valueOf(backAmt));
					}
				}
			}
			
			//新业务包年卡帐户
			if (0 == type || 4 == type)
			{
				accId = this.getDefaultAccountId(accessId, config, params, res);
				if (null != accId && !"".equals(accId))
				{
					//根据帐户ID查询帐户资料,集团客户资料
					List<GroupAccountInfoDT> groupList = this.getgroupacct(accessId, config, params, accId, res);
					if (null != groupList && groupList.size() > 0)
					{
						Map<String, GroupAccountInfoDT> map = new HashMap<String, GroupAccountInfoDT>();
						for (GroupAccountInfoDT accDt : groupList)
						{
							for (int i = 0; i < this.groupAcc.length; i++)
							{
								if (this.groupAcc[i].equals(accDt.getItemId()))
								{
									map.put(groupAcc[i], accDt);
								}
							}
						}
						if (map.size() > 0)
						{
							yearList = new ArrayList();
							for (int i = 0; i < this.groupAccs.length; i++)
							{
								if (null != map.get(this.groupAccs[i][0]))
								{
									yearDt = new YearlyPayDetail();
									//包年卡类型
									yearDt.setYearlyCardType(String.valueOf(this.groupDic.get(groupAccs[i][0])));
									//包年卡余额 单位为：分
									float bal1 = Float.parseFloat(map.get(this.groupAccs[i][0]).getBalance());
									float bal2 = Float.parseFloat(map.get(this.groupAccs[i][1]).getBalance());
									yearDt.setYearlyCardBalance(String.valueOf(bal1 + bal2));
									yearList.add(yearDt);
								}
							}
						}
					}
				}
			}
			
			//查询所有时，至状态为成功
			if (0 == type)
			{
				res.setResultCode("0");
			}
			
			//专有帐户
			res.setSepcAccount(accountDt);
			//专有帐户明细
			res.setSpecAccountDetail(accList);
			//新业务账户
			res.setNewBizAccount(bizAcc);
			//新业务包年卡帐户
			res.setYearlyPayAccount(yearList);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
	
	/**
	 *  根据帐户ID查询帐户资料,集团客户资料
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	public List getgroupacct (String accessId, ServiceConfig config, 
			   List<RequestParameter> params, String accId, QRY010003Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		GroupAccountInfoDT dt = null;
		List<GroupAccountInfoDT> list = null;
		boolean accFlag = true;
		RequestParameter p = null;
		ErrorMapping errDt = null;
		
		try
		{
			for (RequestParameter par : params)
			{
				if ("account_id".equals(par.getParameterName()))
				{
					accFlag = false;
					par.setParameterValue(accId);
				}
			}
			if (accFlag)
			{
				p = new RequestParameter();
				p.setParameterName("account_id");
				p.setParameterValue(accId);
				params.add(p);
			}
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_acgetgroupacct_364", params);
			logger.debug(" ====== 根据帐户ID查询帐户资料,集团客户资料 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "ac_acgetgroupacct_364", this.generateCity(params)));
				logger.debug(" ====== 根据帐户ID查询帐户资料,集团客户资料 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010003", "ac_acgetgroupacct_364", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				List countList = this.config.getContentList(root, "arecord_count");
				if (null != countList && countList.size() > 0)
				{
					list = new ArrayList();
					for (int i = 0; i < countList.size(); i++)
					{
						Element cacctbkdt = this.config.getElement((Element)countList.get(i), "cacctbkdt");
						if (null != cacctbkdt)
						{
							dt = new GroupAccountInfoDT ();
							dt.setItemId(this.config.getChildText(cacctbkdt, "acctbkitem_id"));
							dt.setInAmount(this.config.getChildText(cacctbkdt, "acctbk_in_amount"));
							dt.setOutAmount(this.config.getChildText(cacctbkdt, "acctbk_out_amount"));
							dt.setBalance(this.config.getChildText(cacctbkdt, "acctbk_balance"));
							dt.setStatus(this.config.getChildText(cacctbkdt, "acctbk_status"));
							dt.setOpenDate(this.config.getChildText(cacctbkdt, "acctbk_open_date"));
							dt.setExpireDate(this.config.getChildText(cacctbkdt, "acctbk_expire_date"));
							dt.setLastUpdateTime(this.config.getChildText(cacctbkdt, "acctbk_last_update_time"));
							dt.setAccountSrlId(this.config.getChildText(cacctbkdt, "a_account_srl_id"));
							list.add(dt);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			return null;
		}
		return list;
	}
	
	/**
	 * 获取用户的默认帐户Id
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	public String getDefaultAccountId (String accessId, ServiceConfig config, 
						     List<RequestParameter> params, QRY010003Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		String accId = "";
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_getrelbyuser_608", params);
			logger.debug(" ====== 获取用户的默认帐户Id 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "ac_getrelbyuser_608", this.generateCity(params)));
				logger.debug(" ====== 获取用户的默认帐户Id 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010003", "ac_getrelbyuser_608", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				List countList = this.config.getContentList(root, "arecord_count");
				if (null != countList && countList.size() > 0)
				{
					Element caccountingrelationdt = this.config.getElement((Element)countList.get(0), "caccountingrelationdt");
					if (null != caccountingrelationdt)
					{
						accId = this.config.getChildText(caccountingrelationdt, "account_id");
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			return "";
		}
		return accId;
	}
	
	/**
	 * 营销方案查询，月释放信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @param marketingPlanInfoId
	 * @return
	 */
	public List queryMarketingPlanDetailInfo (String accessId, ServiceConfig config, 
			List<RequestParameter> params, String marketingPlanInfoId, QRY010003Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		SpecAccountDetail accDetail = null;
		RequestParameter p = null;
		boolean serType = true;
		boolean planFlag = true;
		boolean dateFlag = true;
		List<SpecAccountDetail> list = null;
		ErrorMapping errDt = null;
		
		try
		{
			for (RequestParameter par : params)
			{
				if ("bossmms_services_type".equals(par.getParameterName()))
				{
					serType = false;
					par.setParameterValue("1102");
				}
				if ("usermarketingbaseinfo_plan_info_id".equals(par.getParameterName()))
				{
					planFlag = false;
					par.setParameterValue(marketingPlanInfoId);
				}
				if ("date".equals(par.getParameterName()))
				{
					dateFlag = false;
					par.setParameterValue(new SimpleDateFormat("yyyyMM").format(new Date()));
				}
			}
			if (serType)
			{
				p = new RequestParameter();
				p.setParameterName("bossmms_services_type");
				p.setParameterValue("1102");
				params.add(p);
			}
			if (planFlag)
			{
				p = new RequestParameter();
				p.setParameterName("usermarketingbaseinfo_plan_info_id");
				p.setParameterValue(marketingPlanInfoId);
				params.add(p);
			}
			if (dateFlag)
			{
				p = new RequestParameter();
				p.setParameterName("date");
				p.setParameterValue(new SimpleDateFormat("yyyyMM").format(new Date()));
				params.add(p);
			}
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_mmsdeal_361", params);
			logger.debug(" ====== 月释放信息 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_mmsdeal_361", this.generateCity(params)));
				logger.debug(" ====== 月释放信息 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010003", "cc_mmsdeal_361", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				List planList = this.config.getContentList(root, "monthreleaserecord_plan_info_id");
				if (null != planList && planList.size() > 0)
				{
					list = new ArrayList();
					for (int i = 0; i < planList.size(); i++)
					{
						Element recordDt = this.config.getElement((Element)planList.get(i), "cmonthreleaserecorddt");
						if (null != recordDt)
						{
							accDetail = new SpecAccountDetail();
							//现金账户 单位 分
							accDetail.setCashAccount(String.valueOf(
									  Float.parseFloat(
									  this.config.getChildText(recordDt, "monthreleaserecord_real_release_amount"))));
							//返还时间
							accDetail.setReturnTime(this.config.getChildText(recordDt, "monthreleaserecord_release_time"));
							//monthreleaserecord_release_status
							accDetail.setAccountName(this.config.getChildText(recordDt, "monthreleaserecord_release_status"));
							list.add(accDetail);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询用户余额
	 * @param accessId
	 * @param config
	 * @param params
	 */
	public String querySpAccBal (String accessId, ServiceConfig config, 
			                 List<RequestParameter> params, QRY010003Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		float blanceAmt = 0;
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_aqueryspaccbal_360", params);
			logger.debug(" ====== 查询用户余额 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_aqueryspaccbal_360", this.generateCity(params)));
				logger.debug(" ====== 查询用户余额 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010003", "cc_aqueryspaccbal_360", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				List<Element> countList = this.config.getContentList(root, "arecord_count");
				if (null != countList && countList.size() > 0)
				{
					String ddrCity = null;
					RequestParameter ddrCityParam = super.getParameter(params, ServiceConstants.USER_CITY);
					if(ddrCityParam != null)
					{
						ddrCity = (String)ddrCityParam.getParameterValue();
					}
					if(this.needCutCity(ddrCity))
					{
						//华为的接口, 单位分
						//专有账户的余额，是加起来
						for(Element element : countList){
							Element specialAccountEl = this.config.getElement(element, "special_account");
							if(specialAccountEl != null){
								blanceAmt += Float.parseFloat(this.config.getChildText(specialAccountEl, "a_balance"));
							}
						}
					}
					else
					{
						//新大陆的接口, 单位厘
						for(Element element : countList){
							Element specialAccountEl = this.config.getElement(element, "special_account");
							if(specialAccountEl != null){
								blanceAmt += Float.parseFloat(this.config.getChildText(specialAccountEl, "a_balance")) / 10.0f;
							}
						}
					}
					
					/*
					Element account = this.config.getElement((Element)countList.get(0), "special_account");
					if (null != account)
					{
						blanceAmt = String.valueOf(Float.parseFloat(
								this.config.getChildText(account, "a_balance"))/10.0);  //单位 分
					}
					*/
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			return "";
		}
		return blanceAmt + "";
	}
	
	/**
	 * 营销方案查询
	 * @param accessId
	 * @param config
	 * @param params
	 */
	public List queryMarketingSolution (String accessId, ServiceConfig config, 
			                 List<RequestParameter> params, QRY010003Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		RequestParameter p = null;
		MmsDealDetail dt = null;
		List<MmsDealDetail> list = null;
		ErrorMapping errDt = null;
		
		try
		{
			p = new RequestParameter();
			p.setParameterName("bossmms_services_type");
			p.setParameterValue("101");
			params.add(p);
			p = new RequestParameter();
			p.setParameterName("usermarketingbaseinfo_plan_info_id");
			p.setParameterValue("");
			params.add(p);
			p = new RequestParameter();
			p.setParameterName("date");
			p.setParameterValue(new SimpleDateFormat("yyyyMM").format(new Date()));
			params.add(p);
			
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_mmsdeal_361", params);
			logger.debug(" ====== 营销方案查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_mmsdeal_361", this.generateCity(params)));
				logger.debug(" ====== 营销方案查询 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010003", "cc_mmsdeal_361", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				List planList = this.config.getContentList(root, "marketingplan_plan_id");
				List userList = this.config.getContentList(root, "usermarketingbaseinfo_user_id");
				
				if (null != planList && planList.size() > 0)
				{
					list = new ArrayList();
					for (int i = 0; i < planList.size(); i++)
					{
						Element planDt = this.config.getElement((Element)planList.get(i), "cmarketingplandt");
						if (null != planDt)
						{
							dt = new MmsDealDetail();
							//营销方案编号
							dt.setPlanId(this.config.getChildText(planDt, "marketingplan_plan_id"));
							//方案名称
							dt.setDealName(this.config.getChildText(planDt, "marketingplan_name"));
							//方案说明
							dt.setDealDesc(this.config.getChildText(planDt, "marketingplan_remark"));
							//现金充值
							dt.setFeeAcc(this.config.getChildText(planDt, "marketingplan_cash_amount"));
							//新业务充值
							dt.setNewbizAcc(this.config.getChildText(planDt, "marketingplan_new_business_amount"));
							//优惠充值
							dt.setFavAcc(this.config.getChildText(planDt, "marketingplan_discount_amount"));
							list.add(dt);
						}
					}
				}
				if (null != userList && userList.size() > 0 && null != list && list.size() > 0)
				{
					String plan_id = "";
					for (int i = 0; i < userList.size(); i++)
					{
						Element base = this.config.getElement((Element)userList.get(i), "usermarketingbaseinfo");
						if (null != base)
						{
							plan_id = this.config.getChildText(base, "usermarketingbaseinfo_plan_id");
							for (MmsDealDetail pDt : list)
							{
								if (pDt.getPlanId().equals(plan_id))
								{
									//申请时间
									pDt.setApplyDate(this.config.getChildText(base, "usermarketingbaseinfo_create_time"));
									//开始日期
									pDt.setBeginDate(this.config.getChildText(base, "usermarketingbaseinfo_inure_date"));
									//结束日期
									pDt.setEndDate(this.config.getChildText(base, "usermarketingbaseinfo_end_date"));
									//usermarketingbaseinfo_plan_info_id
									pDt.setPlanInfoId(this.config.getChildText(base, "usermarketingbaseinfo_plan_info_id"));
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
			return null;
		}
		return list;
	}
	
	/**
	 * 查询话费帐户
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public CallFeeAccount queryFeeAcc (String accessId, ServiceConfig config, 
			List<RequestParameter> params, StringBuffer str, QRY010003Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		CallFeeAccount dt = null;
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercustaccbalance_67", params);
			logger.debug(" ====== 查询话费帐户 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cgetusercustaccbalance_67", this.generateCity(params)));
				logger.debug(" ====== 查询话费帐户 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY010003", "cc_cgetusercustaccbalance_67", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				dt = new CallFeeAccount();
				//余额
				dt.setBalance(String.valueOf(Double.parseDouble(
				   this.config.getChildText(this.config.getElement(root, "content"), "balance"))));
				dt.setValiDate("");
				String strDate = this.config.getChildText(this.config.getElement(root, "content"), "account_expire_date");
				if (null != strDate && !"".equals(strDate))
				{
					dt.setValiDate(strDate.substring(0, 8));
				}
				//新业务账户
				str.append(String.valueOf(Double.parseDouble(
					this.config.getChildText(this.config.getElement(root, "content"), "new_balance"))));
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return dt;
	}
}
