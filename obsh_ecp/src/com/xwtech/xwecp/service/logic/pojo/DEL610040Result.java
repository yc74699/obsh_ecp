package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 订单提交接口
 * @author xufan
 * 2014-8-26 
 *
 */
public class DEL610040Result extends BaseServiceInvocationResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String retcode= "";
	private String retmsg= "";
	private String orderid;
	private String orderstatus;
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	
}
