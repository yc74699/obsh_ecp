package com.xwtech.xwecp.service.logic.pojo;



import com.xwtech.xwecp.service.BaseServiceInvocationResult;

public class QRY040048Result extends BaseServiceInvocationResult {
	private String isUnlimitedBandwidth;
	private String isPlayAt;
	private String useFlux = "0";
	private HalfFlow halfFlow;
	private PackageFlow packageFlow;
	private int flagJIBAO; //季包标识
	public int getFlagJIBAO() {
		return flagJIBAO;
	}
	public void setFlagJIBAO(int flagJIBAO) {
		this.flagJIBAO = flagJIBAO;
	}
	//附加套餐业务标示
	private String isSpecilFlag;
	//4G半包标识
	private String isHalfFlag;
	public HalfFlow getHalfFlow() {
		return halfFlow;
	}
	public void setHalfFlow(HalfFlow halfFlow) {
		this.halfFlow = halfFlow;
	}
	public PackageFlow getPackageFlow() {
		return packageFlow;
	}
	public void setPackageFlow(PackageFlow packageFlow) {
		this.packageFlow = packageFlow;
	}
	public String getIsUnlimitedBandwidth() {
		return isUnlimitedBandwidth;
	}
	public void setIsUnlimitedBandwidth(String isUnlimitedBandwidth) {
		this.isUnlimitedBandwidth = isUnlimitedBandwidth;
	}
	public String getIsPlayAt() {
		return isPlayAt;
	}
	public void setIsPlayAt(String isPlayAt) {
		this.isPlayAt = isPlayAt;
	}
	public String getUseFlux() {
		return useFlux;
	}
	public void setUseFlux(String useFlux) {
		this.useFlux = useFlux;
	}
	public String getIsSpecilFlag() {
		return isSpecilFlag;
	}
	public void setIsSpecilFlag(String isSpecilFlag) {
		this.isSpecilFlag = isSpecilFlag;
	}
	public String getIsHalfFlag() {
		return isHalfFlag;
	}
	public void setIsHalfFlag(String isHalfFlag) {
		this.isHalfFlag = isHalfFlag;
	}
	
}
