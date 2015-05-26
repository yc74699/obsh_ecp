package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

public class GprsDayFlux implements Serializable
{
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;
	
	private String dayNum;
	private String gprs2GFlux = "0";
	private String gprs3GFlux = "0";
	private String gprs4GFlux = "0";
	public String getDayNum() {
		return dayNum;
	}
	public void setDayNum(String dayNum) {
		this.dayNum = dayNum;
	}
	public String getGprs2GFlux() {
		return gprs2GFlux;
	}
	public void setGprs2GFlux(String gprs2GFlux) {
		this.gprs2GFlux = gprs2GFlux;
	}
	public String getGprs3GFlux() {
		return gprs3GFlux;
	}
	public void setGprs3GFlux(String gprs3GFlux) {
		this.gprs3GFlux = gprs3GFlux;
	}
	public String getGprs4GFlux() {
		return gprs4GFlux;
	}
	public void setGprs4GFlux(String gprs4GFlux) {
		this.gprs4GFlux = gprs4GFlux;
	}
}
