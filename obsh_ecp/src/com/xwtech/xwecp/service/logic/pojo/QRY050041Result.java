package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.TransProductBean;

public class QRY050041Result extends BaseServiceInvocationResult
{
	private List<TransProductBean> transProList = new ArrayList<TransProductBean>();

	public void setTransProList(List<TransProductBean> transProList)
	{
		this.transProList = transProList;
	}

	public List<TransProductBean> getTransProList()
	{
		return this.transProList;
	}

}