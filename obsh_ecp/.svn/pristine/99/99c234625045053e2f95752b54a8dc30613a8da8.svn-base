package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY020004Result;
import com.xwtech.xwecp.service.logic.pojo.SendMailList;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;
import com.xwtech.xwecp.util.StringUtil;

/**
 * E-MAIL方式定制
 * @author yuantao
 * 2010-01-14
 */
public class QueryEmailBizStateInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(QueryMobileClubInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
//	private WellFormedDAO wellFormedDAO;
	
	//报文解析类
	private ParseXmlConfig config;
	
	/**
	 * customize_remind_typ
	 * 2:email
	 * 3:邮寄
	 */
//	private static final String BILL_TYPE_EMAIL = "2";
	
	//寄送类型
	private Map<String,String> map;
	
	public QueryEmailBizStateInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
//		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
		/** 加载寄送类型
		 * 1	帐单	
		 * 2	语音清单	
		 * 3	集团短号清单	
		 * 4	IP直通车清单	
		 * 5	GPRS清单	
		 * 6	梦网清单	
		 * 7	短信清单	
		 * 8	WLAN清单	
		 * 9	MPIC清单	
		 * 10	代理ISP清单	
		 * 11	彩信清单	
		 * 12	17202清单	
		 * 13	96121清单	
		 * 14	无线音乐清单	
		 * 15	LBS清单	
		 */
		if (null == this.map)
		{
			this.map = new HashMap<String,String>();
			this.map.put("100", "1");
			this.map.put("1", "2");
			this.map.put("8", "3");
			this.map.put("2", "4");
			this.map.put("7", "5");
			this.map.put("17", "6");
			this.map.put("6", "7");
			this.map.put("9", "8");
			this.map.put("10", "9");
			this.map.put("3", "10");
			this.map.put("5", "11");
			this.map.put("14", "12");
			this.map.put("11", "13");
			this.map.put("32", "14");
			this.map.put("34", "15");
		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		QRY020004Result res = new QRY020004Result();
		res.setResultCode("0");  //成功
		
		try
		{
			//获取用户默认帐户ID
			String customize_account_id = this.getAccountId(accessId, config, params);
			if (null != customize_account_id && !"".equals(customize_account_id))
			{
				//账单定制信息查询
				this.getCustomBill(accessId, config, params, customize_account_id, res);
				//话单信息查询
				this.getCustomBillQd(accessId, config, params, customize_account_id, res);
				//日期格式：YYYYMM
				String strDate = this.config.getTodayChar14();
				//账单、话单寄送状态查询 ---寄送状态已屏蔽
				//this.getCustBillState(accessId, config, params, res, strDate);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		
		return res;
	}
	
	/**
	 * 账单、话单寄送状态查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void getCustBillState (String accessId, ServiceConfig config, 
	         List<RequestParameter> params, QRY020004Result res, String strDate)
	{
		String rspXml = "";  //返回报文
//		RequestParameter par = null;  //新增参数
		Element root = null;  //根节点
		String resp_code = "";  //错误码
		
		try
		{
			//发送并接收报文
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(this.bossTeletextUtil.mergeTeletext("cc_cgetcuststate_316", params), 
					 accessId, "cc_cgetcuststate_316", this.generateCity(params)));
			logger.debug(" ====== 寄送状态查询返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				if (null != resp_code && !"".equals(resp_code) && "0000".equals(resp_code))
				{
					//寄送状态 1:寄送中 2:已寄送
					//res.setSendState(Integer.parseInt(this.config.getChildText(root, "ecard_state")));
				}
				else  //3:未寄送
				{
					//res.setSendState(3);
				}
			}
			else  //3:未寄送
			{
				//res.setSendState(3);
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 话单信息查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param customize_account_id
	 * @param res
	 */
	public void getCustomBillQd (String accessId, ServiceConfig config, 
			         List<RequestParameter> params, String customize_account_id, 
			         QRY020004Result res)
	{
		String rspXml = "";  //返回报文
//		RequestParameter par = null;  //新增参数
		Element root = null;  //根节点
		String resp_code = "";  //错误码
		SendMailList sendMail = null;
		List<SendMailList> mailList = null;
		
		try
		{
			//设置type
			for (RequestParameter param : params)
			{
				if ("type".equals(param.getParameterName()))
				{
					param.setParameterValue("1");
				}
			}
			String mobile = (String)getParameters(params, "context_login_msisdn");
			//发送并接收报文
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(this.bossTeletextUtil.mergeTeletext("cc_cgetcustom_307", params), 
					 accessId, "cc_cgetcustom_307", this.generateCity(params)));
			logger.debug(" ====== 查询帐单，话单定制信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				if (null != resp_code && !"".equals(resp_code) && "0000".equals(resp_code))
				{
					List userIdList = this.config.getContentList(root, "user_id");
					if (null != userIdList && userIdList.size() > 0)
					{
						if (null != res.getSendMailList() && res.getSendMailList().size() > 0)
						{
							mailList = res.getSendMailList();
						}
						else
						{
							mailList = new ArrayList();
						}
						String customize_address = "";
						for (int i = 0; i < userIdList.size(); i++)
						{
							Element ccallcustomizedt = this.config.getElement((Element)userIdList.get(i), "ccallcustomizedt");
							sendMail = new SendMailList();
							//寄送类型
							sendMail.setSendType(String.valueOf(this.map.get(this.config.getChildText(ccallcustomizedt, "customize_cdr_type"))));
							mailList.add(sendMail);
							res.setSendMailList(mailList);
							//Email地址
							
							customize_address = this.config.getChildText(ccallcustomizedt, "customize_address");
							if(StringUtil.isNull(customize_address)){
								customize_address = mobile + "@139.com";
							}
							
							if(StringUtil.isNull(res.getEmail()))
							{
								res.setEmail(customize_address);
							}
						}
					}
				}else{//为定制，仍然显示139邮箱
					if(null == res.getEmail() || "".equals(res.getEmail())){
						res.setEmail(mobile + "@139.com");
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
	/**
	 * 账单定制信息查询
	 * @param accessId
	 * @param config
	 * @param params
	 * @param customize_account_id
	 * @param res
	 */
	public void getCustomBill (String accessId, ServiceConfig config, 
			         List<RequestParameter> params, String customize_account_id, 
			         QRY020004Result res)
	{
		String rspXml = "";  //返回报文
		RequestParameter par = null;  //新增参数
		Element root = null;  //根节点
		String resp_code = "";  //错误码
		SendMailList sendMail = null;
		List<SendMailList> mailList = null;
		
		try
		{
			//新增参数customize_account_id
			par = new RequestParameter();
			par.setParameterName("customize_account_id");
			par.setParameterValue(customize_account_id);
			params.add(par);
			par = new RequestParameter();
			par.setParameterName("type");
			par.setParameterValue("0");
			params.add(par);
			
			//发送并接收报文
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(this.bossTeletextUtil.mergeTeletext("cc_cgetcustom_307", params), 
					 accessId, "cc_cgetcustom_307", this.generateCity(params)));
			logger.debug(" ====== 查询帐单，话单定制信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				if (null != resp_code && !"".equals(resp_code) && "0000".equals(resp_code))
				{
					List accountIdList = this.config.getContentList(root, "customize_account_id");
					if (null != accountIdList && accountIdList.size() > 0)
					{
						mailList = new ArrayList();
						String customize_is = "";
						for (int i = 0; i < accountIdList.size(); i++)
						{
							Element cacccustomizedt = this.config.getElement((Element)accountIdList.get(i), "cacccustomizedt");
							if (customize_account_id.equalsIgnoreCase(this.config.getChildText(cacccustomizedt, "customize_account_id")) 
							    && "2".equalsIgnoreCase(this.config.getChildText(cacccustomizedt, "customize_remind_type")))
							{
								sendMail = new SendMailList();
								//寄送类型：账单
								sendMail.setSendType(String.valueOf(this.map.get("100")));
								mailList.add(sendMail);
								res.setSendMailList(mailList);
								//Email地址
								res.setEmail(this.config.getChildText(cacccustomizedt, "customize_address"));
								//是否接收宣传附件
								customize_is = this.config.getChildText(cacccustomizedt, "customize_is");
								if(!StringUtil.isNull(customize_is)){
									res.setRecvFlag(Integer.parseInt(customize_is));
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
		}
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
			logger.debug(" ====== 查询账务信息返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				Element root = this.config.getElement(rspXml);
				String resp_code = root.getChild("response").getChildText("resp_code");
				//String resp_desc = root.getChild("response").getChildText("resp_desc");
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
