package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
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
import com.xwtech.xwecp.service.logic.pojo.AgentUserInfo;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.QRY040016Result;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

public class QueryAgentInfoInvocation extends BaseInvocation implements ILogicalService{
  
private static final Logger logger = Logger.getLogger(QueryAgentInfoInvocation.class);
	
	private BossTeletextUtil bossTeletextUtil;
	
	private IRemote remote;
	
	private WellFormedDAO wellFormedDAO;
	
	private ParseXmlConfig config;

	
	public QueryAgentInfoInvocation()
	{
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
		this.remote = (IRemote)(springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO)(springCtx.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();
	}


	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		
		QRY040016Result res = new QRY040016Result();
		
		//代理商基本信息
		List<AgentUserInfo> agentUserList = null;
		
		//二级代理商信息
		List<AgentUserInfo> agentUserLevelTwoList = null;
		
		//三级代理商信息
		List<AgentUserInfo> agentUserLevelThreeList = null;
		
		//代理商名称
		String agentName = "";
		
		//成员列表
		List<AgentUserInfo> memberList = null;
		
		//成员总数
		int allNumber = 0;
		
		//状态正常的代理商数
		int stateNormalNumber = 0;
		
		AgentUserInfo agentUserInfo = null;
		
		try
		{
			agentUserList = queryAgentInfo(accessId,params,res);
			res.setAgentUserInfo(agentUserList);
			
			if(agentUserList != null && agentUserList.size() > 0)
			{
				//成员总数，首先要把自己加上
			    allNumber = 1;
			    
				agentUserInfo = agentUserList.get(0);
				
				//判断状态是否正常
				if("1".equals(agentUserInfo.getAgentState()))
				{
					stateNormalNumber = 1;
				}
				
				
				RequestParameter p3 = new RequestParameter("customerId",agentUserInfo.getAgentCustomerId());
				params.add(p3);
				queryAgentCustomerInfo(accessId,params,res, agentUserInfo);
				
				//获取下级代理商信息
				//只有一级代理商才需要查询他的组信息以及下级成员信息
				if("1".equals(agentUserInfo.getAgentLevel()))
				{
					RequestParameter p1 = new RequestParameter("groupUserId",agentUserInfo.getGroupId());
					RequestParameter p2 = new RequestParameter("agentLevel","2");
					params.add(p1);
					params.add(p2);
					
					//查询二级代理商
					agentUserLevelTwoList = queryAgentInfoByLevel(accessId,params,res);
					
					for(int i = 0 ; i < params.size() ; i ++)
					{
						RequestParameter pTmp = params.get(i);
						if("agentLevel".equals(pTmp.getParameterName()))
						{
							pTmp.setParameterValue("3");
							break;
						}
					}
					
					//查询三级代理商
					agentUserLevelThreeList = queryAgentInfoByLevel(accessId,params,res);
					
					if((agentUserLevelTwoList != null && agentUserLevelTwoList.size() > 0)
							|| (agentUserLevelThreeList != null && agentUserLevelThreeList.size() > 0))
					{
						memberList = new ArrayList<AgentUserInfo>();
					}
					
					
					if(agentUserLevelTwoList != null && agentUserLevelTwoList.size() > 0)
					{
						allNumber = allNumber + agentUserLevelTwoList.size();
						res.setLevel2Number(String.valueOf(agentUserLevelTwoList.size()));
						memberList.addAll(agentUserLevelTwoList);
						for (int i = 0; i < agentUserLevelTwoList.size(); i++) {
							if ("1".equals(agentUserLevelTwoList.get(i).getAgentState())) {
								stateNormalNumber = stateNormalNumber + 1;
							}
						}
					}
						
					if(agentUserLevelThreeList != null && agentUserLevelThreeList.size() > 0)
					{
						allNumber = allNumber + agentUserLevelThreeList.size();
						res.setLevel3Number(String.valueOf(agentUserLevelThreeList.size()));
						memberList.addAll(agentUserLevelThreeList);
						for (int i = 0; i < agentUserLevelThreeList.size(); i++) {
							if ("1".equals(agentUserLevelThreeList.get(i).getAgentState())) {
								stateNormalNumber = stateNormalNumber + 1;
							}
						}
					}
					
					res.setStateNormalNumber(String.valueOf(stateNormalNumber));
					res.setAllNumber(String.valueOf(allNumber));
					if(memberList != null)
					{
						res.setMemberList(memberList);
					}
				}
			}
			
		}
		catch(Exception e)
		{
			res.setResultCode("1");
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * 查询代理商基本信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<AgentUserInfo> queryAgentInfo(String accessId,List<RequestParameter> params,QRY040016Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<AgentUserInfo>  agentUserList = null;
		AgentUserInfo agentUserInfo = null;
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryagentuser_321", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cqryagentuser_321", this.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY040015", "cc_cqryagentuser_321", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				agentUserList = new ArrayList<AgentUserInfo>(1);
				agentUserInfo = new AgentUserInfo();
				
				Element contentElement = this.config.getElement(root, "content");
				Element agentnfoDetail = this.config.getElement(contentElement, "agentuser_agent_user_id");
				Element agentnfo = this.config.getElement(agentnfoDetail, "dtagentuser");
				
				//用户Id
				agentUserInfo.setAgentUserId(this.config.getChildText(agentnfo, "agentuser_agent_user_id"));
				//代理商级别
				agentUserInfo.setAgentLevel(this.config.getChildText(agentnfo, "agentuser_agent_level"));
				//手机号码
				agentUserInfo.setAgentMsisdn(this.config.getChildText(agentnfo, "agentuser_msisdn"));
				//代理商状态
				agentUserInfo.setAgentState(this.config.getChildText(agentnfo, "agentuser_state"));
				//受理日期
				agentUserInfo.setApplyDate(this.config.getChildText(agentnfo, "agentuser_apply_date"));
				//地市
				agentUserInfo.setCityId(this.config.getChildText(agentnfo, "agentuser_city_id"));
				//县市
				agentUserInfo.setCountryId(this.config.getChildText(agentnfo, "agentuser_county_id"));
				//结束日期
				agentUserInfo.setEndDate(this.config.getChildText(agentnfo, "agentuser_end_date"));
				//上级代理商Id
				agentUserInfo.setSuperAgentUserId(this.config.getChildText(agentnfo, "agentuser_superior_user_id"));
				//组编号
				agentUserInfo.setGroupId(this.config.getChildText(agentnfo, "agentuser_group_user_id"));
				//操作员
				agentUserInfo.setModifyOperator(this.config.getChildText(agentnfo, "agentuser_change_operator"));
				//操作日期
				agentUserInfo.setModifyDate(this.config.getChildText(agentnfo, "agentuser_change_date"));
				//操作备注
				agentUserInfo.setModifyRemark(this.config.getChildText(agentnfo, "agentuser_change_remark"));
				//代理商客户Id
				agentUserInfo.setAgentCustomerId(this.config.getChildText(agentnfo, "agentuser_customer_id"));
				//代理商名称
				agentUserInfo.setAgentName(this.config.getChildText(agentnfo, "agentuser_customer_id"));
				agentUserList.add(agentUserInfo);
		    }
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return agentUserList;
	}
	
	/**
	 * 查询二级和三级代理商信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public List<AgentUserInfo> queryAgentInfoByLevel(String accessId,List<RequestParameter> params,QRY040016Result res)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		List<AgentUserInfo>  agentUserList = null;
		AgentUserInfo agentUserInfo = null;
		ErrorMapping errDt = null;
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryagentuser_321_1", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cqryagentuser_321_1", this.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				if("0000".equals(resp_code) || "0".equals(resp_code)){
					res.setResultCode("0");
				}else{
					res.setResultCode("1");
				}
//				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY040015", "cc_cqryagentuser_321_1", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				List agentDetailList = this.config.getContentList(root, "agentuser_agent_user_id");
				
				if(agentDetailList != null && agentDetailList.size() > 0)
				{
					agentUserList = new ArrayList<AgentUserInfo>(agentDetailList.size());
					
					for(int i = 0 ; i < agentDetailList.size() ; i ++)
					{
						agentUserInfo = new AgentUserInfo();
						
						Element agentnfo = this.config.getElement((Element)agentDetailList.get(i), "dtagentuser");
						
						//用户Id
						agentUserInfo.setAgentUserId(this.config.getChildText(agentnfo, "agentuser_agent_user_id"));
						//代理商级别
						agentUserInfo.setAgentLevel(this.config.getChildText(agentnfo, "agentuser_agent_level"));
						//手机号码
						agentUserInfo.setAgentMsisdn(this.config.getChildText(agentnfo, "agentuser_msisdn"));
						//代理商状态
						agentUserInfo.setAgentState(this.config.getChildText(agentnfo, "agentuser_state"));
						//受理日期
						agentUserInfo.setApplyDate(this.config.getChildText(agentnfo, "agentuser_apply_date"));
						//地市
						agentUserInfo.setCityId(this.config.getChildText(agentnfo, "agentuser_city_id"));
						//县市
						agentUserInfo.setCountryId(this.config.getChildText(agentnfo, "agentuser_county_id"));
						//结束日期
						agentUserInfo.setEndDate(this.config.getChildText(agentnfo, "agentuser_end_date"));
						//上级代理商Id
						agentUserInfo.setSuperAgentUserId(this.config.getChildText(agentnfo, "agentuser_superior_user_id"));
						//组编号
						agentUserInfo.setGroupId(this.config.getChildText(agentnfo, "agentuser_group_user_id"));
						//操作员
						agentUserInfo.setModifyOperator(this.config.getChildText(agentnfo, "agentuser_change_operator"));
						agentUserInfo.setModifyDate(this.config.getChildText(agentnfo, "agentuser_change_date"));
						agentUserInfo.setModifyRemark(this.config.getChildText(agentnfo, "agentuser_change_remark"));
						agentUserInfo.setAgentCustomerId(this.config.getChildText(agentnfo, "agentuser_customer_id"));
						
						agentUserList.add(agentUserInfo);
					}
				}
				
				
		    }
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return agentUserList;
	}
	
	
	/**
	 * 查询代理商基本信息
	 * @param accessId
	 * @param config
	 * @param params
	 * @return CallFeeAccount
	 */
	public void queryAgentCustomerInfo(String accessId,List<RequestParameter> params,QRY040016Result res, AgentUserInfo agentUserInfo)
	{
		String reqXml = "";
		String rspXml = "";
		Element root = null;
		String resp_code = "";
		ErrorMapping errDt = null;
		
		String city = null;
		for(RequestParameter param : params){
			if("fixed_ddr_city".equals(param.getParameterName())){
				city = (String) param.getParameterValue();
				break;
			}
		}
		RequestParameter rp = new RequestParameter();
		rp.setParameterName("city");
		rp.setParameterValue(city);
		params.add(rp);
		
		try
		{
			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cqryagentcust_320", params);
			logger.debug(" ====== 查询字典 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml))
			{
				rspXml = (String)this.remote.callRemote(
						 new StringTeletext(reqXml, accessId, "cc_cqryagentcust_320", this.generateCity(params)));
				logger.debug(" ====== 查询字典 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml))
			{
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(root, "response"), "resp_code");
				res.setResultCode("0000".equals(resp_code)?"0":"1");
				if (!"0000".equals(resp_code))
				{
					errDt = this.wellFormedDAO.transBossErrCode("QRY040015", "cc_cqryagentcust_320", resp_code);
					if (null != errDt)
					{
						res.setErrorCode(errDt.getLiErrCode());
						res.setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code))
			{
				
				Element contentElement = this.config.getElement(root, "content");
				Element agentnfoDetail = this.config.getElement(contentElement, "cagentcustomerdt");
				String customerName = this.config.getChildText(agentnfoDetail, "agentcustomer_customer_name");
				String customerAddr = this.config.getChildText(agentnfoDetail, "agentcustomer_ic_addr");
				agentUserInfo.setAgentAddress(customerAddr);
				agentUserInfo.setAgentName(customerName);
		    }
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
	}
	
}
