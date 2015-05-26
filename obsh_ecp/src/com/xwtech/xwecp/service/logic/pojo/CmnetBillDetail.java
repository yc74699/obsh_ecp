package com.xwtech.xwecp.service.logic.pojo;


public class CmnetBillDetail
{
	private String cdrApnni;

	private String cdrStartDate;

	private double dataUpAndDown;

	private String gprsCmnet;
	
	private double callDuration;

	private double otherFee;

	private double pkgFee;

	private String pkgInfo;

	private String startTime;

	private String statusType;

	private double totalFee;

	private String fullTime;

	private double allTotalFee;

	private String tpremark;
	
	private String visitArear;
	
	private double realLfee;
	
	private double baseFeeFlux;
	
	private double otherFeeFlux;

	public double getBaseFeeFlux()
	{
		return baseFeeFlux;
	}

	public void setBaseFeeFlux(double baseFeeFlux) 
	{
		this.baseFeeFlux = baseFeeFlux;
	}

	public double getOtherFeeFlux()
	{
		return otherFeeFlux;
	}

	public void setOtherFeeFlux(double otherFeeFlux) 
	{
		this.otherFeeFlux = otherFeeFlux;
	}

	public String getVisitArear()
	{
		return visitArear;
	}

	public void setVisitArear(String visitArear) 
	{
		this.visitArear = visitArear;
	}

	public void setCdrApnni(String cdrApnni)
	{
		this.cdrApnni = cdrApnni;
	}

	public String getCdrApnni()
	{
		return this.cdrApnni;
	}

	public void setCdrStartDate(String cdrStartDate)
	{
		this.cdrStartDate = cdrStartDate;
	}

	public String getCdrStartDate()
	{
		return this.cdrStartDate;
	}

	public void setDataUpAndDown(double dataUpAndDown)
	{
		this.dataUpAndDown = dataUpAndDown;
	}

	public double getDataUpAndDown()
	{
		return this.dataUpAndDown;
	}

	public void setGprsCmnet(String gprsCmnet)
	{
		this.gprsCmnet = gprsCmnet;
	}

	public String getGprsCmnet()
	{
		return this.gprsCmnet;
	}

	public void setOtherFee(double otherFee)
	{
		this.otherFee = otherFee;
	}

	public double getOtherFee()
	{
		return this.otherFee;
	}

	public void setPkgFee(double pkgFee)
	{
		this.pkgFee = pkgFee;
	}

	public double getPkgFee()
	{
		return this.pkgFee;
	}

	public void setPkgInfo(String pkgInfo)
	{
		this.pkgInfo = pkgInfo;
	}

	public String getPkgInfo()
	{
		return this.pkgInfo;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getStartTime()
	{
		return this.startTime;
	}

	public void setStatusType(String statusType)
	{
		this.statusType = statusType;
	}

	public String getStatusType()
	{
		return this.statusType;
	}

	public void setTotalFee(double totalFee)
	{
		this.totalFee = totalFee;
	}

	public double getTotalFee()
	{
		return this.totalFee;
	}

	public void setFullTime(String fullTime)
	{
		this.fullTime = fullTime;
	}

	public String getFullTime()
	{
		return this.fullTime;
	}

	public void setAllTotalFee(double allTotalFee)
	{
		this.allTotalFee = allTotalFee;
	}

	public double getAllTotalFee()
	{
		return this.allTotalFee;
	}

	public void setTpremark(String tpremark)
	{
		this.tpremark = tpremark;
	}

	public String getTpremark()
	{
		return this.tpremark;
	}

	public double getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(double callDuration) {
		this.callDuration = callDuration;
	}

	public double getRealLfee() {
		return realLfee;
	}

	public void setRealLfee(double realLfee) {
		this.realLfee = realLfee;
	}

}