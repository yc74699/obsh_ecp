package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.ScbHisDetail;

public class QRY030014Result extends BaseServiceInvocationResult
{
	private String amount = "";





	private List<ScbHisDetail> scbHisDetailList = new ArrayList<ScbHisDetail>();

	public void setAmount(String amount)
	{
		this.amount = amount;
	}

	public String getAmount()
	{
		return this.amount;
	}



	public void setScbHisDetailList(List<ScbHisDetail> scbHisDetailList)
	{
		this.scbHisDetailList = scbHisDetailList;
	}

	public List<ScbHisDetail> getScbHisDetailList()
	{
		return this.scbHisDetailList;
	}

}