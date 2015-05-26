package com.xwtech.xwecp.service.logic.pojo;


public class PackageDepend
{
	private String oprateBizCode;

	private String oprateBizName;

	private String optWay;

	private String curStatus;

	private String canChoose;

	private String startDate;

	private String endDate;

	private String nextDate;


	public void setOptWay(String optWay)
	{
		this.optWay = optWay;
	}

	public String getOptWay()
	{
		return this.optWay;
	}

	public void setCurStatus(String curStatus)
	{
		this.curStatus = curStatus;
	}

	public String getCurStatus()
	{
		return this.curStatus;
	}

	public void setCanChoose(String canChoose)
	{
		this.canChoose = canChoose;
	}

	public String getCanChoose()
	{
		return this.canChoose;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getStartDate()
	{
		return this.startDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public String getEndDate()
	{
		return this.endDate;
	}

	public void setNextDate(String nextDate)
	{
		this.nextDate = nextDate;
	}

	public String getNextDate()
	{
		return this.nextDate;
	}

	public String getOprateBizCode() {
		return oprateBizCode;
	}

	public void setOprateBizCode(String oprateBizCode) {
		this.oprateBizCode = oprateBizCode;
	}

	public String getOprateBizName() {
		return oprateBizName;
	}

	public void setOprateBizName(String oprateBizName) {
		this.oprateBizName = oprateBizName;
	}

}