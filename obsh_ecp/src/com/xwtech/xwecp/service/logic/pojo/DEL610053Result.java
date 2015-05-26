package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 积分扣减接口
 * @author 张斌
 * 2015-4-27
 */
public class DEL610053Result extends BaseServiceInvocationResult implements Serializable
{
	private String scoreBal;
	
	private String operatingSrl;
	
	public String getScoreBal() {
		return scoreBal;
	}

	public void setScoreBal(String scoreBal) {
		this.scoreBal = scoreBal;
	}

	public String getOperatingSrl() {
		return operatingSrl;
	}

	public void setOperatingSrl(String operatingSrl) {
		this.operatingSrl = operatingSrl;
	}
	
}