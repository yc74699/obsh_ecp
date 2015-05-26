package com.xwtech.xwecp.service.logic.pojo;


public class VIPUser
{
	private String userId;

	private String msisdn;

	private String type;

	private String grade;

	private String cardId;

	private String createDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getMsisdn()
	{
		return msisdn;
	}

	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getGrade()
	{
		return grade;
	}

	public void setGrade(String grade)
	{
		this.grade = grade;
	}

	public String getCardId() 
	{
		return cardId;
	}

	public void setCardId(String cardId)
	{
		this.cardId = cardId;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate) 
	{
		this.createDate = createDate;
	}


}