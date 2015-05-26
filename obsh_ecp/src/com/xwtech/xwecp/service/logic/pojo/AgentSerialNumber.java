package com.xwtech.xwecp.service.logic.pojo;


public class AgentSerialNumber
{
	private String detailBillMonth;

	private String wfseqType;

	private String msisdn;

	private String adMsisdn;

	private String cardAreaCode;

	private String startDate;

	private String amount;
	
	/**
	 * 撤销状态
	 */
	private String arrFlag;
	

	public void setDetailBillMonth(String detailBillMonth)
	{
		this.detailBillMonth = detailBillMonth;
	}

	public String getDetailBillMonth()
	{
		return this.detailBillMonth;
	}

	public void setWfseqType(String wfseqType)
	{
		this.wfseqType = wfseqType;
	}

	public String getWfseqType()
	{
		return this.wfseqType;
	}

	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}

	public String getMsisdn()
	{
		return this.msisdn;
	}

	public void setAdMsisdn(String adMsisdn)
	{
		this.adMsisdn = adMsisdn;
	}

	public String getAdMsisdn()
	{
		return this.adMsisdn;
	}

	public void setCardAreaCode(String cardAreaCode)
	{
		this.cardAreaCode = cardAreaCode;
	}

	public String getCardAreaCode()
	{
		return this.cardAreaCode;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getStartDate()
	{
		return this.startDate;
	}

	public void setAmount(String amount)
	{
		this.amount = amount;
	}

	public String getAmount()
	{
		return this.amount;
	}

	public String getArrFlag() {
		return arrFlag;
	}

	public void setArrFlag(String arrFlag) {
		this.arrFlag = arrFlag;
	}

}