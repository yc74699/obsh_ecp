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
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.DEL011001Result;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.SendMailList;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * E-MAIL帐单定制
 * @author yuantao
 * 2010-01-14
 */
public class CustomEmailBillInvocation extends BaseInvocation implements ILogicalService
{
	private static final Logger logger = Logger.getLogger(CustomEmailBillInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	//报文解析类
	private ParseXmlConfig config;
	
	//寄送类型
	private Map map;
	
	/**
	 * customize_remind_typ
	 * 2:email
	 * 3:邮寄
	 */
	private static final String BILL_TYPE_EMAIL = "2";
	
	public CustomEmailBillInvocation ()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
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
			this.map = new HashMap();
			this.map.put("1", "100");
			this.map.put("2", "1");
			this.map.put("3", "8");
			this.map.put("4", "2");
			this.map.put("5", "7");
			this.map.put("6", "17");
			this.map.put("7", "6");
			this.map.put("8", "9");
			this.map.put("9", "10");
			this.map.put("10", "3");
			this.map.put("11", "5");
			this.map.put("12", "14");
			this.map.put("13", "11");
			this.map.put("14", "32");
			this.map.put("15", "34");
		}
	}
	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config, List<RequestParameter> params)
	{
		DEL011001Result res = new DEL011001Result();
		List<SendMailList> mailList = null;
		List<SendMailList> sendList = new ArrayList();
		List<SendMailList> newList = new ArrayList();
		SendMailList mailDt = null;
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
			
			for (RequestParameter par : params)
			{
				//转换寄送类型
				if ("sendMailList".equals(par.getParameterName()))
				{
					mailList = (List)par.getParameterValue();
					if (null != mailList && mailList.size() > 0)
					{
						for (SendMailList dt : mailList)
						{
							if (!"1".equals(dt.getSendType()))
							{
//								mailDt = new SendMailList();
//								mailDt.setSendType(String.valueOf(this.map.get(dt.getSendType())));
//								sendList.add(mailDt);
							}
						}
					}
					SendMailList newDt = new SendMailList();
					newDt.setSendType("100");
					newList.add(newDt);
					par.setParameterValue(newList);
				}
			}
			
			//帐单定制/关闭
			this.cutstomizeBill(accessId, config, params, res, "0");
			
			if ("0".equals(res.getResultCode()))
			{
				//替换寄送类型
				for (RequestParameter par : params)
				{
					//转换寄送类型
					if ("sendMailList".equals(par.getParameterName()))
					{
						par.setParameterValue(sendList);
					}
				}
				//清单定制/关闭
				if(sendList!=null && sendList.size()>0){
				this.cutstomizeBill(accessId, config, params, res, "1");}
			}
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
	 * @param type
	 */
	public void cutstomizeBill (String accessId, ServiceConfig config, 
			    List<RequestParameter> params, DEL011001Result res, String type)
	{
		RequestParameter par = null;
		boolean typeBool = true;
		String reqXml = "";
		String rspXml = "";  //返回报文
		Element root = null;  //根节点
		String resp_code = "";  //错误码
		ErrorMapping errDt = null;
		
		try
		{
			//设置参数type类型
			for (RequestParameter param : params)
			{
				if ("type".equalsIgnoreCase(param.getParameterName()))
				{
					typeBool = false;
					param.setParameterValue(type);
				}
			}
			if (typeBool)
			{
				par = new RequestParameter();
				par.setParameterName("type");
				par.setParameterValue(type);
				params.add(par);
				par = new RequestParameter();
				par.setParameterName("customize_remind_type");
				par.setParameterValue(BILL_TYPE_EMAIL);
				params.add(par);
				//发送日期，每月的天数(固定为0)
				par = new RequestParameter();
				par.setParameterName("customize_send_day");
				par.setParameterValue("0");
				params.add(par);
			}
			
			//发送并接收报文
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_ccutstomize_308", params);
			logger.debug(" ====== 帐单，话单定制发送报文 ======\n" + reqXml);
			rspXml = (String)this.remote.callRemote(
					 new StringTeletext(reqXml, 
					 accessId, "cc_ccutstomize_308", this.generateCity(params)));
			logger.debug(" ====== 帐单，话单定制返回报文 ======\n" + rspXml);
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("DEL011001", "cc_ccutstomize_308", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
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
