package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.OrderListInfo;

public class B2B001Result extends BaseServiceInvocationResult
{
	private String amount;

	private List<OrderListInfo> orderList = new ArrayList<OrderListInfo>();

	public void setAmount(String amount)
	{
		this.amount = amount;
	}

	public String getAmount()
	{
		return this.amount;
	}

	public void setOrderList(List<OrderListInfo> orderList)
	{
		this.orderList = orderList;
	}

	public List<OrderListInfo> getOrderList()
	{
		return this.orderList;
	}

}