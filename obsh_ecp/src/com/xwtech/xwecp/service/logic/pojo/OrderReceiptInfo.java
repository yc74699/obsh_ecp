package com.xwtech.xwecp.service.logic.pojo;

public class OrderReceiptInfo {

	//订单处理状态标识：0-未处理；1-等待处理；2-处理中；3-处理成功；4-处理失败
	private String orderState;
	
	//订单处理失败原因状态（0-未进行失败处理；1-业务冲突；2-余额不足；3-其他）
	private String orderErrorResult;
	
	//订单处理失败原因备注
	private String orderErrorBz;

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getOrderErrorResult() {
		return orderErrorResult;
	}

	public void setOrderErrorResult(String orderErrorResult) {
		this.orderErrorResult = orderErrorResult;
	}

	public String getOrderErrorBz() {
		return orderErrorBz;
	}

	public void setOrderErrorBz(String orderErrorBz) {
		this.orderErrorBz = orderErrorBz;
	}

	

}
