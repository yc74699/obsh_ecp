package com.xwtech.xwecp.service.logic.invocation;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.HalfFlow;
import com.xwtech.xwecp.service.logic.pojo.NewScoreDetail;
import com.xwtech.xwecp.service.logic.pojo.PackageFlow;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY030011Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040075Result;
import com.xwtech.xwecp.service.logic.pojo.ScoreYearNum;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;

/**
 * QRY040002、QRY040048、QRY040065 合成 
 * @author YangXQ
 * 2014-6-30
 */
public class HBScoreFluxGprsBossInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(HBScoreFluxGprsInvocation.class);
	
	/**
	 * BOSS接口
	 */
	private static final String BOSS_INTERFACE = "cc_cgetuseraccscore_361";
	
	/**
	 * 组装报文
	 */
	private BossTeletextUtil bossTeletextUtil = null;
	
	/**
	 * 调用Boss接口
	 */
	private IRemote remote = null;
	
	private WellFormedDAO wellFormedDAO = null;
	/*20元流量封顶的套餐类型编码*/
	private static String unlimitedBandwidthType = "1046";
	/*存放20元流量封顶的产品编码*/
	private Set<String> maxCode;
	/*存放随E玩的产品编码*/
	private Set<String> sEWCode;
	/*标示用户是否办理了20元流量封顶业务*/
	private String isUnlimitedBandwidth = "0";
	/*标示用户是否办理了随E玩业务*/ 
	private String isPlayAt = "0";
	/*附加套餐业务标 1:流量季包半年包标识*/
	private String isSpecilFlag = "0";
	/*4G半包套餐业务标识*/
	private String isHalfFlag = "0";
	/*存放流量叠加包编码*/
	private Map<String,Integer> fluxPackage ;
	/*用于屏蔽Wlan包流量*/
	private Set<String> wlan;
	/*流量季包半年包流量*/
	private Set<String> quaGprs;
	
	/*4G产品对照表(全包)*/
	//private Set<String> LTEGprs;
	
	/*用于屏蔽4G产品对照表(半包) 隨意玩*/
	private Set<String> halfGprs;
	
	
	
	public HBScoreFluxGprsBossInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		/*
		 * QRY040048
		 */
		isUnlimitedBandwidth = "0";
		isPlayAt = "0";
		maxCode = new HashSet<String>();
		sEWCode = new HashSet<String>();
		wlan = new HashSet<String>();
		fluxPackage = new HashMap<String,Integer>();
		quaGprs = new HashSet<String>();
		//查询半包类业务编码
		halfGprs = getLTEGRPS("3");
		maxCode.add("4052");
		maxCode.add("1277");
		maxCode.add("5652");
		maxCode.add("4051");
		maxCode.add("4825");

		sEWCode.add("2000002660");
		sEWCode.add("2000002661");
		wlan.add("2000003177");
		wlan.add("2000003178");
		wlan.add("2000003179");
		/*港澳台GPRS流量漫游*/
		wlan.add("3223");
		wlan.add("1979");
		wlan.add("3221");
		wlan.add("3222");
		wlan.add("2000003223");
		wlan.add("2000001979");
		wlan.add("2000003221");
		wlan.add("2000003222");
		/*存放流量叠加包编码*/
		fluxPackage.put("2000002505", 0);
		fluxPackage.put("2000002504", 0);
		fluxPackage.put("2000002631", 0);
		fluxPackage.put("2000003020", 0);
		fluxPackage.put("2000003540", 0);
//		fluxPackage.put("2000003541", 0);
		/*流量季包半年包*/
		quaGprs.add("2000003748");
		quaGprs.add("2000003749");
		quaGprs.add("2000003750");
		quaGprs.add("2000003751");
		quaGprs.add("2000003752");
		quaGprs.add("2000003753");
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
        // 在查询QRY040048时，cycle值会被重新赋值（赋为当前时间），会影响QRY040065的参数查询，将该值备份。
		String circleTmp = (String)getParameters(params,"cycle");		
		
		QRY040075Result result = new QRY040075Result();		
		
		//组装请求报文
		String reqXml = bossTeletextUtil.mergeTeletext(BOSS_INTERFACE, params);		
		//调用Boss接口
		try {
			String rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, BOSS_INTERFACE, this.generateCity(params)));
			
			if (null != rspXml && !"".equals(rspXml)) {
				parseXmlConversionObj(rspXml, result); // 解析Xml转换为QRY040002对象
			}
			else{
				setErrorResult(result);
			}
		
			//20111231 yangg add start
			//如果是神州行品牌,需要判断号码是否开通了"神州行积分计划",如果未开通，resultcode返回xxx.积分数量0
			String brand  = (String)getParameters(params, "brand");
			if(!StringUtils.isBlank(brand) && brand.length() >=3 && brand.startsWith("SZX"))
			{
				ServiceLocator sl1 = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
				ServiceInfo si1;
				try {
					params.add(new RequestParameter("type", 1));
					params.add(new RequestParameter("bizId", "SZX_JFJH")); //查询神州行积分计划
					si1 = sl1.locate("QRY020001", params);
					QRY020001Result re = (QRY020001Result)si1.getServiceInstance().execute(accessId);
					if (re != null)
					{
						if (null != re.getGommonBusiness() && re.getGommonBusiness().size() > 0)
						{
							for (GommonBusiness g : re.getGommonBusiness())
							{
								if("SZX_JFJH".equals(g.getId()) && 2 != g.getState()){
									//result.setResultCode("0");
									result.setErrorCode("-2727");
									result.setErrorMessage("未开通神州行积分计划");
									return result;
								}else{						//开通了积分计划，查询积分，调用QRY030011
									//
									String szx_jf = "";
									ServiceLocator sl2 = (ServiceLocator)XWECPApp.SPRING_CONTEXT.getBean("serviceLocator");
									ServiceInfo si2;
									params.add(new RequestParameter("flag", "2"));
									si2 = sl2.locate("QRY030011", params);
									QRY030011Result re_szxjf = (QRY030011Result)si2.getServiceInstance().execute(accessId);
									if(re_szxjf!=null && re_szxjf.getNewScoreDetail() != null){
										for(NewScoreDetail nsd : re_szxjf.getNewScoreDetail())
										{
											if("7".equals(nsd.getScorenId())){
												szx_jf = String.valueOf(nsd.getScore());
												result.setScoreLeavingsScore(szx_jf);
												break;
											}
										}
										
									}
								}
							}
							
						}
					}
					
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			//yangg add end

		} catch (CommunicateException e) {
			logger.error(e, e);
			setErrorResult(result);
		} catch (Exception e) {
			logger.error(e, e);
		}	
		
		//查询 Qry040048Result		
		result.setErrorCode(null);
		checkPackage(params,accessId,result);
		if(null == result.getErrorCode() || "".equals(result.getErrorCode()))
		{
			result.setIsUnlimitedBandwidth(isUnlimitedBandwidth);
			result.setIsPlayAt(isPlayAt);
			result.setIsSpecilFlag(isSpecilFlag);
			result.setIsHalfFlag(isHalfFlag);
			
			if("1".equals( isUnlimitedBandwidth))
			{
				getUsedFlux(result,params,accessId);
				return result;
			}
			getUserFluxInfo(result,params,accessId,config);
		}		
		
		//查询Qry040048Result
		setParameter(params, "cycle",circleTmp);
		result = qryGprsSectionUsedFlux(accessId,config,params,result);			
		return result;
	}
	
	
	/**
	 * 解析Xml转换为QRY040002对象
	 * @param rspXml
	 * @param result
	 */
	private void parseXmlConversionObj(String rspXml, QRY040075Result result) {
		StringReader stringReader = new StringReader(rspXml);
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Document doc = builder.build(new InputSource(stringReader));
			//获取operation_root节点
			Element root = doc.getRootElement();
			
			//获取返回信息
			Element responseEl = root.getChild("response");
			if(responseEl != null){
				setResponseInfo(responseEl, result);//从Boss报文获取响应状态插入逻辑接口中
			}
			
			//获取Content节点
			Element content = root.getChild("content");
			
			//获取余额信息
			Element balanceEl = content.getChild("balance_response");
			if(balanceEl != null){
				setBalanceInfo(balanceEl, result);//从Boss返回的Xml中插入数据到逻辑接口
			}
			
			//获取积分信息
			Element scoreEl = content.getChild("score_response");
			if(scoreEl != null){
				setScoreInfo(scoreEl, result);//从Boss中获取积分信息插入逻辑接口
			}
			
			//获取M值信息
			Element zoneMvalueEl = content.getChild("zonemvalue_response");
			if(zoneMvalueEl != null){
				setZoneMvalueInfo(zoneMvalueEl, result);
			}
			
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 从Boss报文获取响应状态插入逻辑接口中
	 * @param responseEl
	 * @param result
	 */
	private void setResponseInfo(Element responseEl, QRY040075Result result) {
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

	/**
	 * 从Boss报文获取M值信息插入逻辑接口
	 * @param child
	 * @param result
	 */
	private void setZoneMvalueInfo(Element zoneMvalueEl, QRY040075Result result) {
		//客户M值
		String zoneMvaleTotalMvalue = zoneMvalueEl.getChildText("zonemvalue_total_mvalue");
		result.setZoneMvaleTotalMvalue(zoneMvaleTotalMvalue);
		
		//M值
		String zoneMvlaueMvalue = zoneMvalueEl.getChildText("zonemvalue_mvalue");
		result.setZoneMvlaueMvalue(zoneMvlaueMvalue);
		
		//上半年剩余回馈M值
		String zoneMvalueLastMvalue = zoneMvalueEl.getChildText("zonemvalue_last_mvalue");
		result.setZoneMvalueLastMvalue(zoneMvalueLastMvalue);

		//前年客户M值
		String zoneMvaluePreMvalue = zoneMvalueEl.getChildText("zonemvalue_pre_mvalue");
		result.setZoneMvaluePreMvalue(zoneMvaluePreMvalue);
		
		//今年消费M值
		String zoneMvaluePresentMvalue = zoneMvalueEl.getChildText("zonemvalue_present_mvalue");
		result.setZoneMvaluePresentMvalue(zoneMvaluePresentMvalue);
		
		//今年奖励M值
		String zoneMvalueBountyMvalue = zoneMvalueEl.getChildText("zonemvalue_bounty_mvalue");
		result.setZoneMvalueBountyMvalue(zoneMvalueBountyMvalue);
		
		//已使用回馈M值
		String zoneMvalueUsedMvalue = zoneMvalueEl.getChildText("zonemvalue_used_mvalue");
		result.setZoneMvalueUsedMvalue(zoneMvalueUsedMvalue);
	}

	/**
	 * 从Boss中获取积分信息插入逻辑接口
	 * @param scoreEl
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	private void setScoreInfo(Element scoreEl, QRY040075Result result) {
		//用户标示
		String userId = scoreEl.getChildText("score_user_id");
		result.setScoreUserId(userId);
		
		//转赠积分
		String scoreGiftScore = scoreEl.getChildText("score_gift_score");
		result.setScoreGiftScore(scoreGiftScore);
		
		//已兑换积分
		String scoreExchangedScore = scoreEl.getChildText("score_exchanged_score");
		result.setScoreExchangedScore(scoreExchangedScore);
		
		//剩余积分
		String scoreLeavingsScore = scoreEl.getChildText("score_leavings_score");
		result.setScoreLeavingsScore(scoreLeavingsScore);
		
		//积分
		String scoreChangeFlag = scoreEl.getChildText("score_change_flag");
		result.setScoreChangeFlag(scoreChangeFlag);
		
		//列表
		List<Element> scoreYearNumList = scoreEl.getChildren("score_year_num");
		List<ScoreYearNum> scoreYearNumPojoList = new ArrayList<ScoreYearNum>();
		for(int i = 0;i < scoreYearNumList.size() ; i++){
			Element cscoreinfoDtEl = scoreYearNumList.get(i).getChild("cscoreinfo_dt");
			
			ScoreYearNum scoreYearNum = new ScoreYearNum();
			
			//积分年份
			String scoreYearNumEl = cscoreinfoDtEl.getChildText("score_year_num");
			scoreYearNum.setScoreYearNum(scoreYearNumEl);
			
			//消费奖励积分
			String scoreBountyScoreEl = cscoreinfoDtEl.getChildText("score_bounty_score");
			scoreYearNum.setScoreBountyScore(scoreBountyScoreEl);
			
			//话费积分
			String scorePhoneScoreEl = cscoreinfoDtEl.getChildText("score_phone_score");
			scoreYearNum.setScorePhoneScore(scorePhoneScoreEl);
			
			//年累计积分
			String scoreAlreadyExchangedScoreEl = cscoreinfoDtEl.getChildText("score_already_exchanged_score");
			scoreYearNum.setScoreAlreadyExchangedScore(scoreAlreadyExchangedScoreEl);
			
			scoreYearNumPojoList.add(scoreYearNum);
		}
		result.setScoreYearNum(scoreYearNumPojoList);
	}

	/**
	 * 从Boss返回的Xml中插入数据到逻辑接口
	 * @param balanceEl
	 * @param result
	 */
	private void setBalanceInfo(Element balanceEl, QRY040075Result result) {
		//获取余额
		String balance = balanceEl.getChildText("balance");
		if(!"".equals(balance)){
			result.setBalance((Float.parseFloat(balance) / 100) + "");
		}
		
		//获取新余额
		String newBalance = balanceEl.getChildText("new_balance");
		result.setNewBalance(newBalance);
		
		//有效期
		String expire = balanceEl.getChildText("account_expire_date");
		result.setAccountExpireDate(expire);
	}
	
	/*判断业务中是否有20元封顶和随E玩*/
	private void checkPackage(List<RequestParameter> params ,String accessId,QRY040075Result res)
	{
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		int nowDate = Integer.parseInt(DateTimeUtil.getTodayChar8());
		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_find_package_62_TC", params);
			logger.debug(" ====== 查询个人套餐信息发送报文 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_find_package_62_TC", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				if (null != rspXml && !"".equals(rspXml))
				{
					Element personalInfo = this.getElement(rspXml.getBytes());
					String resp_code = personalInfo.getChild("response").getChildText("resp_code");
					String resp_desc = personalInfo.getChild("response").getChildText("resp_desc");
				
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
					
					if(!LOGIC_SUCESS.equals(resultCode))
					{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
					else
					{
						List package_code = personalInfo.getChild("content").getChildren("package_code");
						int size = 0; 
						if(null!=package_code)
						{
							size = package_code.size();
						}
						if (null != package_code && size > 0)
						{
							for (int i = 0; i < size; i++)
							{
								Element cplanpackagedt = ((Element)package_code.get(i)).getChild("cplanpackagedt");

								String packageType = p.matcher(cplanpackagedt.getChildText("package_type")).replaceAll("");
								String packageCode = p.matcher(cplanpackagedt.getChildText("package_code")).replaceAll("");
								int userDate = Integer.parseInt(p.matcher(
										cplanpackagedt.getChildText("package_use_date")).replaceAll("").substring(0, 8));
								
								if(unlimitedBandwidthType.equals(packageType) && (checkMaxFlux(packageCode,res)) && nowDate >= userDate)
								{
									/*标示用户是否办理了20元GPS流量封顶业务,如果有封顶业务无需循环直接跳出*/
									isUnlimitedBandwidth = "1";
								}
								if(sEWCode.contains(packageCode)&& nowDate >= userDate)
								{	
									isPlayAt = "1";
								}
								if(quaGprs.contains(packageCode) && nowDate >= userDate){
									isSpecilFlag = "1";
								}
								if(halfGprs.contains(packageCode) && nowDate >= userDate){
									isHalfFlag = "1";
								}
							}							
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
	}
	private String formatGPRSDetail(String gprsFlux)
	{
		double flux = Double.parseDouble((String) gprsFlux);
		DecimalFormat df = new DecimalFormat("#0.00");
		gprsFlux = df.format(flux / 1024);
		
		return gprsFlux;
	}
	
	/*获取20元封顶的月流量使用情况*/
	private void getUsedFlux(QRY040075Result res,List<RequestParameter> params ,String accessId)
	{
		setParameter(params, "dayNumber", "31");
		setParameter(params, "idType", "0");
		String date = (String)getParameters(params,"cycle");
		setParameter(params, "date",date);
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		try{
			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetgprsflux_717", params);
			logger.debug(" ====== GPRS流量查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId, "ac_agetgprsflux_717", super.generateCity(params)));
				logger.debug(" ====== GPRS流量查询 接收报文 ====== \n" + rspXml);
				if (null != rspXml && !"".equals(rspXml)) 
				{
					root = this.getElement(rspXml.getBytes());
					resp_code = root.getChild("response").getChildText("resp_code");
					String resp_desc = root.getChild("response").getChildText("resp_desc");
					
					String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;

					if (LOGIC_SUCESS.equals(resultCode))
					{
						String errCode = root.getChild("content").getChildText("error_code");
						String useFlux = root.getChild("content").getChildText("gprsbill_total_fee");
						String tempFlux = formatGPRSDetail(useFlux);
						
						res.setResultCode(resultCode);
						res.setErrorCode(errCode);
						res.setErrorMessage(resp_desc);
						res.setUseFlux(tempFlux);
						return;
					}
					else
					{
						res.setResultCode(resultCode);
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e,e);
		}
	}
	/*
	 * 获取用户流量使用明细
	 * */
	private void getUserFluxInfo(QRY040075Result res,List<RequestParameter> params ,String accessId,ServiceConfig config)
	{ 
		int totalValue = 0;
		int userdValue = 0;
//		List<String> anomalylist  = this.getAnomalyGprs();
		
		try{
	       //查询办理4G套餐功能的产品使用情况（全包和半年包）
	       List<Node> lteList = queryGPRSPackageInfo(accessId,config,params,res);
	        
		    if(null != lteList && lteList.size() > 0)
		    {
			   for(int i=0;i<lteList.size();i++)
			   {
				 
		         Node lteNode = lteList.get(i);
			     String productId = lteNode.selectSingleNode("gprs_product_id").getText().trim();
			     //去除不规则流量GPRS查询 
//			     if(null !=anomalylist && anomalylist.contains(productId) )  continue;
			     
			     String productType = lteNode.selectSingleNode("gprs_product_type").getText().trim();
			     String rateType = lteNode.selectSingleNode("gprs_rate_type").getText().trim();
			     
			     int tempTotal = Integer.parseInt(lteNode.selectSingleNode("gprs_max_value").getText().trim());
			     int tempUsed = Integer.parseInt(lteNode.selectSingleNode("gprs_cumulate_value").getText().trim());
			     
			     //处理随意玩套餐流量的使用情况
			     if(sEWCode.contains(productId))
			     {
			    	 HalfFlow halfFlow = new HalfFlow();
					 halfFlow.setTotalFlow(String.valueOf(tempTotal));
					 halfFlow.setUsedFlow(String.valueOf(tempUsed));
					 res.setHalfFlow(halfFlow);
					 continue;
				  //全包的套餐流量总量和使用量的总和。(productType： 1:半包，2：全包，3：全包，半包)
			     }
			     else if("2".equals(productType) && !"2".equals(rateType))
			     {
			    	 totalValue = totalValue + tempTotal;
				     userdValue = userdValue + tempUsed;
			     }
			  }
		   }

		   PackageFlow packageFlow = new PackageFlow();
		   packageFlow.setTotalFlow(String.valueOf(totalValue));
		   packageFlow.setUsedFlow(String.valueOf(userdValue));

		   res.setPackageFlow(packageFlow);
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}	
    }
	/**
	 * 查询不规则流量GPRS
	 * @param  
	 * @return 
	 */
//	private  List<String> getAnomalyGprs() {
//		return this.wellFormedDAO.getAnomalyGprs();
//	}

	/**
	 * 查询用户个人4G功能套餐的使用明细
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> queryGPRSPackageInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040075Result res)
	{
		setParameter(params, "gprstype", "3");
		String dateNow = DateTimeUtil.getTodayChar6();
		setParameter(params, "cycle",dateNow);

		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_4gqrygprsallpkgflux_70", params);
			logger.debug(" ====== 查询用户个人4G功能套餐的使用明细 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_4gqrygprsallpkgflux_70", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					org.dom4j.Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/gprs_all_pkg_list");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						return freeItemNodes;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}

	/**
	 * 检查返回的xml是否为空并且返回结果是否为成功
	 * @param rspXml
	 * @param res
	 * @return Element
	 */
	private Element checkReturnXml(String rspXml,QRY040075Result res)
	{
		Element root = this.getElement(rspXml.getBytes());
		if(null!=root){
			String resp_code = root.getChild("response").getChildText("resp_code");
			String resp_desc = root.getChild("response").getChildText("resp_desc");
			
			String resultCode = BOSS_SUCCESS.equals(resp_code) ? LOGIC_SUCESS : LOGIC_ERROR;
			if(null != rspXml && !"".equals(rspXml))
			{
				if(!LOGIC_SUCESS.equals(resultCode) || null == root)
				{
					res.setResultCode(resultCode);
					res.setErrorCode(resp_code);
					res.setErrorMessage(resp_desc);
				}
				else
				{
					res.setResultCode(resultCode);
					res.setErrorCode(resp_code);
					res.setErrorMessage(resp_desc);
					return root;
				}
			}
		}
		return null;
	}
	/**
	 * 检查有无20元封顶业务
	 * @param packageCode
	 * @param res
	 * @return
	 */
	private boolean checkMaxFlux(String packageCode,QRY040075Result res)
	{
		//判断是否是否匹配编码
		if(maxCode.contains(packageCode)||maxCode.contains(packageCode.substring(packageCode.length()-4, packageCode.length())))
		{
			return true;
		}
		return false;
	}
	private Set<String> getLTEGRPS(String ... type) {
		return this.wellFormedDAO.getLTE4GCode(type);
	}
	
	/**
	 * 根据网络统计流量使用量
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private QRY040075Result qryGprsSectionUsedFlux(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040075Result res){
		int TwoNetFlux = 0;
		int ThreeNetFlux = 0;
		int FlourNetFlux = 0;
		List<Node> sectionList = qryGprsSectionFluxInfo(accessId,config,params,res); 
		if(null != sectionList && sectionList.size() > 0){
			for(int i = 0;i < sectionList.size();i++){
				Node sectionNode = sectionList.get(i);
				String netType = sectionNode.selectSingleNode("gprs_net_type").getText().trim();
				int tempTotal = Integer.parseInt(sectionNode.selectSingleNode("gprs_flux").getText().trim());
				if(!"".equals(netType) && "2".equals(netType)){
					TwoNetFlux = TwoNetFlux + tempTotal;
				}
				if(!"".equals(netType) && "3".equals(netType)){
					ThreeNetFlux = ThreeNetFlux + tempTotal;
				}
				if(!"".equals(netType) && "4".equals(netType)){
					FlourNetFlux = FlourNetFlux + tempTotal;
				}
			}
		}
		res.setTwoNetFlux(String.valueOf(TwoNetFlux));
		res.setThreeNetFlux(String.valueOf(ThreeNetFlux));
		res.setFourNetFlux(String.valueOf(FlourNetFlux));
		return res;
	}
	
	/**
	 * 查询用户日流量使用情况
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @return
	 */
	private List<Node> qryGprsSectionFluxInfo(String accessId, ServiceConfig config, List<RequestParameter> params,QRY040075Result res)
	{
		//若传的年月份是当月的去当前天数，不是则取每个月的31天
		String cycle =(String)getParameters(params,"cycle");
		String dateNow = DateTimeUtil.getTodayChar6();
		if(!"".equals(cycle) && cycle.equals(dateNow)){
			setParameter(params, "daynumber",DateTimeUtil.getTodayDay());
		}else{
			setParameter(params, "daynumber","31");
		}
		

		try{
			String reqXml = this.bossTeletextUtil.mergeTeletext("cc_4gqrygprsdayflux_70", params);
			logger.debug(" ====== 查询用户日流量使用情况 ======\n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				String rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_4gqrygprsdayflux_70", this.generateCity(params)));
				logger.debug(" ====== 返回报文 ======\n" + rspXml);
				Element root = checkReturnXml(rspXml,res);
				if(null != root)
				{
					org.dom4j.Document doc = DocumentHelper.parseText(rspXml);
					List<Node> freeItemNodes = doc.selectNodes("/operation_out/content/gprs_list");
					if(null != freeItemNodes && freeItemNodes.size()>0)
					{
						return freeItemNodes;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.error(e, e);
		}
		return null;
	}


}
