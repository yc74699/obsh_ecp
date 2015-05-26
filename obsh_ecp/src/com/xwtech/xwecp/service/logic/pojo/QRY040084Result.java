package com.xwtech.xwecp.service.logic.pojo;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;

/**
 * 20元封顶业务
 * @author YangXQ
 * 20140724
 */
public class QRY040084Result extends BaseServiceInvocationResult {
	private String isUnlimitedBandwidth; // 1：用户订购20流量封顶业务0：未订购
	private String useFlux = "0";        // 用户当月流量使用情况
	private HalfFlow halfFlow;           // 半包类套餐使用情况
	private PackageFlow packageFlow;     // 全包类套餐使用情况
	private int flagJIBAO;               //季包标识
	public int getFlagJIBAO() {
		return flagJIBAO;
	}
	public void setFlagJIBAO(int flagJIBAO) {
		this.flagJIBAO = flagJIBAO;
	}
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
	public String getUseFlux() {
		return useFlux;
	}
	public void setUseFlux(String useFlux) {
		this.useFlux = useFlux;
	}	
}
