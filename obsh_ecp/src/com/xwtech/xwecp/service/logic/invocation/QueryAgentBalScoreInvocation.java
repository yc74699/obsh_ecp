package com.xwtech.xwecp.service.logic.invocation;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.QRY040015Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 代理商余额
 * 
 * @author Tkk
 * 
 */
public class QueryAgentBalScoreInvocation extends BaseInvocation implements ILogicalService {

	private static final Logger logger = Logger.getLogger(QueryAgentBalScoreInvocation.class);

	/**
	 * 组装报文
	 */
	private BossTeletextUtil bossTeletextUtil = null;

	/**
	 * 调用Boss接口
	 */
	private IRemote remote = null;

	/**
	 * 代理商余额接口
	 */
	private static String AGENT_BOSS_INTEGERFACE = "ac_agetacctbalan_519";

	/**
	 * 割接地市用代理商余额接口
	 */
	private static String AGENT_BOSS_INTEGERFACE_NEEDCUT = "cc_getagentbal_03";
	
	/**
	 * 如果使用代理商余额查询不到 则使用手机余额查询
	 */
	private static String MOBILE_BOSS_INTEGERFACE = "cc_cgetuseraccscore_770";

	/**
	 * 账务关系
	 */
	private static String AGENT_ACCOUNT_BOSS_INTERFACE = "ac_getrelbyuser_608_1";

	public QueryAgentBalScoreInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
	}

	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		QRY040015Result result = new QRY040015Result();

		try {
			//调用ac_agetacctbalan_519的<account_id>
			String accountId ="";
			String rspXml ="";
			String  userCity = (String)this.getParameters(params, "context_ddr_city");
			if(needCutCity(userCity)){
				//割接地市现增加一个接口调用获取正确的账户ID
				accountId = getNewAgentCustomId(accessId, params);
				//accountId = "1842200003120738";
			}else{
			// 首先查询账务关系,获取accountId
				rspXml = getResponseXml(AGENT_ACCOUNT_BOSS_INTERFACE, accessId, params);
				accountId = getAccountId4Xml(rspXml);
			}
			if(accountId != null){
				for(RequestParameter param : params){
					if("context_agentCustomId".equals(param.getParameterName())){
						param.setParameterValue(accountId);
						break;
					}
				}
			}
			
			// 查询余额
			String agentBalance = "";
			if(needCutCity(userCity)){
				
				rspXml = getResponseXml(AGENT_BOSS_INTEGERFACE_NEEDCUT, accessId, params);
				agentBalance = getBalance4Xml(rspXml, result);
			}else{
				rspXml = getResponseXml(AGENT_BOSS_INTEGERFACE, accessId, params);
				agentBalance = getBalance4Xml(rspXml, result);
			}
			
			// 开始调用手机余额
			if (agentBalance == null || "".equals(agentBalance)) {
				//割接地市的agentBalance为空，因为ac_getrelbyuser接口返回的context_agentCustomId不正确
				//现增加一个接口调用获取正确的账户ID，“调用手机余额”逻辑易造成投诉，删除。其他流程不变。
				//agentBalance = getMobileBalance(accessId, params);
			}else{
				result.setResultCode("0");
			}
			// 返回的是分 要转换为元
			if (agentBalance != null) {
				agentBalance = (Float.parseFloat(agentBalance) / 100) + "";
				result.setResultCode("0");
			}
			result.setAgentBalance(agentBalance);

		} catch (Exception e) {
			logger.error(e, e);
		}
		return result;
	}

	/**
	 * 发送报文给boss
	 * 
	 * @param requestXml
	 * @param bossInterface
	 * @param accessId
	 * @param params
	 * @return
	 */
	private String getResponseXml(String bossInterface, String accessId, List<RequestParameter> params) {
		String rspXml = null;
		try {
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(
					bossInterface, params), accessId, bossInterface, this.generateCity(params)));
		} catch (Exception e) {
			logger.error(e, e);
		}
		return rspXml;
	}

	/**
	 * 获取客户Id
	 * 
	 * @param rspXml
	 * @return
	 */
	private String getAccountId4Xml(String rspXml) {
		String accountId = null;
		try {
			Document doc = DocumentHelper.parseText(rspXml);

			List<Node> nodeList = doc
					.selectNodes("/operation_out/content/arecord_count/caccountingrelationdt/account_id");
			for (Node node : nodeList) {
				accountId = node.getText();
			}
		} catch (DocumentException e) {
			logger.error(e, e);
		}

		return accountId;
	}

	/**
	 * 获取手机余额
	 * 
	 * @param params
	 * @return
	 */
	private String getMobileBalance(String accessId, List<RequestParameter> params) {
		// 组装请求报文
		String reqXml = bossTeletextUtil.mergeTeletext(MOBILE_BOSS_INTEGERFACE, params);

		String mobileBalance = null;

		// 调用Boss接口
		try {
			String rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId,
					MOBILE_BOSS_INTEGERFACE, this.generateCity(params)));
			Document doc = DocumentHelper.parseText(rspXml);
			List<Node> nodeList = doc.getRootElement().selectNodes("./content/balance_response/balance");
			for (Node node : nodeList) {
				mobileBalance = node.getText();
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return mobileBalance;
	}

	/**
	 * 获取代理商余额信息
	 * 
	 * @param rspXml
	 * @return
	 */
	private String getBalance4Xml(String rspXml, QRY040015Result result) {
		String balance = null;

		try {
			Document doc = DocumentHelper.parseText(rspXml);
			List<Node> nodeList = doc.getRootElement().selectNodes("./content/balance");
			for (Node node : nodeList) {
				balance = node.getText();
			}
		} catch (Exception e) {
			logger.error("解析返回报文失败, 无法获取余额, 开始调用手机余额接口");
		}
		return balance;
	}

	/**
	 * 华为新增接口获取正确的账户ID
	 * 
	 * @param params
	 * @return
	 */
	private String getNewAgentCustomId(String accessId, List<RequestParameter> params) {
		// 组装请求报文
		String reqXml = bossTeletextUtil.mergeTeletext("cc_getagentacct_02", params);
		String rspXml ="";
		
		logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
		String newAgentCustomId = null;

		// 调用Boss接口
		try {
			if (null != reqXml && !"".equals(reqXml))
			{
			    rspXml = (String) this.remote.callRemote(new StringTeletext(reqXml, accessId,
						"cc_getagentacct_02", this.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			
			
			Document doc = DocumentHelper.parseText(rspXml);
			List<Node> nodeList = doc.getRootElement().selectNodes("./content/acctrelation/acctid");
			if(nodeList != null && nodeList.size() > 0){
				for (Node node : nodeList) {
					newAgentCustomId = node.getText();
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return newAgentCustomId;
	}
	
	
	
}
