package com.xwtech.xwecp.service.logic.pojo;

import java.util.HashMap;
import java.util.Map;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040081Result extends BaseServiceInvocationResult{

	//流量银行 查询流量档次集合
	private Map<String,String> fluxLevelMap = new HashMap<String,String>();

	public Map<String, String> getFluxLevelMap() {
		return fluxLevelMap;
	}

	public void setFluxLevelMap(Map<String, String> fluxLevelMap) {
		this.fluxLevelMap = fluxLevelMap;
	}
	
}
