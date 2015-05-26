package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ScoreReserve;

public class QRY030004Result extends BaseServiceInvocationResult
{
	private List<ScoreReserve> scoreReserve = new ArrayList<ScoreReserve>();

	public void setScoreReserve(List<ScoreReserve> scoreReserve)
	{
		this.scoreReserve = scoreReserve;
	}

	public List<ScoreReserve> getScoreReserve()
	{
		return this.scoreReserve;
	}

}