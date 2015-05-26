package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.OtherScores;
import com.xwtech.xwecp.service.logic.pojo.Reserve;
import com.xwtech.xwecp.service.logic.pojo.ScoreDetail;
import com.xwtech.xwecp.service.logic.pojo.ScoreDT;

public class QRY030002Result extends BaseServiceInvocationResult
{
	private String remainScore = "0";

	private List<OtherScores> otherScores = new ArrayList<OtherScores>();

	private List<Reserve> reserve = new ArrayList<Reserve>();

	private List<ScoreDetail> scoreDetail = new ArrayList<ScoreDetail>();

	private ScoreDT scoreDT;

	public void setRemainScore(String remainScore)
	{
		this.remainScore = remainScore;
	}

	public String getRemainScore()
	{
		return this.remainScore;
	}

	public void setOtherScores(List<OtherScores> otherScores)
	{
		this.otherScores = otherScores;
	}

	public List<OtherScores> getOtherScores()
	{
		return this.otherScores;
	}

	public void setReserve(List<Reserve> reserve)
	{
		this.reserve = reserve;
	}

	public List<Reserve> getReserve()
	{
		return this.reserve;
	}

	public void setScoreDetail(List<ScoreDetail> scoreDetail)
	{
		this.scoreDetail = scoreDetail;
	}

	public List<ScoreDetail> getScoreDetail()
	{
		return this.scoreDetail;
	}

	public void setScoreDT(ScoreDT scoreDT)
	{
		this.scoreDT = scoreDT;
	}

	public ScoreDT getScoreDT()
	{
		return this.scoreDT;
	}
}