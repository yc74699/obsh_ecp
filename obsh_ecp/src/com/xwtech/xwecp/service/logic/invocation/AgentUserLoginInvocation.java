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
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040018Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

/**
 * 代理商登陆接口
 * @author Tkk
 *
 */
public class AgentUserLoginInvocation extends BaseInvocation implements ILogicalService {
	
	private static final Logger logger = Logger.getLogger(AgentUserLoginInvocation.class);

	
	private WellFormedDAO wellFormedDAO;
	/**
	 * 组装报文
	 */
	private BossTeletextUtil bossTeletextUtil = null;

	/**
	 * 调用Boss接口
	 */
	private IRemote remote = null;
	
	/**
	 * 查询客户资料接口
	 */
	private static String USER_INFO_BOSS_INTERFACE = "cc_cgetusercust_69";
	
	/**
	 * 代理商登陆
	 */
	private static String AGENT_USER_LOGIN_BOSS_INTERFACE = "cc_cqryagentuser_321";
	
	public AgentUserLoginInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx.getBean("bossTeletextUtil"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx.getBean("wellFormedDAO"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
	}

	
	public BaseServiceInvocationResult executeService(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		QRY040018Result result = new QRY040018Result();
		result.setResultCode("0");
		
		try {
			//查询客户资料接口,获取地市
			String rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(USER_INFO_BOSS_INTERFACE, params), accessId, USER_INFO_BOSS_INTERFACE, this.generateCity(params)));
			String city = getCity4Xml(rspXml);
			if(city != null){
				for(RequestParameter param : params){
					if("fixed_ddr_city".equals(param.getParameterName())){
						param.setParameterValue(city);
						break;
					}
				}
				RequestParameter rp = new RequestParameter();
				rp.setParameterName("context_ddr_city");
				rp.setParameterValue(city);
				params.add(rp);
			}
			
			//开始调用,代理商登陆接口
			rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext(AGENT_USER_LOGIN_BOSS_INTERFACE, params), accessId, AGENT_USER_LOGIN_BOSS_INTERFACE, this.generateCity(params)));
			Document doc = DocumentHelper.parseText(rspXml);
			String errorCode = doc.selectSingleNode("/operation_out/response/resp_code").getText();
			String errorMessage = doc.selectSingleNode("/operation_out/response/resp_desc").getText();
			result.setErrorCode(errorCode);
			result.setErrorMessage(errorMessage);
			if("0000".equals(errorCode)){
				result.setResultCode(LOGIC_SUCESS);
				
				//用户id
				Node node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_agent_user_id");
				result.setAgentUserId(getNodeText(node));
				
				//用户等级
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_agent_level");
				result.setAgentLevel(getNodeText(node));
				
				//手机号码
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_msisdn");
				result.setAgentMsisdn(getNodeText(node));
				
				//代理商状态
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_state");
				result.setAgentState(getNodeText(node));
				
				//受理时间
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_apply_date");
				result.setApplyDate(getNodeText(node));
				
				//城市
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_city_id");
				result.setCityId(getNodeText(node));
				
				//县市
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_county_id");
				result.setCountryId(getNodeText(node));
				
				//结束时间
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_end_date");
				result.setEndDate(getNodeText(node));
				
				//上级代理商Id
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_superior_user_id");
				result.setSuperAgentUserId(getNodeText(node));
				
				//组编号
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_group_user_id");
				result.setGroupId(getNodeText(node));
				
				//操作员
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_change_operator");
				result.setModifyOperator(getNodeText(node));
				
				//操作日期
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_change_date");
				result.setModifyDate(getNodeText(node));
				
				//操作备注
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_change_remark");
				result.setModifyRemark(getNodeText(node));
				
				//代理商客户Id
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_customer_id");
				result.setAgentCustomerId(getNodeText(node));
				
				//代理商名称
				node = doc.selectSingleNode("/operation_out/content/agentuser_agent_user_id/dtagentuser/agentuser_customer_name");
				result.setAgentName(getNodeText(node));
				
				
				//对代理商密码进行校验
				String agent_user_id = result.getAgentUserId();
				if(agent_user_id != null){
					RequestParameter rp = new RequestParameter();
					rp.setParameterName("agent_user_id");
					rp.setParameterValue(agent_user_id);
					params.add(rp);
					RequestParameter rp1 = new RequestParameter();
					rp1.setParameterName("context_route_type");
					rp1.setParameterValue("1");
					params.add(rp1);
				}
				 rspXml = (String) this.remote.callRemote(new StringTeletext(this.bossTeletextUtil.mergeTeletext("VerifyAgentUserPwd_607", params), accessId, "VerifyAgentUserPwd_607", this.generateCity(params)));
				 Document doc1 = DocumentHelper.parseText(rspXml);
                 String errorCode1 = doc1.selectSingleNode("/operation_out/response/resp_code").getText();
                 if (!"0000".equals(errorCode1)) {
                	 String errDesc = "";
                	 result.setResultCode(LOGIC_ERROR);
                	 ErrorMapping errDt = this.wellFormedDAO.transBossErrCode("QRY040018", "VerifyAgentUserPwd_607", errorCode1);
 					if (null != errDt) {
 						errorCode1 = errDt.getLiErrCode();
 						errDesc = errDt.getLiErrMsg();
 					}
 					result.setErrorCode(errorCode1);
 					result.setErrorMessage(errDesc);
                 }
			}
			else
			{
				result.setResultCode(LOGIC_ERROR);
			}
			
		}  catch (Exception e) {
			logger.error(e, e);
			result.setResultCode(LOGIC_ERROR);
		}
		return result;
	}
	
	/**
	 * 获取地市
	 * @param rspXml
	 * @return
	 * @throws DocumentException 
	 */
	private String getCity4Xml(String rspXml) throws DocumentException {
		String city = null;
		Document doc = DocumentHelper.parseText(rspXml);
		List<Node>	nodeList = doc.selectNodes("/operation_out/content/user_info/user_city");
		for(Node node : nodeList){
			city = node.getText();
		}
		return city;
	}
	
	/**
	 * 获取元素的值
	 * @param node
	 * @return
	 */
	private String getNodeText(Node node){
		if(node == null){
			return "";
		}
		return node.getText();
	}

	
}
