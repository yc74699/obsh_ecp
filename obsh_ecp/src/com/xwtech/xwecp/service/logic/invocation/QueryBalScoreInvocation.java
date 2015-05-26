package com.xwtech.xwecp.service.logic.invocation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import com.xwtech.xwecp.service.logic.pojo.DEL040005Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GommonBusiness;
import com.xwtech.xwecp.service.logic.pojo.NewScoreDetail;
import com.xwtech.xwecp.service.logic.pojo.QRY020001Result;
import com.xwtech.xwecp.service.logic.pojo.QRY030011Result;
import com.xwtech.xwecp.service.logic.pojo.QRY040002Result;
import com.xwtech.xwecp.service.logic.pojo.ScoreYearNum;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 获取用户积分
 * @author Tkk
 *
 */
public class QueryBalScoreInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(CheckPasswordInvocation.class);
	
	/**
	 * BOSS接口
	 */
	private static final String BOSS_INTERFACE = "cc_cgetuseraccscore_770";
	
	/**
	 * 组装报文
	 */
	private BossTeletextUtil bossTeletextUtil = null;
	
	/**
	 * 调用Boss接口
	 */
	private IRemote remote = null;
	
	private WellFormedDAO wellFormedDAO = null;
	
	public QueryBalScoreInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		
		QRY040002Result result = new QRY040002Result();
		
		//组装请求报文
		String reqXml = bossTeletextUtil.mergeTeletext(BOSS_INTERFACE, params);
		
		//调用Boss接口
		try {
			String rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, BOSS_INTERFACE, this.generateCity(params)));
			
			if (null != rspXml && !"".equals(rspXml)) {
				parseXmlConversionObj(rspXml, result);
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
		
		return result;
	}
	
	/**
	 * 解析Xml转换为QRY040002对象
	 * @param rspXml
	 * @param result
	 */
	private void parseXmlConversionObj(String rspXml, QRY040002Result result) {
		StringReader stringReader = new StringReader(rspXml);
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Document doc = builder.build(new InputSource(stringReader));
			//获取operation_root节点
			Element root = doc.getRootElement();
			
			//获取返回信息
			Element responseEl = root.getChild("response");
			if(responseEl != null){
				setResponseInfo(responseEl, result);
			}
			
			//获取Content节点
			Element content = root.getChild("content");
			
			//获取余额信息
			Element balanceEl = content.getChild("balance_response");
			if(balanceEl != null){
				setBalanceInfo(balanceEl, result);
			}
			
			//获取积分信息
			Element scoreEl = content.getChild("score_response");
			if(scoreEl != null){
				setScoreInfo(scoreEl, result);
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
	 * 从Boss报文获取响应状态插入逻辑接口中s
	 * @param responseEl
	 * @param result
	 */
	private void setResponseInfo(Element responseEl, QRY040002Result result) {
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
	private void setZoneMvalueInfo(Element zoneMvalueEl, QRY040002Result result) {
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
	private void setScoreInfo(Element scoreEl, QRY040002Result result) {
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
	private void setBalanceInfo(Element balanceEl, QRY040002Result result) {
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
	
}
