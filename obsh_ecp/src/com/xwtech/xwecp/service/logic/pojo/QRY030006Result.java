package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ScoreMonth;

public class QRY030006Result extends BaseServiceInvocationResult
{
	private List<ScoreMonth> scoreMonth = new ArrayList<ScoreMonth>();

	public void setScoreMonth(List<ScoreMonth> scoreMonth)
	{
		this.scoreMonth = scoreMonth;
	}

	public List<ScoreMonth> getScoreMonth()
	{
		return this.scoreMonth;
	}

}