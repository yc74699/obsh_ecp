package com.xwtech.xwecp.pojo;

/**
 * 字典接口
 * @author user
 *
 */
public class ChannelInfo {
  
	private String channelId;
	
	private String channelName;
	
	private String needAuthIp;
	
	private String needAuthPwd;
	
	private String needAuthLi;
	
	private String userNum;
	
	private String password;
	
	private String needFlowContrl;
	
	private String maxConnNum;
	
	private String appId;
	
	private String accessToken;
	
	private String sign;
	
	

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getNeedAuthIp() {
		return needAuthIp;
	}

	public void setNeedAuthIp(String needAuthIp) {
		this.needAuthIp = needAuthIp;
	}

	public String getNeedAuthPwd() {
		return needAuthPwd;
	}

	public void setNeedAuthPwd(String needAuthPwd) {
		this.needAuthPwd = needAuthPwd;
	}

	public String getNeedAuthLi() {
		return needAuthLi;
	}

	public void setNeedAuthLi(String needAuthLi) {
		this.needAuthLi = needAuthLi;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNeedFlowContrl() {
		return needFlowContrl;
	}

	public void setNeedFlowContrl(String needFlowContrl) {
		this.needFlowContrl = needFlowContrl;
	}

	public String getMaxConnNum() {
		return maxConnNum;
	}

	public void setMaxConnNum(String maxConnNum) {
		this.maxConnNum = maxConnNum;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
