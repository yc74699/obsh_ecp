package com.xwtech.xwecp.pojo;

public class ScoreBean {
   
	//积分兑换方式
	private String redeemType;
	
	//积分兑换名称
	private String serverName;
	
	//积分兑换金额
	private String value;

	public String getRedeemType() {
		return redeemType;
	}

	public void setRedeemType(String redeemType) {
		this.redeemType = redeemType;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
