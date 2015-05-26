package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.AgentSerialNumber;

public class QRY040017Result extends BaseServiceInvocationResult
{
	private List<AgentSerialNumber> agentSerialNumber = new ArrayList<AgentSerialNumber>();

	public void setAgentSerialNumber(List<AgentSerialNumber> agentSerialNumber)
	{
		this.agentSerialNumber = agentSerialNumber;
	}

	public List<AgentSerialNumber> getAgentSerialNumber()
	{
		return this.agentSerialNumber;
	}

}