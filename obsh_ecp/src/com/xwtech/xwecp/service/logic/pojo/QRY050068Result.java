package com.xwtech.xwecp.service.logic.pojo;

import java.util.List;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY050068Result extends BaseServiceInvocationResult {
	private List<CountryNetworkInfo> couNetInfoList;

	public List<CountryNetworkInfo> getCouNetInfoList() {
		return couNetInfoList;
	}

	public void setCouNetInfoList(List<CountryNetworkInfo> couNetInfoList) {
		this.couNetInfoList = couNetInfoList;
	}
}
