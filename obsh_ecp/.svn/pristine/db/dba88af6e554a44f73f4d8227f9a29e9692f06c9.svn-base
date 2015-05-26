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
import com.xwtech.xwecp.service.logic.pojo.DEL011001Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * E-MAIL帐单定制-短厅
 * @author chenxiaoming_1037
 * 2011-12-01
 */
public class CustomEmailBill4SMSInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(CustomEmailBill4SMSInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	//报文解析类
	private ParseXmlConfig config;
	
	private static final String BILL_TYPE_EMAIL = "1";
	
	public CustomEmailBill4SMSInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL011001Result res = new DEL011001Result();
		RequestParameter parm = null;
		try
		{
			//获取用户默认帐户ID
			String customize_account_id = this.getAccountId(accessId, config, params);
			//新增参数
			parm = new RequestParameter();
			parm.setParameterName("customize_account_id");
			parm.setParameterValue(customize_account_id);
			params.add(parm);
			
			//帐单定制
			this.cutstomizeBill(accessId, config, params, res);
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return res;
	}
	
	/**
	 * 账单、话单定制
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	public void cutstomizeBill (String accessId, ServiceConfig config, 
			    List<RequestParameter> params, DEL011001Result res)
	{
		String reqXml = "";
		String rspXml = "";  //返回报文
		Element root = null;  //根节点
		String resp_code = "";  //错误码
		
		try
		{
			//发送并接收报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_ccustomizebill_801", params);
			logger.debug(" ====== 帐单，话单定制发送报文 ======\n" + reqXml);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(reqXml, 
					 accessId, "cc_ccustomizebill_801", this.generateCity(params)));
			logger.debug(" ====== 帐单，话单定制返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					res.setErrorCode(resp_code);
					res.setErrorMessage(this.config.getChildText(this.config.getElement(root, "response"), "resp_desc"));
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
