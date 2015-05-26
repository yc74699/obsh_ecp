package com.xwtech.xwecp.service.logic.pojo;

import java.util.ArrayList;
import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 营销案方案产品变更查询实体类
 * @author wang.huan
 *
 */
public class QRY040098Result extends BaseServiceInvocationResult {
	private String retMsg;
	private String retCode;
	
	private List<UserRewardInfo> userRewardList = new ArrayList<UserRewardInfo>();

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public List<UserRewardInfo> getUserRewardList() {
		return userRewardList;
	}

	public void setUserRewardList(List<UserRewardInfo> userRewardList) {
		this.userRewardList = userRewardList;
	}
	
	
}
