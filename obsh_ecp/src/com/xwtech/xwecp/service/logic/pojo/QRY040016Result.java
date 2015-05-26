package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.AgentUserInfo;

public class QRY040016Result extends BaseServiceInvocationResult
{
	private List<AgentUserInfo> agentUserInfo = new ArrayList<AgentUserInfo>();

	private String allNumber;

	private String stateNormalNumber;

	private String level2Number;

	private String level3Number;

	private List<AgentUserInfo> memberList = new ArrayList<AgentUserInfo>();

	public void setAgentUserInfo(List<AgentUserInfo> agentUserInfo)
	{
		this.agentUserInfo = agentUserInfo;
	}

	public List<AgentUserInfo> getAgentUserInfo()
	{
		return this.agentUserInfo;
	}

	public void setAllNumber(String allNumber)
	{
		this.allNumber = allNumber;
	}

	public String getAllNumber()
	{
		return this.allNumber;
	}

	public void setStateNormalNumber(String stateNormalNumber)
	{
		this.stateNormalNumber = stateNormalNumber;
	}

	public String getStateNormalNumber()
	{
		return this.stateNormalNumber;
	}

	public void setLevel2Number(String level2Number)
	{
		this.level2Number = level2Number;
	}

	public String getLevel2Number()
	{
		return this.level2Number;
	}

	public void setLevel3Number(String level3Number)
	{
		this.level3Number = level3Number;
	}

	public String getLevel3Number()
	{
		return this.level3Number;
	}

	public void setMemberList(List<AgentUserInfo> memberList)
	{
		this.memberList = memberList;
	}

	public List<AgentUserInfo> getMemberList()
	{
		return this.memberList;
	}

}