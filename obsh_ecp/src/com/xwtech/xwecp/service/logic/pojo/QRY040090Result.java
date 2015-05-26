package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040090Result extends BaseServiceInvocationResult {
	
	private List<OrderDetail> orderList = new ArrayList<OrderDetail>();

	public List<OrderDetail> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<OrderDetail> orderList) {
		this.orderList = orderList;
	}
	
	
}
