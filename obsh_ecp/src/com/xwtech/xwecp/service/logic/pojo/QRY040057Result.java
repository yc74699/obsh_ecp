package com.xwtech.xwecp.service.logic.pojo;

import java.util.List;

import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040057Result extends BaseServiceInvocationResult {
	private String totalFlux;
	private String usedFlux;
	private String pkgType;
	private List<GprsPkg> gprsPkgList;
	public String getTotalFlux() {
		return totalFlux;
	}
	public void setTotalFlux(String totalFlux) {
		this.totalFlux = totalFlux;
	}
	public String getUsedFlux() {
		return usedFlux;
	}
	public void setUsedFlux(String usedFlux) {
		this.usedFlux = usedFlux;
	}
	public String getPkgType() {
		return pkgType;
	}
	public void setPkgType(String pkgType) {
		this.pkgType = pkgType;
	}
	public List<GprsPkg> getGprsPkgList() {
		return gprsPkgList;
	}
	public void setGprsPkgList(List<GprsPkg> gprsPkgList) {
		this.gprsPkgList = gprsPkgList;
	}
}
