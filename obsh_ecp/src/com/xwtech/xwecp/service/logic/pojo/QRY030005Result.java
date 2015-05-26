package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.MExchange;

public class QRY030005Result extends BaseServiceInvocationResult
{
	private List<MExchange> mExchange = new ArrayList<MExchange>();

	public void setMExchange(List<MExchange> mExchange)
	{
		this.mExchange = mExchange;
	}

	public List<MExchange> getMExchange()
	{
		return this.mExchange;
	}

}