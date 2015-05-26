package com.xwtech.xwecp.service.logic.pojo;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import java.util.List;
import java.util.ArrayList;
import com.xwtech.xwecp.service.logic.pojo.OrderInfo;

public class QRY090015Result extends BaseServiceInvocationResult
{
	private List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();

	public void setOrderInfos(List<OrderInfo> orderInfos)
	{
		this.orderInfos = orderInfos;
	}

	public List<OrderInfo> getOrderInfos()
	{
		return this.orderInfos;
	}

}