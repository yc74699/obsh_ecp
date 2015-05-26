package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 流量和金额兑换查询
 * @author wang.h
 *
 */
public class QRY610054Result  extends BaseServiceInvocationResult implements Serializable{
	private String retCode;
	private String payfee;
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getPayfee() {
		return payfee;
	}
	public void setPayfee(String payfee) {
		this.payfee = payfee;
	}
	
	
}
