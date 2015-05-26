package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;
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
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040040Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 语音话单
 * @author gongww
 * 2009-12-04
 */
public class QueryCashLeaveMoneyInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryCashLeaveMoneyInvocation.class);
	private BossTeletextUtil bossTeletextUtil;
	private IRemote remote;
	private WellFormedDAO wellFormedDAO;
	private ParseXmlConfig config;
	
	public QueryCashLeaveMoneyInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY040040Result res = new QRY040040Result();
		RequestParameter parm = null;
		String reqXml = "";
		String rspXml = "";
		ErrorMapping errDt = null;
		try
		{
			//获取用户默认帐户ID
			String account_id = this.getAccountId(accessId, config, params);
			parm = new RequestParameter();
			parm.setParameterName("accid");
			parm.setParameterValue(account_id);
			params.add(parm);
			parm = new RequestParameter();
			parm.setParameterName("subjecttype");
			parm.setParameterValue("13");
			params.add(parm);
			parm = new RequestParameter();
			parm.setParameterName("isdeductdebt");
			parm.setParameterValue("");
			params.add(parm);
			reqXml= this.bossTeletextUtil.mergeTeletext("cc_qryaccbalancebysubject_2040", params);
			rspXml = (String)this.remote.callRemote(new StringTeletext(reqXml, accessId, 
					"cc_qryaccbalancebysubject_2040", this.generateCity(params)));
			if(null !=rspXml && !"".equals(rspXml)){
				Element root = this.getElement(rspXml.getBytes());
				String resp_code = root.getChild("response").getChildText("resp_code");
				String resp_desc = root.getChild("response").getChildText("resp_desc");
				res.setResultCode(BOSS_SUCCESS.equals(resp_code)?LOGIC_SUCESS:LOGIC_ERROR);
				res.setErrorCode(resp_code);
				res.setErrorMessage(resp_desc);
				if (!BOSS_SUCCESS.equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL010001", "cc_qryaccbalancebysubject_2040", resp_code);
					if (null != errDt)
					{
						resp_code = errDt.getLiErrCode();
						resp_desc = errDt.getLiErrMsg();
						res.setErrorCode(resp_code);
						res.setErrorMessage(resp_desc);
					}
				}
				if (null != resp_code && "0000".equals(resp_code)) {
					Element content = root.getChild("content");
					if (content != null) {
						res.setLeftMoney(content.getChildText("leftmoney"));
						res.setInvalidDate(content.getChildText("invalidate"));
					}
					}
				}
				
			
		}
			catch (Exception e)
		{
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
	protected String getAccountId(String accessId, ServiceConfig config, List<RequestParameter> params) {
		String accountId = "";
		String rspXml = "";
		
		try {
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(this.bossTeletextUtil.mergeTeletext("ac_getrelbyuser_608", params), 
					 accessId, "ac_getrelbyuser_608", this.generateCity(params)));
			logger.info(" ====== 查询账务信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = root.getChild("response").getChildText("resp_code");
				if (null != resp_code && ("0000".equals(resp_code))) {
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
								String isDefault = this.config.getChildText(accountDt, "acrelation_isdefault"); 
								
								if (isDefault != null && "1".equals(isDefault)) {
									accountId = this.config.getChildText(accountDt, "account_id");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
			return null;
		}
		return accountId;
	}
}
