package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ExperCfg;

public class QRY050005Result extends BaseServiceInvocationResult
{
	private List<ExperCfg> experCfg = new ArrayList<ExperCfg>();

	public void setExperCfg(List<ExperCfg> experCfg)
	{
		this.experCfg = experCfg;
	}

	public List<ExperCfg> getExperCfg()
	{
		return this.experCfg;
	}

}