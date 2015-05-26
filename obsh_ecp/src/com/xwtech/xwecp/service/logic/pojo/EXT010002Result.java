package com.xwtech.xwecp.service.logic.pojo;

import java.util.Map;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class EXT010002Result extends BaseServiceInvocationResult {
	private Map<String, Object> map;

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
}
