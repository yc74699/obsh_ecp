package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.PayPlandtBack;

public class DEL040033Result extends BaseServiceInvocationResult
{
	private List<PayPlandtBack> payplandtlist = new ArrayList<PayPlandtBack>();

	public void setPayplandtlist(List<PayPlandtBack> payplandtlist)
	{
		this.payplandtlist = payplandtlist;
	}

	public List<PayPlandtBack> getPayplandtlist()
	{
		return this.payplandtlist;
	}

}