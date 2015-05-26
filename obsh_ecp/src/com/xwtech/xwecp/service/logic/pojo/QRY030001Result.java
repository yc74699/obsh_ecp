package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.Exchange;

public class QRY030001Result extends BaseServiceInvocationResult
{
	private List<Exchange> exchangeList = new ArrayList<Exchange>();

	public void setExchangeList(List<Exchange> exchangeList)
	{
		this.exchangeList = exchangeList;
	}

	public List<Exchange> getExchangeList()
	{
		return this.exchangeList;
	}

}