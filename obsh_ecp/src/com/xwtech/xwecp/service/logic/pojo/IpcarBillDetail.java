package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;


public class IpcarBillDetail
{
	private String statusType;
	
	private String visitArear;

	private String otherParty;
	
	private String startTime;

	private String callDuration;

	private double realCfee;

	private double realLfee;

	private double totalFee;
	
	private double freeFee;
	
	private String CdrStartDate;

	public String getCdrStartDate() {
		return CdrStartDate;
	}

	public void setCdrStartDate(String cdrStartDate) {
		CdrStartDate = cdrStartDate;
	}

	public String getVisitArear() {
		return visitArear;
	}

	public void setVisitArear(String visitArear) {
		this.visitArear = visitArear;
	}

	public double getFreeFee() {
		return freeFee;
	}

	public void setFreeFee(double freeFee) {
		this.freeFee = freeFee;
	}

	public void setStatusType(String statusType)
	{
		this.statusType = statusType;
	}

	public String getStatusType()
	{
		return this.statusType;
	}

	public void setOtherParty(String otherParty)
	{
		this.otherParty = otherParty;
	}

	public String getOtherParty()
	{
		return this.otherParty;
	}

	
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getStartTime()
	{
		return this.startTime;
	}

	public void setCallDuration(String callDuration)
	{
		this.callDuration = callDuration;
	}

	public String getCallDuration()
	{
		return this.callDuration;
	}

	public void setRealCfee(double realCfee)
	{
		this.realCfee = realCfee;
	}

	public double getRealCfee()
	{
		return this.realCfee;
	}

	public void setRealLfee(double realLfee)
	{
		this.realLfee = realLfee;
	}

	public double getRealLfee()
	{
		return this.realLfee;
	}

	public void setTotalFee(double totalFee)
	{
		this.totalFee = totalFee;
	}

	public double getTotalFee()
	{
		return this.totalFee;
	}

}