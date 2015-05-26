package com.xwtech.xwecp.service.logic.pojo;


public class GsmBillDetail
{
	private String statusType;

	private String otherParty;

	private String startTime;

	private String callDuration;

	private double firstCfee;

	private double realLfeeAndFirstOfee;

	private double realCfee;

	private double realLfee;

	private double totalFee;

	private double feeItem01;

	private String tpRemark;

	private String visitArear;
	
	private String callType;
	
	private String roamType;
	
	private String freeCode;
	
	private String cdrStartDate;
	
	private String pkgCode;
	
	public String getPkgCode() {
		return pkgCode;
	}

	public void setPkgCode(String pkgCode) {
		this.pkgCode = pkgCode;
	}

	public String getCdrStartDate() {
		return cdrStartDate;
	}

	public void setCdrStartDate(String cdrStartDate) {
		this.cdrStartDate = cdrStartDate;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getRateInd() {
		return rateInd;
	}

	public void setRateInd(String rateInd) {
		this.rateInd = rateInd;
	}

	private String serviceCode;
	
	private String rateInd;

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public String getOtherParty() {
		return otherParty;
	}

	public void setOtherParty(String otherParty) {
		this.otherParty = otherParty;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public double getFirstCfee() {
		return firstCfee;
	}

	public void setFirstCfee(double firstCfee) {
		this.firstCfee = firstCfee;
	}

	public double getRealLfeeAndFirstOfee() {
		return realLfeeAndFirstOfee;
	}

	public void setRealLfeeAndFirstOfee(double realLfeeAndFirstOfee) {
		this.realLfeeAndFirstOfee = realLfeeAndFirstOfee;
	}

	public double getRealCfee() {
		return realCfee;
	}

	public void setRealCfee(double realCfee) {
		this.realCfee = realCfee;
	}

	public double getRealLfee() {
		return realLfee;
	}

	public void setRealLfee(double realLfee) {
		this.realLfee = realLfee;
	}

	public double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}

	public double getFeeItem01() {
		return feeItem01;
	}

	public void setFeeItem01(double feeItem01) {
		this.feeItem01 = feeItem01;
	}

	public String getTpRemark() {
		return tpRemark;
	}

	public void setTpRemark(String tpRemark) {
		this.tpRemark = tpRemark;
	}

	public String getVisitArear() {
		return visitArear;
	}

	public void setVisitArear(String visitArear) {
		this.visitArear = visitArear;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getRoamType() {
		return roamType;
	}

	public void setRoamType(String roamType) {
		this.roamType = roamType;
	}

	public String getFreeCode() {
		return freeCode;
	}

	public void setFreeCode(String freeCode) {
		this.freeCode = freeCode;
	}

}