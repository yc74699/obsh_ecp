package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
import com.xwtech.xwecp.service.logic.pojo.DEL100005Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 商城活动-营销方案校验、办理功能
 * @author YG
 * 
 */
public class MarketPlanTransInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(MarketPlanTransInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	
	public MarketPlanTransInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
	}
	
	public BaseServiceInvocationResult executeService(String accessId, 
			                ServiceConfig config, List<RequestParameter> params)
	{
		DEL100005Result res = new DEL100005Result();

		try
		{
			//res.setResultCode(LOGIC_SUCESS);
			//res.setErrorMessage("");
			//功能类型
			String functionType = (String)getParameters(params, "functionType"); //1出货或退货功能     ;2换货功能
			//父订单号
			String servnumber  = (String)getParameters(params, "servnumber"); // 手机号码
			//活动编码
			String actid  = (String)getParameters(params, "actid"); 
			//档次编码
			String privid  = (String)getParameters(params, "privid"); 
			//物品包编码
			String packid  = (String)getParameters(params, "packid"); 
			//业务包编码
			String busidpackid = (String)getParameters(params, "busidpackid"); 
			//领取方式
			String gettype = (String)getParameters(params, "gettype"); // functionType=2时以下规则传入：3 -配送 2-营业厅支取  functionType=1时传空

			//子订单号
			String orderid = (String)getParameters(params, "orderid"); //functionType=2时传入子订单编号  functionType=1时传空

			BaseResult terminalSaleRet = null;
			//1-校验
			if ("1".equals(functionType)) {
				//传入参数校验 --非空
				if(StringUtils.isBlank(servnumber) || StringUtils.isBlank(privid)){
					res.setResultCode(LOGIC_PARAM_ERROR);
					return res;
				}
			
				
				 this.doMarketPlanCheck(accessId, config, params,res);
				
			} else { //2-	办理
				
				//约束
				if(StringUtils.isBlank(orderid)){
					res.setResultCode(LOGIC_PARAM_ERROR);
					return res;
				}
				this.doMarketPlanApply(accessId, config, params,res);
			}
		
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 营销方案校验
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected void doMarketPlanCheck(final String accessId, final ServiceConfig config, final List<RequestParameter> params,DEL100005Result res) {
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_marketplancheck_1007", params);

			logger.debug(" ====== 营销方案校验请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_marketplancheck_1007", this.generateCity(params)));
			logger.debug(" ====== 营销方案校验 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL100005", "cc_marketplancheck_1007", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				if(BOSS_SUCCESS.equals(errCode))
				{
					Element content = root.getChild("content");
					String privId = p.matcher(content.getChildText("NEWPRIVID") == null ? "" : content.getChildText("NEWPRIVID")).replaceAll("");
					String privName = p.matcher(content.getChildText("NEWPRIVNAME") == null ? "" : content.getChildText("NEWPRIVNAME")).replaceAll("");
					String startDate = p.matcher(content.getChildText("NEWPRIVSTARTDATE") == null ? "" :  content.getChildText("NEWPRIVSTARTDATE")).replaceAll("");
					String endDate = p.matcher(content.getChildText("NEWPRIVENDDATE")== null ? "" : content.getChildText("NEWPRIVENDDATE")).replaceAll("");
					res.setNewPrivId(privId);
					res.setNewPrivName(privName);
					res.setNewPrivStartDate(startDate);
					res.setNewPrivEndDate(endDate);
				}
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	/**
	 * 营销方案办理
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	protected void doMarketPlanApply(final String accessId, final ServiceConfig config, final List<RequestParameter> params,DEL100005Result res) {
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try {
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_marketplanapply_1008", params);

			logger.info(" ====== 换货接口 请求报文 ======\n" + reqXml);

			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, "cc_marketplanapply_1008", this.generateCity(params)));
			logger.info(" ====== 换货接口 返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.getElement(rspXml.getBytes());
				String errCode = root.getChild("response").getChildText("resp_code");
				String errDesc = root.getChild("response").getChildText("resp_desc");
				
				if (!BOSS_SUCCESS.equals(errCode))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL100005", "cc_marketplanapply_1008", errCode);
					if (null != errDt)
					{
						errCode = errDt.getLiErrCode();
						errDesc = errDt.getLiErrMsg();
					}
				}
				res.setResultCode(BOSS_SUCCESS.equals(errCode)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(errCode);
				res.setErrorMessage(errDesc);
				if(null != errCode && (BOSS_SUCCESS.equals(errCode))){
					Element content = root.getChild("content");
					String recoid = p.matcher(content.getChildText("recoid")).replaceAll("");
					res.setRecoid(recoid);
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}
