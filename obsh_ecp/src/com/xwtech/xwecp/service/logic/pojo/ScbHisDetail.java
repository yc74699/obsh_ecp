package com.xwtech.xwecp.service.logic.pojo;


public class ScbHisDetail
{
	private String oprSrl;

	private String oprDate;

	private String operator;

	private String operatorName;

	private String oprSource;

	private String oprType;

	private String scoreValue;

	private String chgResonType;

	private String chgResonRemark;

	private String isRollback;
	
	private String subSid;
	
	private String income;

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public void setOprSrl(String oprSrl)
	{
		this.oprSrl = oprSrl;
	}

	public String getOprSrl()
	{
		return this.oprSrl;
	}

	public void setOprDate(String oprDate)
	{
		this.oprDate = oprDate;
	}

	public String getOprDate()
	{
		return this.oprDate;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	public String getOperator()
	{
		return this.operator;
	}

	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}

	public String getOperatorName()
	{
		return this.operatorName;
	}

	public void setOprSource(String oprSource)
	{
		this.oprSource = oprSource;
	}

	public String getOprSource()
	{
		return this.oprSource;
	}

	public void setOprType(String oprType)
	{
		this.oprType = oprType;
	}

	public String getOprType()
	{
		return this.oprType;
	}

	public void setScoreValue(String scoreValue)
	{
		this.scoreValue = scoreValue;
	}

	public String getScoreValue()
	{
		return this.scoreValue;
	}

	public void setChgResonType(String chgResonType)
	{
		this.chgResonType = chgResonType;
	}

	public String getChgResonType()
	{
		return this.chgResonType;
	}

	public void setChgResonRemark(String chgResonRemark)
	{
		this.chgResonRemark = chgResonRemark;
	}

	public String getChgResonRemark()
	{
		return this.chgResonRemark;
	}

	public void setIsRollback(String isRollback)
	{
		this.isRollback = isRollback;
	}

	public String getIsRollback()
	{
		return this.isRollback;
	}

	public String getSubSid() {
		return subSid;
	}

	public void setSubSid(String subSid) {
		this.subSid = subSid;
	}

}