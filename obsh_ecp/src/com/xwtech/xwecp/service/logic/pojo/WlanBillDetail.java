package com.xwtech.xwecp.service.logic.pojo;


public class WlanBillDetail
{
	private String statusType;
	
	private String visitArear;

	private String startTime;

	private String callDuration;

	private long dataUp;

	private long dataDown;

	private String ispCode;

	private double orgSmFee;

	private double infoFee;

	private double totalFee;
	
	private String authType;
	//计费总流量，单位为BYTE
	private long rateTotalflow;
	
	public long getRateTotalflow()
	{
		return rateTotalflow;
	}
	
	public void setRateTotalflow(long rateTotalflow)
	{
		this.rateTotalflow = rateTotalflow;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public void setStatusType(String statusType)
	{
		this.statusType = statusType;
	}

	public String getStatusType()
	{
		return this.statusType;
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

	public void setDataUp(long dataUp)
	{
		this.dataUp = dataUp;
	}

	public long getDataUp()
	{
		return this.dataUp;
	}

	public void setDataDown(long dataDown)
	{
		this.dataDown = dataDown;
	}

	public long getDataDown()
	{
		return this.dataDown;
	}

	public void setIspCode(String ispCode)
	{
		this.ispCode = ispCode;
	}

	public String getIspCode()
	{
		return this.ispCode;
	}

	public void setOrgSmFee(double orgSmFee)
	{
		this.orgSmFee = orgSmFee;
	}

	public double getOrgSmFee()
	{
		return this.orgSmFee;
	}

	public void setInfoFee(double infoFee)
	{
		this.infoFee = infoFee;
	}

	public double getInfoFee()
	{
		return this.infoFee;
	}

	public void setTotalFee(double totalFee)
	{
		this.totalFee = totalFee;
	}

	public double getTotalFee()
	{
		return this.totalFee;
	}

	public String getVisitArear() {
		return visitArear;
	}

	public void setVisitArear(String visitArear) {
		this.visitArear = visitArear;
	}

}