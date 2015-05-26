package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.MonthScoreDetail;

public class QRY030012Result extends BaseServiceInvocationResult
{
	private List<MonthScoreDetail> scoreDetailList = new ArrayList<MonthScoreDetail>();

	public void setScoreDetailList(List<MonthScoreDetail> scoreDetailList)
	{
		this.scoreDetailList = scoreDetailList;
	}

	public List<MonthScoreDetail> getScoreDetailList()
	{
		return this.scoreDetailList;
	}

}