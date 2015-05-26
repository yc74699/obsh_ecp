package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ScoreYearNum;

public class QRY040002Result extends BaseServiceInvocationResult
{
	private String balance = "0";

	private String newBalance = "0";

	private String scoreChangeFlag = "0";

	private String accountExpireDate = "0";

	private String scoreGiftScore = "0";

	private String scoreExchangedScore = "0";

	private String scoreLeavingsScore = "0";

	private String scoreUserId = "";

	private List<ScoreYearNum> scoreYearNum = new ArrayList<ScoreYearNum>();

	private String zoneMvaleTotalMvalue = "0";

	private String zoneMvlaueMvalue = "0";

	private String zoneMvalueLastMvalue = "0";

	private String zoneMvaluePreMvalue = "0";

	private String zoneMvaluePresentMvalue = "0";

	private String zoneMvalueBountyMvalue = "0";

	private String zoneMvalueUsedMvalue = "0";

	public void setBalance(String balance)
	{
		this.balance = balance;
	}

	public String getBalance()
	{
		return this.balance;
	}

	public void setNewBalance(String newBalance)
	{
		this.newBalance = newBalance;
	}

	public String getNewBalance()
	{
		return this.newBalance;
	}

	public void setScoreChangeFlag(String scoreChangeFlag)
	{
		this.scoreChangeFlag = scoreChangeFlag;
	}

	public String getScoreChangeFlag()
	{
		return this.scoreChangeFlag;
	}

	public void setAccountExpireDate(String accountExpireDate)
	{
		this.accountExpireDate = accountExpireDate;
	}

	public String getAccountExpireDate()
	{
		return this.accountExpireDate;
	}

	public void setScoreGiftScore(String scoreGiftScore)
	{
		this.scoreGiftScore = scoreGiftScore;
	}

	public String getScoreGiftScore()
	{
		return this.scoreGiftScore;
	}

	public void setScoreExchangedScore(String scoreExchangedScore)
	{
		this.scoreExchangedScore = scoreExchangedScore;
	}

	public String getScoreExchangedScore()
	{
		return this.scoreExchangedScore;
	}

	public void setScoreLeavingsScore(String scoreLeavingsScore)
	{
		this.scoreLeavingsScore = scoreLeavingsScore;
	}

	public String getScoreLeavingsScore()
	{
		return this.scoreLeavingsScore;
	}

	public void setScoreUserId(String scoreUserId)
	{
		this.scoreUserId = scoreUserId;
	}

	public String getScoreUserId()
	{
		return this.scoreUserId;
	}

	public void setScoreYearNum(List<ScoreYearNum> scoreYearNum)
	{
		this.scoreYearNum = scoreYearNum;
	}

	public List<ScoreYearNum> getScoreYearNum()
	{
		return this.scoreYearNum;
	}

	public void setZoneMvaleTotalMvalue(String zoneMvaleTotalMvalue)
	{
		this.zoneMvaleTotalMvalue = zoneMvaleTotalMvalue;
	}

	public String getZoneMvaleTotalMvalue()
	{
		return this.zoneMvaleTotalMvalue;
	}

	public void setZoneMvlaueMvalue(String zoneMvlaueMvalue)
	{
		this.zoneMvlaueMvalue = zoneMvlaueMvalue;
	}

	public String getZoneMvlaueMvalue()
	{
		return this.zoneMvlaueMvalue;
	}

	public void setZoneMvalueLastMvalue(String zoneMvalueLastMvalue)
	{
		this.zoneMvalueLastMvalue = zoneMvalueLastMvalue;
	}

	public String getZoneMvalueLastMvalue()
	{
		return this.zoneMvalueLastMvalue;
	}

	public void setZoneMvaluePreMvalue(String zoneMvaluePreMvalue)
	{
		this.zoneMvaluePreMvalue = zoneMvaluePreMvalue;
	}

	public String getZoneMvaluePreMvalue()
	{
		return this.zoneMvaluePreMvalue;
	}

	public void setZoneMvaluePresentMvalue(String zoneMvaluePresentMvalue)
	{
		this.zoneMvaluePresentMvalue = zoneMvaluePresentMvalue;
	}

	public String getZoneMvaluePresentMvalue()
	{
		return this.zoneMvaluePresentMvalue;
	}

	public void setZoneMvalueBountyMvalue(String zoneMvalueBountyMvalue)
	{
		this.zoneMvalueBountyMvalue = zoneMvalueBountyMvalue;
	}

	public String getZoneMvalueBountyMvalue()
	{
		return this.zoneMvalueBountyMvalue;
	}

	public void setZoneMvalueUsedMvalue(String zoneMvalueUsedMvalue)
	{
		this.zoneMvalueUsedMvalue = zoneMvalueUsedMvalue;
	}

	public String getZoneMvalueUsedMvalue()
	{
		return this.zoneMvalueUsedMvalue;
	}

}